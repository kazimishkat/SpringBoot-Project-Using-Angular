package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.MedicineRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.MedicineResponseDto;
import com.mishkat.PharmacyManagement.entity.Medicine;

public class MedicineMapper {
    public static MedicineResponseDto toDTO(Medicine medicine) {
        if (medicine == null) return null;

        MedicineResponseDto dto = new MedicineResponseDto();
        dto.setId(medicine.getId());
        dto.setMedicineCode(medicine.getMedicineCode());
        dto.setBrandName(medicine.getBrandName());
        dto.setManufacturer(medicine.getManufacturer());
        dto.setDosageForm(medicine.getDosageForm());
        dto.setStrength(medicine.getStrength());
        dto.setUnitOfMeasure(medicine.getUnitOfMeasure());
        dto.setUnitsPerPack(medicine.getUnitsPerPack());
        dto.setDrugSchedule(medicine.getDrugSchedule());
        dto.setStorageCondition(medicine.getStorageCondition());
        dto.setReorderLevel(medicine.getReorderLevel());
        dto.setReorderQuantity(medicine.getReorderQuantity());
        dto.setDefaultPurchasePrice(medicine.getDefaultPurchasePrice());
        dto.setDefaultSellingPrice(medicine.getDefaultSellingPrice());
        dto.setIsActive(medicine.getIsActive());
        dto.setImage(medicine.getImage());

        if (medicine.getGenericMedicine() != null) {
            dto.setGenericMedicineId(medicine.getGenericMedicine().getId());
            dto.setGenericName(medicine.getGenericMedicine().getGenericName());
        }

        return dto;
    }

    public static Medicine toEntity(MedicineRequestDto dto) {
        if (dto == null) return null;

        Medicine medicine = new Medicine();
        medicine.setMedicineCode(dto.getMedicineCode());
        medicine.setBrandName(dto.getBrandName());
        medicine.setManufacturer(dto.getManufacturer());
        medicine.setDosageForm(dto.getDosageForm());
        medicine.setStrength(dto.getStrength());
        medicine.setUnitOfMeasure(dto.getUnitOfMeasure());
        medicine.setUnitsPerPack(dto.getUnitsPerPack());
        medicine.setDrugSchedule(dto.getDrugSchedule());
        medicine.setStorageCondition(dto.getStorageCondition());
        medicine.setReorderLevel(dto.getReorderLevel());
        medicine.setReorderQuantity(dto.getReorderQuantity());
        medicine.setDefaultPurchasePrice(dto.getDefaultPurchasePrice());
        medicine.setDefaultSellingPrice(dto.getDefaultSellingPrice());

        // genericMedicineId Service-এ প্রসেস করতে হবে

        return medicine;
    }

    public static void updateEntity(Medicine medicine, MedicineRequestDto dto) {
        if (dto == null) return;

        if (dto.getBrandName() != null) medicine.setBrandName(dto.getBrandName());
        if (dto.getManufacturer() != null) medicine.setManufacturer(dto.getManufacturer());
        if (dto.getDosageForm() != null) medicine.setDosageForm(dto.getDosageForm());
        if (dto.getDefaultSellingPrice() != null) medicine.setDefaultSellingPrice(dto.getDefaultSellingPrice());
        if (dto.getReorderLevel() != null) medicine.setReorderLevel(dto.getReorderLevel());

    }
}
