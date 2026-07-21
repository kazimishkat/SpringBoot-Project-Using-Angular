package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.BranchInventoryMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.BranchInventoryRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.BranchInventoryResponseDto;
import com.mishkat.PharmacyManagement.entity.Branch;
import com.mishkat.PharmacyManagement.entity.BranchInventory;
import com.mishkat.PharmacyManagement.entity.MedicineBatch;
import com.mishkat.PharmacyManagement.repository.BranchInventoryRepository;
import com.mishkat.PharmacyManagement.repository.BranchRepository;
import com.mishkat.PharmacyManagement.repository.MedicineBatchRepository;
import com.mishkat.PharmacyManagement.service.BranchInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchInventoryServiceImpl implements BranchInventoryService {

    private final BranchInventoryRepository branchInventoryRepository;
    private final BranchRepository branchRepository;
    private final MedicineBatchRepository medicineBatchRepository;

    private final BranchInventoryMapper mapper = new BranchInventoryMapper();

    @Override
    @Transactional(readOnly = true)
    public List<BranchInventoryResponseDto> getAllInventory() {
        return branchInventoryRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BranchInventoryResponseDto getInventoryById(Long id) {
        BranchInventory inventory = branchInventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory records missing with verification identity: " + id));
        return mapper.toDTO(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchInventoryResponseDto> getInventoryByBranch(Long branchId) {
        return branchInventoryRepository.findByBranchId(branchId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchInventoryResponseDto> getInventoryByMedicine(Long medicineId) {
        return branchInventoryRepository.findByMedicineId(medicineId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchInventoryResponseDto> getLowStock(Integer threshold) {
        int finalThreshold = (threshold != null) ? threshold : 10;
        return branchInventoryRepository.findByQuantityOnHandLessThanEqual(finalThreshold).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchInventoryResponseDto> getOutOfStock() {
        return branchInventoryRepository.findByQuantityOnHandLessThanEqual(0).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchInventoryResponseDto> getExpiringInventory(LocalDate beforeDate) {
        LocalDate targetDate = (beforeDate != null) ? beforeDate : LocalDate.now();
        return branchInventoryRepository.findByBatchExpiryDateBefore(targetDate).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    // ── 🟢 🌟 [NEW]: GRN Approved হলে স্টক Auto Add করার লজিক ──
    @Override
    @Transactional
    public void addStock(Long branchId, Long batchId, Integer quantity) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found with ID: " + branchId));

        MedicineBatch batch = medicineBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Medicine Batch not found with ID: " + batchId));

        Optional<BranchInventory> inventoryOptional = branchInventoryRepository.findByBranchIdAndBatchId(branchId, batchId);

        int addedQty = (quantity != null) ? quantity : 0;

        if (inventoryOptional.isPresent()) {
            // ১. ইনভেন্টরিতে রেকর্ড থাকলে পরিমাণ বাড়িয়ে দেওয়া (quantityOnHand কলাম ধরে)
            BranchInventory inventory = inventoryOptional.get();
            int currentQty = (inventory.getQuantityOnHand() != null) ? inventory.getQuantityOnHand() : 0;
            inventory.setQuantityOnHand(currentQty + addedQty);
            branchInventoryRepository.save(inventory);
        } else {
            // ২. রেকর্ড না থাকলে নতুন BranchInventory এন্ট্রি তৈরি করা
            BranchInventory newInventory = new BranchInventory();
            newInventory.setBranch(branch);
            newInventory.setBatch(batch);
            newInventory.setQuantityOnHand(addedQty);
            newInventory.setIsActive(true);
            branchInventoryRepository.save(newInventory);
        }
    }

    // ── 🔴 🌟 [NEW]: GRN Cancelled হলে স্টক Auto Deduct/Reverse করার লজিক ──
    @Override
    @Transactional
    public void deductStock(Long branchId, Long batchId, Integer quantity) {
        BranchInventory inventory = branchInventoryRepository.findByBranchIdAndBatchId(branchId, batchId)
                .orElseThrow(() -> new RuntimeException("Branch Inventory record not found for branch ID: " + branchId + " and batch ID: " + batchId));

        int deductQty = (quantity != null) ? quantity : 0;
        int currentQty = (inventory.getQuantityOnHand() != null) ? inventory.getQuantityOnHand() : 0;

        if (currentQty < deductQty) {
            throw new RuntimeException("Insufficient stock in branch inventory to reverse GRN! Available: "
                    + currentQty + ", Required: " + deductQty);
        }

        inventory.setQuantityOnHand(currentQty - deductQty);
        branchInventoryRepository.save(inventory);
    }
}
