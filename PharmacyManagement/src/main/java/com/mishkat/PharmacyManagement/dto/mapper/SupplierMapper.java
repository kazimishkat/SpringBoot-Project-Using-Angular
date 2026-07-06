package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.SupplierRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.SupplierResponseDto;
import com.mishkat.PharmacyManagement.entity.Supplier;

public class SupplierMapper {
    /**
     * Converts SupplierRequestDto to Supplier Entity for creation/updates.
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

        // Handling nested Address mapping (Assumes AddressMapper exists)
        if (dto.getAddress() != null) {
            // If Address is an @Embedded object, you typically instantiate it directly
            // or use an AddressMapper if you have one.
            // Example: supplier.setAddress(AddressMapper.toEntity(dto.getAddress()));
        }

        return supplier;
    }

    /**
     * Converts Supplier Entity to SupplierResponseDto.
     */
    public SupplierResponseDto toDTO(Supplier supplier) {
        if (supplier == null) {
            return null;
        }

        SupplierResponseDto dto = new SupplierResponseDto();

        // Inherited fields from BaseEntity (assuming id and isActive exist there)
        dto.setId(supplier.getId());
        dto.setIsActive(supplier.getIsActive());

        // Supplier specific fields
        dto.setSupplierCode(supplier.getSupplierCode());
        dto.setName(supplier.getName());
        dto.setContactPerson(supplier.getContactPerson());
        dto.setPhone(supplier.getPhone());
        dto.setEmail(supplier.getEmail());
        dto.setTradeLicenseNo(supplier.getTradeLicenseNo());
        dto.setTaxId(supplier.getTaxId());

        // Handling nested Address response mapping
        if (supplier.getAddress() != null) {
            // Example: dto.setAddress(AddressMapper.toResponseDto(supplier.getAddress()));
        }

        return dto;
    }

    /**
     * Updates an existing Supplier entity using data from a SupplierRequestDto.
     * Useful for PUT/PATCH update operations.
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

        if (dto.getAddress() != null) {
            // Handle address update logic here
        }
    }
}
