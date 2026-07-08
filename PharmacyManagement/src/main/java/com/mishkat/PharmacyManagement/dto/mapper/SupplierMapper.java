package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.SupplierRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.SupplierResponseDto;
import com.mishkat.PharmacyManagement.entity.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SupplierMapper {

    private final AddressMapper addressMapper;

    /**
     * Convert SupplierRequestDto to Supplier Entity
     */
    public Supplier toEntity(SupplierRequestDto dto) {

        if (dto == null) {
            return null;
        }

        Supplier supplier = new Supplier();

        supplier.setSupplierCode(dto.getSupplierCode());
        supplier.setName(dto.getName());
        supplier.setContactPerson(dto.getContactPerson());
        supplier.setPhone(dto.getPhone());
        supplier.setEmail(dto.getEmail());
        supplier.setTradeLicenseNo(dto.getTradeLicenseNo());
        supplier.setTaxId(dto.getTaxId());

        supplier.setAddress(addressMapper.toEntity(dto.getAddress()));

        return supplier;
    }

    /**
     * Convert Supplier Entity to SupplierResponseDto
     */
    public SupplierResponseDto toDTO(Supplier supplier) {

        if (supplier == null) {
            return null;
        }

        SupplierResponseDto dto = new SupplierResponseDto();

        dto.setId(supplier.getId());
        dto.setIsActive(supplier.getIsActive());

        dto.setSupplierCode(supplier.getSupplierCode());
        dto.setName(supplier.getName());
        dto.setContactPerson(supplier.getContactPerson());
        dto.setPhone(supplier.getPhone());
        dto.setEmail(supplier.getEmail());
        dto.setTradeLicenseNo(supplier.getTradeLicenseNo());
        dto.setTaxId(supplier.getTaxId());

        dto.setAddress(addressMapper.toDto(supplier.getAddress()));

        return dto;
    }

    /**
     * Update existing Supplier Entity from DTO
     */
    public void updateEntityFromDto(SupplierRequestDto dto, Supplier supplier) {

        if (dto == null || supplier == null) {
            return;
        }

        supplier.setSupplierCode(dto.getSupplierCode());
        supplier.setName(dto.getName());
        supplier.setContactPerson(dto.getContactPerson());
        supplier.setPhone(dto.getPhone());
        supplier.setEmail(dto.getEmail());
        supplier.setTradeLicenseNo(dto.getTradeLicenseNo());
        supplier.setTaxId(dto.getTaxId());

        supplier.setAddress(addressMapper.toEntity(dto.getAddress()));
    }

}
