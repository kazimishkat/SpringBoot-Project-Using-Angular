package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.StockMovementRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.StockMovementResponseDto;
import com.mishkat.PharmacyManagement.entity.StockMovement;

import java.time.LocalDateTime;

public class StockMovementMapper {
    /**
     * Request DTO থেকে StockMovement Entity-তে রূপান্তর।
     * লক্ষ্য করুন: এনটিটিতে `movementDate` ফিল্ডটি নট-নাল (nullable = false)।
     * যেহেতু এটি রিকোয়েস্ট DTO-তে নেই, তাই অবজেক্ট তৈরির সময় আমরা কারেন্ট টাইম সেট করে দিচ্ছি।
     */
    public StockMovement toEntity(StockMovementRequestDto dto) {
        if (dto == null) {
            return null;
        }

        StockMovement movement = new StockMovement();
        movement.setMovementType(dto.getMovementType());
        movement.setQuantity(dto.getQuantity());
        movement.setReferenceType(dto.getReferenceType());
        movement.setReferenceId(dto.getReferenceId());

        // মুভমেন্ট বা ট্রানজেকশনটি ঠিক কখন ঘটছে তা ট্র্যাক করার জন্য কারেন্ট টাইম সেট করা হচ্ছে
        movement.setMovementDate(LocalDateTime.now());

        // নোট: branchId এবং batchId-এর মূল অবজেক্টগুলো সার্ভিস লেয়ারে
        // রেপজিটরি থেকে তুলে এনে সেট করতে হবে। (যেমন: movement.setBranch(branch))

        return movement;
    }

    /**
     * StockMovement Entity থেকে Response DTO-তে রূপান্তর।
     * অডিট লগের সুবিধার্থে এখানে BaseEntity থেকে আসা `createdAt` এবং `createdBy` ফিল্ডগুলোও ম্যাপ করা হয়েছে।
     */
    public StockMovementResponseDto toDTO(StockMovement movement) {
        if (movement == null) {
            return null;
        }

        StockMovementResponseDto dto = new StockMovementResponseDto();

        // BaseEntity থেকে অডিট ট্র্যাকিং ফিল্ড ম্যাপিং
        dto.setId(movement.getId());
        dto.setCreatedAt(movement.getCreatedAt());
        dto.setCreatedBy(movement.getCreatedBy()); // ধরে নেওয়া হয়েছে BaseEntity-তে Spring Security অডিটিং সেট করা আছে

        // সাধারণ ট্রানজেকশন ডেটা ম্যাপিং
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

    /**
     * গুরুত্বপূর্ণ সতর্কতা: সাধারণত StockMovement বা স্টক লেজার টেবিলের ডেটা "Immutable" বা অপরিবর্তনশীল হয়।
     * একবার কোনো স্টকের হিস্ট্রি বা মুভমেন্ট এন্ট্রি হয়ে গেলে তা এডিট বা আপডেট করা অ্যাকাউন্টিং/ইনভেন্টরি নিয়মের পরিপন্থী।
     * যদি কোনো এন্ট্রি ভুল হয়, তবে নতুন আরেকটি রিভার্স এন্ট্রি দিয়ে তা ঠিক করতে হয়।
     * তাই এই ম্যাপারে কোনো `updateEntityFromDto` মেথড রাখা হয়নি।
     */
}
