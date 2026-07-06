package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.MedicineRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.MedicineResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MedicineService {
    // Added MultipartFile image
    MedicineResponseDto createMedicine(MedicineRequestDto dto, MultipartFile image);

    List<MedicineResponseDto> getAllMedicines();

    MedicineResponseDto getMedicineById(Long id);

    MedicineResponseDto getMedicineByCode(String medicineCode);

    List<MedicineResponseDto> searchMedicinesByBrandName(String brandName);

    List<MedicineResponseDto> getLowStockMedicines();

    // Added MultipartFile image
    MedicineResponseDto updateMedicine(Long id, MedicineRequestDto dto, MultipartFile image);

    void deleteMedicine(Long id);
}
