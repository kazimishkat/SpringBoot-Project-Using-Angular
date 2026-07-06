package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.DeliveryCompanyRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.DeliveryCompanyResponseDto;

import java.util.List;

public interface DeliveryCompanyService {
    DeliveryCompanyResponseDto registerCompany(DeliveryCompanyRequestDto dto);
    List<DeliveryCompanyResponseDto> getActiveCompanies();
    DeliveryCompanyResponseDto getCompanyById(Long id);
    DeliveryCompanyResponseDto updateCompanyStatus(Long id, Boolean isActive);
}
