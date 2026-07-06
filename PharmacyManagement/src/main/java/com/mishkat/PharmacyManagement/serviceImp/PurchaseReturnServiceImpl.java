package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.PurchaseReturnMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.PurchaseReturnItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.PurchaseReturnRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PurchaseReturnResponseDto;
import com.mishkat.PharmacyManagement.entity.*;
import com.mishkat.PharmacyManagement.repository.BranchRepository;
import com.mishkat.PharmacyManagement.repository.MedicineBatchRepository;
import com.mishkat.PharmacyManagement.repository.PurchaseReturnRepository;
import com.mishkat.PharmacyManagement.repository.SupplierRepository;
import com.mishkat.PharmacyManagement.service.PurchaseReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseReturnServiceImpl implements PurchaseReturnService {
    private final PurchaseReturnRepository purchaseReturnRepository;
    private final SupplierRepository supplierRepository;
    private final BranchRepository branchRepository;
    private final MedicineBatchRepository medicineBatchRepository;

    // ম্যাপারের মেথডগুলো স্ট্যাটিক না হওয়ায় ইনস্ট্যান্স তৈরি করা হলো
    private final PurchaseReturnMapper mapper = new PurchaseReturnMapper();

    @Override
    @Transactional
    public PurchaseReturnResponseDto createPurchaseReturn(PurchaseReturnRequestDto dto) {
        // ১. ইউনিক Return Number চেক করা
        // Repository তে Optional<PurchaseReturn> findByReturnNumber(String returnNumber) থাকতে হবে
        if (purchaseReturnRepository.findByReturnNumber(dto.getReturnNumber()).isPresent()) {
            throw new RuntimeException("Purchase Return already exists with Return Number: " + dto.getReturnNumber());
        }

        PurchaseReturn purchaseReturn = mapper.toEntity(dto);

        // ২. Supplier অবজেক্ট সেট করা
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + dto.getSupplierId()));
        purchaseReturn.setSupplier(supplier);

        // ৩. Branch অবজেক্ট সেট করা
        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + dto.getBranchId()));
        purchaseReturn.setBranch(branch);

        // ৪. প্রতিটি Item এর জন্য MedicineBatch সেট করা
        if (purchaseReturn.getItems() != null && dto.getItems() != null) {
            for (int i = 0; i < purchaseReturn.getItems().size(); i++) {
                PurchaseReturnItem item = purchaseReturn.getItems().get(i);
                PurchaseReturnItemRequestDto itemDto = dto.getItems().get(i);

                MedicineBatch batch = medicineBatchRepository.findById(itemDto.getBatchId())
                        .orElseThrow(() -> new RuntimeException("Medicine Batch not found with id: " + itemDto.getBatchId()));
                item.setBatch(batch);
            }
        }

        PurchaseReturn savedReturn = purchaseReturnRepository.save(purchaseReturn);
        return mapper.toDTO(savedReturn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseReturnResponseDto> getAllPurchaseReturns() {
        return purchaseReturnRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseReturnResponseDto getPurchaseReturnById(Long id) {
        PurchaseReturn purchaseReturn = purchaseReturnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Return not found with id: " + id));
        return mapper.toDTO(purchaseReturn);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseReturnResponseDto getPurchaseReturnByNumber(String returnNumber) {
        PurchaseReturn purchaseReturn = purchaseReturnRepository.findByReturnNumber(returnNumber)
                .orElseThrow(() -> new RuntimeException("Purchase Return not found with Return Number: " + returnNumber));
        return mapper.toDTO(purchaseReturn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseReturnResponseDto> getReturnsBySupplierId(Long supplierId) {
        // Repository তে List<PurchaseReturn> findBySupplierId(Long supplierId) থাকতে হবে
        return purchaseReturnRepository.findBySupplierId(supplierId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PurchaseReturnResponseDto updatePurchaseReturn(Long id, PurchaseReturnRequestDto dto) {
        PurchaseReturn existingReturn = purchaseReturnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Return not found with id: " + id));

        // Return Number পরিবর্তন হলে ডুপ্লিকেট চেক
        if (dto.getReturnNumber() != null && !existingReturn.getReturnNumber().equals(dto.getReturnNumber())) {
            if (purchaseReturnRepository.findByReturnNumber(dto.getReturnNumber()).isPresent()) {
                throw new RuntimeException("Return Number already exists: " + dto.getReturnNumber());
            }
        }

        // যেহেতু এটি চাইল্ড লিস্ট (Items) সহ একটি কমপ্লেক্স অবজেক্ট,
        // ম্যাপার দিয়ে নতুন করে Entity জেনারেট করে পুরোনোটার ওপর ওভাররাইট করা নিরাপদ।
        PurchaseReturn updatedData = mapper.toEntity(dto);

        existingReturn.setReturnNumber(updatedData.getReturnNumber());
        existingReturn.setReturnDate(updatedData.getReturnDate());

        // Supplier আপডেট
        if (!existingReturn.getSupplier().getId().equals(dto.getSupplierId())) {
            Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found"));
            existingReturn.setSupplier(supplier);
        }

        // Branch আপডেট
        if (!existingReturn.getBranch().getId().equals(dto.getBranchId())) {
            Branch branch = branchRepository.findById(dto.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
            existingReturn.setBranch(branch);
        }

        // Items আপডেট (পুরোনো লিস্ট ক্লিয়ার করে নতুনগুলো অ্যাড করা, orphanRemoval এটা হ্যান্ডেল করবে)
        existingReturn.getItems().clear();
        if (updatedData.getItems() != null && dto.getItems() != null) {
            for (int i = 0; i < updatedData.getItems().size(); i++) {
                PurchaseReturnItem newItem = updatedData.getItems().get(i);
                PurchaseReturnItemRequestDto itemDto = dto.getItems().get(i);

                MedicineBatch batch = medicineBatchRepository.findById(itemDto.getBatchId())
                        .orElseThrow(() -> new RuntimeException("Medicine Batch not found"));

                newItem.setBatch(batch);
                newItem.setPurchaseReturn(existingReturn); // Parent পুনরায় সেট করা
                existingReturn.getItems().add(newItem);
            }
        }

        PurchaseReturn savedReturn = purchaseReturnRepository.save(existingReturn);
        return mapper.toDTO(savedReturn);
    }

    @Override
    @Transactional
    public void deletePurchaseReturn(Long id) {
        PurchaseReturn purchaseReturn = purchaseReturnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Return not found with id: " + id));
        purchaseReturnRepository.delete(purchaseReturn);
    }
}
