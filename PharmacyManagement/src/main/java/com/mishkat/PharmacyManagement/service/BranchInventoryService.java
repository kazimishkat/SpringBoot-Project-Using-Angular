package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.BranchInventoryRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.BranchInventoryResponseDto;

import java.util.List;

public interface BranchInventoryService {
    BranchInventoryResponseDto createInventory(BranchInventoryRequestDto dto);

    List<BranchInventoryResponseDto> getAllInventories();

    List<BranchInventoryResponseDto> getInventoriesByBranch(Long branchId);

    BranchInventoryResponseDto getInventoryById(Long id);

    BranchInventoryResponseDto getInventoryByBranchAndBatch(Long branchId, Long batchId);

    Integer getTotalQuantityByBranchAndMedicine(Long branchId, Long medicineId);

    BranchInventoryResponseDto updateInventory(Long id, BranchInventoryRequestDto dto);

    void deleteInventory(Long id);
}
