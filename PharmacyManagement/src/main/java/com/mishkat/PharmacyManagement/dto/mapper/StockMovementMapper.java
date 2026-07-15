package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.StockMovementRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.StockMovementResponseDto;
import com.mishkat.PharmacyManagement.entity.StockMovement;

import java.time.LocalDateTime;

public class StockMovementMapper {
    public StockMovementResponseDto toDTO(StockMovement movement) {
        if (movement == null) {
            return null;
        }

        StockMovementResponseDto dto = new StockMovementResponseDto();

        // BaseEntity থেকে অডিট ট্র্যাকিং ফিল্ড ম্যাপিং
        dto.setId(movement.getId());
        dto.setCreatedAt(movement.getCreatedAt());
        dto.setCreatedBy(movement.getCreatedBy());

        // সাধারণ ট্রনজেকশন ডেটা ম্যাপিং
        dto.setMovementType(movement.getMovementType());
        dto.setQuantity(movement.getQuantity());
        dto.setReferenceType(movement.getReferenceType());
        dto.setReferenceId(movement.getReferenceId());

        // Branch অবজেক্ট থেকে ডেটা ম্যাপিং
        if (movement.getBranch() != null) {
            dto.setBranchId(movement.getBranch().getId());
            dto.setBranchName(movement.getBranch().getName());
        }

        // MedicineBatch এবং nested Medicine অবজেক্ট চেইন থেকে ডেটা ম্যাপিং
        if (movement.getBatch() != null) {
            dto.setBatchId(movement.getBatch().getId());
            dto.setBatchNumber(movement.getBatch().getBatchNumber());

            // ব্যাচের ভেতর থেকে মেডিসিনের নাম বের করা হচ্ছে
            if (movement.getBatch().getMedicine() != null) {
                dto.setMedicineName(movement.getBatch().getMedicine().getBrandName());
            }
        }

        return dto;
    }
}
