package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.StockAdjustmentRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.StockAdjustmentResponseDto;

import java.util.List;

public interface StockAdjustmentService {
    StockAdjustmentResponseDto createStockAdjustment(StockAdjustmentRequestDto dto);

    List<StockAdjustmentResponseDto> getAllStockAdjustments();

    StockAdjustmentResponseDto getStockAdjustmentById(Long id);

    StockAdjustmentResponseDto getStockAdjustmentByNumber(String adjustmentNumber);

    List<StockAdjustmentResponseDto> getStockAdjustmentsByBranchId(Long branchId);

    StockAdjustmentResponseDto updateStockAdjustment(Long id, StockAdjustmentRequestDto dto);

    void deleteStockAdjustment(Long id);
}
