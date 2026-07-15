package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.StockMovementRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.StockMovementResponseDto;
import com.mishkat.PharmacyManagement.enums.StockMovementType;

import java.util.List;

public interface StockMovementService {
    // ── Internal Service Communication API (No Public Controller Access) ──
    void recordMovement(Long branchId, Long batchId, StockMovementType movementType,
                        Integer quantity, String referenceType, Long referenceId, String remarks);

    // ── Reporting & Audit Trail APIs ──────────────────────────────────────
    List<StockMovementResponseDto> getAllStockMovements();

    StockMovementResponseDto getStockMovementById(Long id);

    List<StockMovementResponseDto> getMovementsByBranchId(Long branchId);

    List<StockMovementResponseDto> getMovementsByBatchId(Long batchId);

    List<StockMovementResponseDto> getMovementsByBranchAndType(Long branchId, StockMovementType movementType);

    List<StockMovementResponseDto> getMovementsByReference(String referenceType, Long referenceId);
}
