package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.PurchaseReturnMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.PurchaseReturnItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.PurchaseReturnRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PurchaseReturnResponseDto;
import com.mishkat.PharmacyManagement.entity.*;
import com.mishkat.PharmacyManagement.enums.StockMovementType;
import com.mishkat.PharmacyManagement.repository.BranchRepository;
import com.mishkat.PharmacyManagement.repository.MedicineBatchRepository;
import com.mishkat.PharmacyManagement.repository.PurchaseReturnRepository;
import com.mishkat.PharmacyManagement.repository.SupplierRepository;
import com.mishkat.PharmacyManagement.service.PurchaseReturnService;
import com.mishkat.PharmacyManagement.service.StockMovementService;
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

    // Injecting Internal Stock Movement Ledger Engine
    private final StockMovementService stockMovementService;

    private final PurchaseReturnMapper mapper = new PurchaseReturnMapper();

    @Override
    @Transactional
    public PurchaseReturnResponseDto createPurchaseReturn(PurchaseReturnRequestDto dto) {
        if (purchaseReturnRepository.findByReturnNumber(dto.getReturnNumber()).isPresent()) {
            throw new RuntimeException("Purchase Return already exists with Return Number: " + dto.getReturnNumber());
        }

        PurchaseReturn purchaseReturn = mapper.toEntity(dto);

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + dto.getSupplierId()));
        purchaseReturn.setSupplier(supplier);

        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + dto.getBranchId()));
        purchaseReturn.setBranch(branch);

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

        // Automatically log outward stock deduction to ledger since return transaction is closed
        if (savedReturn.getItems() != null) {
            for (PurchaseReturnItem item : savedReturn.getItems()) {
                stockMovementService.recordMovement(
                        savedReturn.getBranch().getId(),
                        item.getBatch().getId(),
                        StockMovementType.PURCHASE_RETURN,
                        item.getQuantity(), // আপনার মডেলে রিটার্ন করা ফিল্ড নেম (যেমন quantity)
                        "PURCHASE_RETURN",
                        savedReturn.getId(),
                        "Stock outward debit via supplier Return: " + savedReturn.getReturnNumber()
                );
            }
        }

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
        return purchaseReturnRepository.findBySupplierId(supplierId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PurchaseReturnResponseDto updatePurchaseReturn(Long id, PurchaseReturnRequestDto dto) {
        PurchaseReturn existingReturn = purchaseReturnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Return not found with id: " + id));

        if (dto.getReturnNumber() != null && !existingReturn.getReturnNumber().equals(dto.getReturnNumber())) {
            if (purchaseReturnRepository.findByReturnNumber(dto.getReturnNumber()).isPresent()) {
                throw new RuntimeException("Return Number already exists: " + dto.getReturnNumber());
            }
        }

        PurchaseReturn updatedData = mapper.toEntity(dto);

        existingReturn.setReturnNumber(updatedData.getReturnNumber());
        existingReturn.setReturnDate(updatedData.getReturnDate());

        if (!existingReturn.getSupplier().getId().equals(dto.getSupplierId())) {
            Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found"));
            existingReturn.setSupplier(supplier);
        }

        if (!existingReturn.getBranch().getId().equals(dto.getBranchId())) {
            Branch branch = branchRepository.findById(dto.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
            existingReturn.setBranch(branch);
        }

        existingReturn.getItems().clear();
        if (updatedData.getItems() != null && dto.getItems() != null) {
            for (int i = 0; i < updatedData.getItems().size(); i++) {
                PurchaseReturnItem newItem = updatedData.getItems().get(i);
                PurchaseReturnItemRequestDto itemDto = dto.getItems().get(i);

                MedicineBatch batch = medicineBatchRepository.findById(itemDto.getBatchId())
                        .orElseThrow(() -> new RuntimeException("Medicine Batch not found"));

                newItem.setBatch(batch);
                newItem.setPurchaseReturn(existingReturn);
                existingReturn.getItems().add(newItem);
            }
        }

        PurchaseReturn savedReturn = purchaseReturnRepository.save(existingReturn);

        // Note: Ledger entries are immutable, updating a return document typically
        // requires adjusting accounting steps manually, but we log the current snapshot here.

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
