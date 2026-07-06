package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.StockMovementRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.StockMovementResponseDto;
import com.mishkat.PharmacyManagement.enums.StockMovementType;

import java.util.List;

public interface StockMovementService {
    StockMovementResponseDto createStockMovement(StockMovementRequestDto dto);

    List<StockMovementResponseDto> getAllStockMovements();

    StockMovementResponseDto getStockMovementById(Long id);

    List<StockMovementResponseDto> getMovementsByBranchId(Long branchId);

    List<StockMovementResponseDto> getMovementsByBatchId(Long batchId);

    List<StockMovementResponseDto> getMovementsByBranchAndType(Long branchId, StockMovementType movementType);

    List<StockMovementResponseDto> getMovementsByReference(String referenceType, Long referenceId);

    // Note: No update or delete methods provided to maintain ledger immutability.
}
