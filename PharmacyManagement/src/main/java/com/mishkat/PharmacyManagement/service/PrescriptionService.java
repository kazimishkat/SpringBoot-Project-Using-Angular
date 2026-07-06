package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.PrescriptionRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PrescriptionResponseDto;
import com.mishkat.PharmacyManagement.entity.Prescription;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PrescriptionService {
    PrescriptionResponseDto createPrescription(PrescriptionRequestDto dto, MultipartFile file);

    List<PrescriptionResponseDto> getAllPrescriptions();

    PrescriptionResponseDto getPrescriptionById(Long id);

    List<PrescriptionResponseDto> getPrescriptionsByCustomerId(Long customerId);

    List<PrescriptionResponseDto> searchByDoctorName(String doctorName);

    PrescriptionResponseDto updatePrescription(Long id, PrescriptionRequestDto dto, MultipartFile file);

    void deletePrescription(Long id);

    Prescription getPrescriptionEntityById(Long id);
}
