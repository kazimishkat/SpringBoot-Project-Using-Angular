package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.DeliveryCompanyRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.DeliveryCompanyResponseDto;
import com.mishkat.PharmacyManagement.entity.DeliveryCompany;

public class DeliveryCompanyMapper {
    public DeliveryCompanyResponseDto toDTO(DeliveryCompany entity) {
        if (entity == null) return null;

        DeliveryCompanyResponseDto dto = new DeliveryCompanyResponseDto();
        dto.setId(entity.getId());
        dto.setCompanyName(entity.getCompanyName());
        dto.setContactPerson(entity.getContactPerson());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setIsActive(entity.getIsActive());

        return dto;
    }

    public DeliveryCompany toEntity(DeliveryCompanyRequestDto dto) {
        if (dto == null) return null;

        DeliveryCompany entity = new DeliveryCompany();
        entity.setCompanyName(dto.getCompanyName());
        entity.setContactPerson(dto.getContactPerson());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setApiKey(dto.getApiKey());
        entity.setIsActive(true); // নতুন পার্টনার বাই-ডিফল্ট একটিভ

        return entity;
    }
}
