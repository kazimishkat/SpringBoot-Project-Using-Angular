package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.GenericMedicineRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.GenericMedicineResponseDto;

import java.util.List;

public interface GenericMedicineService {
    GenericMedicineResponseDto createGenericMedicine(GenericMedicineRequestDto dto);

    List<GenericMedicineResponseDto> getAllGenericMedicines();

    GenericMedicineResponseDto getGenericMedicineById(Long id);

    GenericMedicineResponseDto getByGenericName(String genericName);

    GenericMedicineResponseDto updateGenericMedicine(Long id, GenericMedicineRequestDto dto);

    void deleteGenericMedicine(Long id);
}
