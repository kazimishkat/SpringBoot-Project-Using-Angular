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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchInventoryServiceImpl implements BranchInventoryService {

    private final BranchInventoryRepository branchInventoryRepository;
    private final BranchRepository branchRepository;
    private final MedicineBatchRepository medicineBatchRepository;

    // যেহেতু আপনার Mapper ক্লাসের মেথডগুলো static নয়, তাই এর একটি ইন্সট্যান্স তৈরি করে নেওয়া হলো
    private final BranchInventoryMapper mapper = new BranchInventoryMapper();

    @Override
    @Transactional
    public BranchInventoryResponseDto createInventory(BranchInventoryRequestDto dto) {
        // একই ব্রাঞ্চ এবং ব্যাচের জন্য ইনভেন্টরি আগে থেকেই আছে কি না তা চেক করা
        if (branchInventoryRepository.findByBranchIdAndBatchId(dto.getBranchId(), dto.getBatchId()).isPresent()) {
            throw new RuntimeException("Inventory already exists for this branch and batch combination.");
        }

        // Branch এবং Batch ডাটাবেস থেকে খুঁজে আনা
        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + dto.getBranchId()));

        MedicineBatch batch = medicineBatchRepository.findById(dto.getBatchId())
                .orElseThrow(() -> new RuntimeException("Medicine Batch not found with id: " + dto.getBatchId()));

        BranchInventory inventory = mapper.toEntity(dto);
        inventory.setBranch(branch);
        inventory.setBatch(batch);

        BranchInventory savedInventory = branchInventoryRepository.save(inventory);
        return mapper.toDTO(savedInventory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchInventoryResponseDto> getAllInventories() {
        return branchInventoryRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchInventoryResponseDto> getInventoriesByBranch(Long branchId) {
        return branchInventoryRepository.findByBranchId(branchId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BranchInventoryResponseDto getInventoryById(Long id) {
        BranchInventory inventory = branchInventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));
        return mapper.toDTO(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public BranchInventoryResponseDto getInventoryByBranchAndBatch(Long branchId, Long batchId) {
        BranchInventory inventory = branchInventoryRepository.findByBranchIdAndBatchId(branchId, batchId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for the given branch and batch."));
        return mapper.toDTO(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalQuantityByBranchAndMedicine(Long branchId, Long medicineId) {
        Integer total = branchInventoryRepository.getTotalQuantityByBranchAndMedicine(branchId, medicineId);
        return total != null ? total : 0;
    }

    @Override
    @Transactional
    public BranchInventoryResponseDto updateInventory(Long id, BranchInventoryRequestDto dto) {
        BranchInventory inventory = branchInventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));

        // আপনার Mapper-এর লজিক অনুযায়ী শুধুমাত্র quantityOnHand আপডেট করা হচ্ছে
        mapper.updateEntityFromDto(dto, inventory);

        BranchInventory savedInventory = branchInventoryRepository.save(inventory);
        return mapper.toDTO(savedInventory);
    }

    @Override
    @Transactional
    public void deleteInventory(Long id) {
        BranchInventory inventory = branchInventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));
        branchInventoryRepository.delete(inventory);
    }
}
