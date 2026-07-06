package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.MedicineBatchRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.MedicineBatchResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface MedicineBatchService {
    MedicineBatchResponseDto createBatch(MedicineBatchRequestDto dto);

    List<MedicineBatchResponseDto> getAllBatches();

    MedicineBatchResponseDto getBatchById(Long id);

    List<MedicineBatchResponseDto> getBatchesByMedicine(Long medicineId);

    List<MedicineBatchResponseDto> getBatchesByNumber(String batchNumber);

    List<MedicineBatchResponseDto> getExpiringBatches(LocalDate date);

    MedicineBatchResponseDto updateBatch(Long id, MedicineBatchRequestDto dto);

    void deleteBatch(Long id);
}
