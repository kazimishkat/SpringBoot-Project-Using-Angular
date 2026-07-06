package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.PrescriptionRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PrescriptionResponseDto;
import com.mishkat.PharmacyManagement.entity.Prescription;

public class PrescriptionMapper {
    public PrescriptionResponseDto toDTO(Prescription entity) {
        if (entity == null) return null;

        PrescriptionResponseDto dto = new PrescriptionResponseDto();
        dto.setId(entity.getId());
        dto.setDoctorName(entity.getDoctorName());
        dto.setHospitalOrClinic(entity.getHospitalOrClinic());
        dto.setPrescriptionDate(entity.getPrescriptionDate());
        dto.setRemarks(entity.getRemarks());

        if (entity.getCustomer() != null) {
            dto.setCustomerId(entity.getCustomer().getId());
            dto.setCustomerName(entity.getCustomer().getName());
        }

        return dto;
    }

    public Prescription toEntity(PrescriptionRequestDto dto) {
        if (dto == null) return null;

        Prescription entity = new Prescription();
        entity.setDoctorName(dto.getDoctorName());
        entity.setHospitalOrClinic(dto.getHospitalOrClinic());
        entity.setPrescriptionDate(dto.getPrescriptionDate());
        entity.setRemarks(dto.getRemarks());

        return entity;
    }
}
