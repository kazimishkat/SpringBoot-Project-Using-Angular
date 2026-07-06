package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.MedicineCategoryRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.MedicineCategoryResponseDto;
import com.mishkat.PharmacyManagement.entity.MedicineCategory;

public class MedicineCategoryMapper {/**
 * Request DTO থেকে MedicineCategory Entity-তে রূপান্তর করার পদ্ধতি।
 * এটি সাধারণত ডাটাবেজে নতুন ক্যাটাগরি Save বা Update করার সময় ব্যবহার করা হয়।
 */
public MedicineCategory toEntity(MedicineCategoryRequestDto dto) {
    // DTO যদি null হয়, তবে অবজেক্ট তৈরি না করে সরাসরি null রিটার্ন করবে যেন NullPointerException না হয়।
    if (dto == null) {
        return null;
    }

    MedicineCategory category = new MedicineCategory();

    // Request DTO থেকে ডেটা নিয়ে Entity অবজেক্টে সেট করা হচ্ছে
    category.setName(dto.getName());
    category.setDescription(dto.getDescription());

    return category;
}

    /**
     * MedicineCategory Entity থেকে Response DTO-তে রূপান্তর করার পদ্ধতি।
     * ডাটাবেজ থেকে ডেটা তুলে এনে ক্লায়েন্ট বা ফ্রন্টএন্ডে পাঠানোর জন্য এটি ব্যবহার করা হয়।
     */
    public MedicineCategoryResponseDto toDTO(MedicineCategory category) {
        if (category == null) {
            return null;
        }

        MedicineCategoryResponseDto dto = new MedicineCategoryResponseDto();

        // এখানে id, isActive, এবং createdAt ফিল্ডগুলো BaseEntity থেকে ইনহেরিট হয়ে (পাস করে) এসেছে।
        // তাই এগুলো সরাসরি category অবজেক্ট থেকে গেট (get) করে DTO-তে সেট করা হচ্ছে।
        dto.setId(category.getId());
        dto.setIsActive(category.getIsActive());
        dto.setCreatedAt(category.getCreatedAt());

        // MedicineCategory-এর নিজস্ব ফিল্ডগুলো ম্যাপ করা হচ্ছে
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());

        return dto;
    }

    /**
     * বিদ্যমান (Existing) কোনো Entity-কে Request DTO-এর ডেটা দিয়ে আপডেট করার পদ্ধতি।
     * এটি সাধারণত PUT বা PATCH রিকোয়েস্টের মাধ্যমে ক্যাটাগরি এডিট করার সময় কাজে লাগে।
     */
    public void updateEntityFromDto(MedicineCategoryRequestDto dto, MedicineCategory category) {
        if (dto == null || category == null) {
            return;
        }

        // ডাটাবেজের পুরানো ডেটার ওপর নতুন DTO-এর ডেটা ওভাররাইট (Overwrite) করা হচ্ছে
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
    }
}
