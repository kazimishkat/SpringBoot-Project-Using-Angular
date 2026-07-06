package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.SupplierRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.SupplierResponseDto;

import java.util.List;

public interface SupplierService {
    // ── Supplier profile management ──────────────────────────────────
    SupplierResponseDto createSupplier(SupplierRequestDto dto);

    List<SupplierResponseDto> getAllSuppliers();

    SupplierResponseDto getSupplierById(Long id);

    SupplierResponseDto getSupplierByCode(String supplierCode);

    SupplierResponseDto updateSupplier(Long id, SupplierRequestDto dto);

    void deleteSupplier(Long id);
}
