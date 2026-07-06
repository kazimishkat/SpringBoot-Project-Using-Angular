package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.GenericMedicineRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.GenericMedicineResponseDto;
import com.mishkat.PharmacyManagement.entity.GenericMedicine;

public class GenericMedicineMapper {
    // 🟢 Entity → Response DTO
    public static GenericMedicineResponseDto toDTO(GenericMedicine genericMedicine) {
        if (genericMedicine == null) return null;

        GenericMedicineResponseDto dto = new GenericMedicineResponseDto();
        dto.setId(genericMedicine.getId());
        dto.setGenericName(genericMedicine.getGenericName());
        dto.setDescription(genericMedicine.getDescription());
        dto.setIndication(genericMedicine.getIndication());
        dto.setSideEffects(genericMedicine.getSideEffects());
        dto.setContraindications(genericMedicine.getContraindications());
        dto.setIsActive(genericMedicine.getIsActive()); // BaseEntity থেকে আসা ফিল্ড

        // চাইল্ড অবজেক্টের রিলেশন ভেঙে সরাসরি ফ্ল্যাট ফিল্ডে ডেটা বসানো হচ্ছে
        if (genericMedicine.getCategory() != null) {
            dto.setCategoryId(genericMedicine.getCategory().getId());
            dto.setCategoryName(genericMedicine.getCategory().getName());
        }

        return dto;
    }

    // 🟢 Request DTO → Entity (category অবজেক্টটি সার্ভিস লেয়ারে ID দিয়ে খুঁজে সেট করতে হবে)
    public static GenericMedicine toEntity(GenericMedicineRequestDto dto) {
        if (dto == null) return null;

        GenericMedicine genericMedicine = new GenericMedicine();
        genericMedicine.setGenericName(dto.getGenericName());
        genericMedicine.setDescription(dto.getDescription());
        genericMedicine.setIndication(dto.getIndication());
        genericMedicine.setSideEffects(dto.getSideEffects());
        genericMedicine.setContraindications(dto.getContraindications());

        // ডিফল্টভাবে নতুন এন্ট্রি একটিভ থাকবে
        genericMedicine.setIsActive(true);

        // NOTE: dto.getCategoryId() এর অবজেক্টটি সার্ভিস লেয়ারে ডেটাবেস থেকে খুঁজে
        // genericMedicine.setCategory(category) করতে হবে।

        return genericMedicine;
    }

    // 🟢 Apply request DTO onto existing entity (ফর আপডেট)
    public static void updateEntity(GenericMedicine genericMedicine, GenericMedicineRequestDto dto) {
        if (dto == null || genericMedicine == null) return;

        // ডেটাবেসের এক্সিস্টিং অবজেক্টে নাল-সেফটি চেক করে শুধু ইনপুট দেওয়া ফিল্ডগুলো আপডেট করা হচ্ছে
        if (dto.getGenericName() != null) genericMedicine.setGenericName(dto.getGenericName());
        if (dto.getDescription() != null) genericMedicine.setDescription(dto.getDescription());
        if (dto.getIndication() != null) genericMedicine.setIndication(dto.getIndication());
        if (dto.getSideEffects() != null) genericMedicine.setSideEffects(dto.getSideEffects());
        if (dto.getContraindications() != null) genericMedicine.setContraindications(dto.getContraindications());

        // categoryId চেঞ্জ করার লজিকটি সার্ভিস লেয়ারে রিপোজিটরির সাহায্যে এক্সিকিউট করতে হবে
    }
}
