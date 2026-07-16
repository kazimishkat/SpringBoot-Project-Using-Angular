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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchInventoryServiceImpl implements BranchInventoryService {

    private final BranchInventoryRepository branchInventoryRepository;
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
        // থ্রেশহোল্ড প্যারামিটার নাল হলে বাই-ডিফল্ট ১০ ইউনিট বা তার নিচে ধরা হবে
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
        // ডেট দেওয়া না থাকলে আজকের দিনের সাপেক্ষে মেয়াদোত্তীর্ণ ওষুধ ফিল্টার হবে
        LocalDate targetDate = (beforeDate != null) ? beforeDate : LocalDate.now();
        return branchInventoryRepository.findByBatchExpiryDateBefore(targetDate).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
