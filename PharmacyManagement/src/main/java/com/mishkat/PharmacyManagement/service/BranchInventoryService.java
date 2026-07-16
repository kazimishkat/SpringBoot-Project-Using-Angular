package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.BranchInventoryRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.BranchInventoryResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface BranchInventoryService {
    List<BranchInventoryResponseDto> getAllInventory();

    BranchInventoryResponseDto getInventoryById(Long id);

    List<BranchInventoryResponseDto> getInventoryByBranch(Long branchId);

    List<BranchInventoryResponseDto> getInventoryByMedicine(Long medicineId);

    List<BranchInventoryResponseDto> getLowStock(Integer threshold);

    List<BranchInventoryResponseDto> getOutOfStock();

    List<BranchInventoryResponseDto> getExpiringInventory(LocalDate beforeDate);
}
