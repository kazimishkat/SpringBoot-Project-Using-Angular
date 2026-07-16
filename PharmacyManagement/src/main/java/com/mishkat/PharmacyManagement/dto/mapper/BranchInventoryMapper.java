package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.BranchInventoryRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.BranchInventoryResponseDto;
import com.mishkat.PharmacyManagement.entity.BranchInventory;
import com.mishkat.PharmacyManagement.entity.MedicineBatch;
import com.mishkat.PharmacyManagement.entity.Medicine;

public class BranchInventoryMapper {

    /**
     * Request DTO থেকে BranchInventory Entity-তে রূপান্তর।
     */
    public BranchInventory toEntity(BranchInventoryRequestDto dto) {
        if (dto == null) {
            return null;
        }

        BranchInventory inventory = new BranchInventory();
        inventory.setQuantityOnHand(dto.getQuantityOnHand());
        inventory.setQuantityReserved(0); // প্রাথমিক ডিফল্ট মান অ্যাসাইনমেন্ট

        // নোট: branchId এবং batchId-এর মূল রেফারেন্সগুলো সার্ভিস লেয়ারে
        // রেপজিটরি থেকে ডাটাবেস কোয়েরি করে ডাইনামিক সেট করতে হবে।

        return inventory;
    }

    /**
     * BranchInventory Entity থেকে Response DTO-তে রূপান্তর।
     * চেইন রিলেশন (Inventory -> Batch -> Medicine -> GenericMedicine -> Category) নাল-সেফ উপায়ে ম্যাপ করা হয়েছে।
     */
    public BranchInventoryResponseDto toDTO(BranchInventory inventory) {
        if (inventory == null) {
            return null;
        }

        BranchInventoryResponseDto dto = new BranchInventoryResponseDto();

        // BaseEntity থেকে ইউনিক আইডি এবং অডিট টাইমস্ট্যাম্প রিকভারি
        dto.setId(inventory.getId());
        dto.setLastUpdated(inventory.getUpdatedAt());

        // ইনভেন্টরি স্টক পজিশন ফিল্ডস
        dto.setQuantityOnHand(inventory.getQuantityOnHand());
        dto.setQuantityReserved(inventory.getQuantityReserved());

        // 1. Branch অবজেক্ট ডাটা ম্যাপিং
        if (inventory.getBranch() != null) {
            dto.setBranchId(inventory.getBranch().getId());
            dto.setBranchName(inventory.getBranch().getName());
        }

        // 2. MedicineBatch এবং তার ভেতরের রিলেশন চেইন ইন্টারসেপশন
        if (inventory.getBatch() != null) {
            MedicineBatch batch = inventory.getBatch();
            dto.setBatchId(batch.getId());
            dto.setBatchNumber(batch.getBatchNumber());
            dto.setExpiryDate(batch.getExpiryDate());
            dto.setPurchasePrice(batch.getPurchasePrice());
            dto.setSellingPrice(batch.getSellingPrice());

            // Supplier অবজেক্ট রিলেশন হ্যান্ডেলিং (Lombok getName() মেথড ট্রিগার)
            if (batch.getSupplier() != null) {
                dto.setSupplierName(batch.getSupplier().getName());
            }

            // 3. Medicine অবজেক্ট চেইন ডাটা এক্সট্রাকশন (আপনার শেয়ার করা এনটিটি অনুযায়ী ফিল্ড ম্যাপিং)
            if (batch.getMedicine() != null) {
                Medicine medicine = batch.getMedicine();
                dto.setMedicineBrandName(medicine.getBrandName());
                dto.setReorderLevel(medicine.getReorderLevel()); // আপনার মডেলে থাকা reorderLevel ফিল্ড

                // 4. Medicine -> GenericMedicine চেইন ও জেনেরিক নেম রিকভারি
                if (medicine.getGenericMedicine() != null) {
                    dto.setGenericName(medicine.getGenericMedicine().getGenericName());

                    // 5. GenericMedicine থেকে ক্যাটাগরি অবজেক্টের নাম তুলে আনা হচ্ছে (NPE প্রোটেকশনসহ)
                    if (medicine.getGenericMedicine().getCategory() != null) {
                        dto.setCategoryName(medicine.getGenericMedicine().getCategory().getName());
                    }
                }
            }
        }

        return dto;
    }

    /**
     * বিদ্যমান ইনভেন্টরি রেকর্ড নতুন রিকোয়েস্টের ডেটা দিয়ে আপডেট করার পদ্ধতি।
     */
    public void updateEntityFromDto(BranchInventoryRequestDto dto, BranchInventory inventory) {
        if (dto == null || inventory == null) {
            return;
        }

        inventory.setQuantityOnHand(dto.getQuantityOnHand());
        // ইনভেন্টরি কন্ট্রোল পলিসি অনুযায়ী reserved কোয়ান্টিটি সরাসরি এডিট ব্লক রাখা হয়েছে।
    }
}