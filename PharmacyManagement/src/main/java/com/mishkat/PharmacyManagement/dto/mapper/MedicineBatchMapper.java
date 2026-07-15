package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.MedicineBatchRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.MedicineBatchResponseDto;
import com.mishkat.PharmacyManagement.entity.MedicineBatch;

public class MedicineBatchMapper {
    /**
     * Request DTO থেকে MedicineBatch Entity-তে রূপান্তর।
     * এখানে রিলেশনাল আইডি (medicineId, supplierId) গুলোকে সরাসরি ম্যাপ করা যাবে না।
     * ডাটাবেজে সেভ করার আগে সার্ভিস লেয়ারে রিপোজিটরি থেকে মূল এনটিটি অবজেক্ট নিয়ে এসে সেট করতে হবে।
     */
    public MedicineBatch toEntity(MedicineBatchRequestDto dto) {
        if (dto == null) {
            return null;
        }

        MedicineBatch batch = new MedicineBatch();
        batch.setBatchNumber(dto.getBatchNumber());
        batch.setManufactureDate(dto.getManufactureDate());
        batch.setExpiryDate(dto.getExpiryDate());
        batch.setPurchasePrice(dto.getPurchasePrice());
        batch.setSellingPrice(dto.getSellingPrice());

        // নোট: এখানে সরাসরি medicine এবং supplier সেট করা যাবে না, কারণ DTO-তে শুধু আইডি আছে।
        // সার্ভিস লেয়ারে medicineRepository.findById(dto.getMedicineId()) দিয়ে অবজেক্ট এনে নিচে ম্যাপ করতে হবে।

        return batch;
    }

    /**
     * MedicineBatch Entity থেকে Response DTO-তে রূপান্তর।
     * ক্লায়েন্ট বা ফ্রন্টএন্ডের সুবিধার জন্য অবজেক্ট থেকে আইডি ও নাম বের করে ফ্ল্যাট (Flat) প্রোপার্টিতে ম্যাপ করা হচ্ছে।
     */
    public MedicineBatchResponseDto toDTO(MedicineBatch batch) {
        if (batch == null) {
            return null;
        }

        MedicineBatchResponseDto dto = new MedicineBatchResponseDto();

        // BaseEntity-র ফিল্ড ম্যাপিং
        dto.setId(batch.getId());
        dto.setIsActive(batch.getIsActive());

        // সাধারণ ফিল্ড ম্যাপিং
        dto.setBatchNumber(batch.getBatchNumber());
        dto.setManufactureDate(batch.getManufactureDate());
        dto.setExpiryDate(batch.getExpiryDate());
        dto.setPurchasePrice(batch.getPurchasePrice());
        dto.setSellingPrice(batch.getSellingPrice());

        // nested object 'Medicine' থেকে আইডি এবং ব্র্যান্ডের নাম বের করে DTO-তে ফ্ল্যাট করা হচ্ছে।
        if (batch.getMedicine() != null) {
            dto.setMedicineId(batch.getMedicine().getId());
            dto.setBrandName(batch.getMedicine().getBrandName());
        }

        // nested object 'Supplier' থেকে আইডি এবং সাপ্লায়ারের নাম বের করে DTO-তে ম্যাপ করা হচ্ছে।
        if (batch.getSupplier() != null) {
            dto.setSupplierId(batch.getSupplier().getId());
            dto.setSupplierName(batch.getSupplier().getName());
        }

        return dto;
    }

    /**
     * বিদ্যমান (Existing) ব্যাচ অবজেক্টকে নতুন ডেটা দিয়ে আপডেট করার পদ্ধতি।
     */
    public void updateEntityFromDto(MedicineBatchRequestDto dto, MedicineBatch batch) {
        if (dto == null || batch == null) {
            return;
        }

        batch.setBatchNumber(dto.getBatchNumber());
        batch.setManufactureDate(dto.getManufactureDate());
        batch.setExpiryDate(dto.getExpiryDate());
        batch.setPurchasePrice(dto.getPurchasePrice());
        batch.setSellingPrice(dto.getSellingPrice());

        // নোট: মেডিসিন বা সাপ্লায়ারের আইডি পরিবর্তন হলে, সেটিও সার্ভিস লেয়ারে ম্যানুয়ালি হ্যান্ডেল বা সেট করতে হবে।
    }
}
