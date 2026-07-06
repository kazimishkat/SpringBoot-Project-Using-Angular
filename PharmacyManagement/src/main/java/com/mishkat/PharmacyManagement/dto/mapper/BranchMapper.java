package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.BranchRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.AddressResponseDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.BranchResponseDto;
import com.mishkat.PharmacyManagement.entity.Address;
import com.mishkat.PharmacyManagement.entity.Branch;

public class BranchMapper {
    // Entity → Response DTO
    public static BranchResponseDto toDTO(Branch branch) {
        if (branch == null) return null;

        BranchResponseDto dto = new BranchResponseDto();
        dto.setId(branch.getId());
        dto.setBranchCode(branch.getBranchCode());
        dto.setName(branch.getName());
        dto.setBranchType(branch.getBranchType());
        dto.setPhone(branch.getPhone());
        dto.setEmail(branch.getEmail());
        dto.setLicenseNumber(branch.getLicenseNumber());
        dto.setManagerName(branch.getManagerName());
        dto.setIsActive(branch.getIsActive());
        dto.setCreatedAt(branch.getCreatedAt());
        dto.setUpdatedAt(branch.getUpdatedAt());

        // Embedded Address ম্যানুয়ালি ম্যাপ করা হচ্ছে
        if (branch.getAddress() != null) {
            AddressResponseDto addressDto = new AddressResponseDto();
            addressDto.setAddressLine1(branch.getAddress().getAddressLine1());
            addressDto.setAddressLine2(branch.getAddress().getAddressLine2());
            addressDto.setCity(branch.getAddress().getCity());
            addressDto.setState(branch.getAddress().getState());
            addressDto.setPostalCode(branch.getAddress().getPostalCode());
            addressDto.setCountry(branch.getAddress().getCountry());
            dto.setAddress(addressDto);
        }

        return dto;
    }

    // Request DTO → Entity
    public static Branch toEntity(BranchRequestDto dto) {
        if (dto == null) return null;

        Branch branch = new Branch();
        branch.setBranchCode(dto.getBranchCode());
        branch.setName(dto.getName());
        branch.setBranchType(dto.getBranchType());
        branch.setPhone(dto.getPhone());
        branch.setEmail(dto.getEmail());
        branch.setLicenseNumber(dto.getLicenseNumber());
        branch.setManagerName(dto.getManagerName());

        // Address Entity তৈরি করে সেট করা
        if (dto.getAddress() != null) {
            Address address = new Address();
            address.setAddressLine1(dto.getAddress().getAddressLine1());
            address.setAddressLine2(dto.getAddress().getAddressLine2());
            address.setCity(dto.getAddress().getCity());
            address.setState(dto.getAddress().getState());
            address.setPostalCode(dto.getAddress().getPostalCode());
            address.setCountry(dto.getAddress().getCountry());
            branch.setAddress(address);
        }

        return branch;
    }

    // Apply request DTO onto existing entity
    public static void updateEntity(Branch branch, BranchRequestDto dto) {
        if (dto == null) return;

        if (dto.getName() != null) branch.setName(dto.getName());
        if (dto.getBranchType() != null) branch.setBranchType(dto.getBranchType());
        if (dto.getPhone() != null) branch.setPhone(dto.getPhone());
        if (dto.getEmail() != null) branch.setEmail(dto.getEmail());
        if (dto.getLicenseNumber() != null) branch.setLicenseNumber(dto.getLicenseNumber());
        if (dto.getManagerName() != null) branch.setManagerName(dto.getManagerName());

        // Address আপডেট লজিক
        if (dto.getAddress() != null) {
            Address address = branch.getAddress();
            if (address == null) {
                address = new Address();
                branch.setAddress(address);
            }
            if (dto.getAddress().getAddressLine1() != null) address.setAddressLine1(dto.getAddress().getAddressLine1());
            if (dto.getAddress().getCity() != null) address.setCity(dto.getAddress().getCity());
            if (dto.getAddress().getPostalCode() != null) address.setPostalCode(dto.getAddress().getPostalCode());
            if (dto.getAddress().getCountry() != null) address.setCountry(dto.getAddress().getCountry());
        }
    }
}
