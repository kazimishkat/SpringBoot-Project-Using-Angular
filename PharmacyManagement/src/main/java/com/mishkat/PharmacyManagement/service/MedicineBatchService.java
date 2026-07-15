package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.MedicineBatchRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.MedicineBatchResponseDto;
import com.mishkat.PharmacyManagement.entity.MedicineBatch;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface MedicineBatchService {
    // ── Internal Engine Communication API (Called by GRN Process) ──
    MedicineBatch createOrUpdateBatch(Long medicineId, String batchNumber, Long supplierId,
                                      LocalDate manufactureDate, LocalDate expiryDate,
                                      BigDecimal purchasePrice, BigDecimal sellingPrice);

    // ── Read-Only & Reporting APIs ──
    List<MedicineBatchResponseDto> getAllBatches();

    MedicineBatchResponseDto getBatchById(Long id);

    List<MedicineBatchResponseDto> getBatchesByMedicine(Long medicineId);

    List<MedicineBatchResponseDto> getBatchesByNumber(String batchNumber);

    List<MedicineBatchResponseDto> getExpiringBatches(LocalDate date);

    // Mapped back to MedicineBatchRequestDto to match your Mapper file
    MedicineBatchResponseDto updateBatch(Long id, MedicineBatchRequestDto dto);
}
