package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.BranchInventoryRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.BranchInventoryResponseDto;
import com.mishkat.PharmacyManagement.entity.BranchInventory;

public class BranchInventoryMapper {
    /**
     * Request DTO থেকে BranchInventory Entity-তে রূপান্তর।
     * এখানে quantityReserved ফিল্ডটি DTO-তে রাখা হয়নি, কারণ ইনভেন্টরি তৈরি বা আপডেটের সময়
     * এটি সাধারণত ডিফল্টভাবে 0 থাকে অথবা বিজনেস লজিক দিয়ে হ্যান্ডেল করা হয়।
     */
    public BranchInventory toEntity(BranchInventoryRequestDto dto) {
        if (dto == null) {
            return null;
        }

        BranchInventory inventory = new BranchInventory();
        inventory.setQuantityOnHand(dto.getQuantityOnHand());

        // ডিফল্ট মান সেট করা হচ্ছে (যদি ডাটাবেজে অন্য কোনো ভ্যালু না পাঠানো হয়)
        inventory.setQuantityReserved(0);

        // নোট: branchId এবং batchId-এর আসল অবজেক্টগুলো সার্ভিস লেয়ারে
        // রেপজিটরি (Repository) থেকে খুঁজে নিয়ে এসে সেট করতে হবে।
        // যেমন: inventory.setBranch(branch);

        return inventory;
    }

    /**
     * BranchInventory Entity থেকে Response DTO-তে রূপান্তর।
     * এখানে nested অবজেক্টগুলোর চেইন (Inventory -> Batch -> Medicine) থেকে ডেটা তুলে এনে
     * ফ্ল্যাট প্রোপার্টিতে ম্যাপ করা হয়েছে যা ফ্রন্টএন্ড বা ক্লায়েন্টের জন্য পড়া খুব সহজ।
     */
    public BranchInventoryResponseDto toDTO(BranchInventory inventory) {
        if (inventory == null) {
            return null;
        }

        BranchInventoryResponseDto dto = new BranchInventoryResponseDto();

        // BaseEntity থেকে আসা ID
        dto.setId(inventory.getId());

        // ইনভেন্টরি ফিল্ড ম্যাপিং
        dto.setQuantityOnHand(inventory.getQuantityOnHand());
        dto.setQuantityReserved(inventory.getQuantityReserved());

        // Branch অবজেক্ট থেকে ডেটা ম্যাপিং
        if (inventory.getBranch() != null) {
            dto.setBranchId(inventory.getBranch().getId());
            dto.setBranchName(inventory.getBranch().getName()); // ধরে নেওয়া হয়েছে Branch এনটিটিতে name ফিল্ড আছে
        }

        // MedicineBatch এবং তার সাথে যুক্ত Medicine অবজেক্ট থেকে ডেটা ম্যাপিং
        if (inventory.getBatch() != null) {
            dto.setBatchId(inventory.getBatch().getId());
            dto.setBatchNumber(inventory.getBatch().getBatchNumber());

            // ব্যাচের ভেতর থাকা মেডিসিন অবজেক্ট থেকে ব্র্যান্ডের নাম নেওয়া হচ্ছে (Object Chain Mapping)
            if (inventory.getBatch().getMedicine() != null) {
                dto.setMedicineBrandName(inventory.getBatch().getMedicine().getBrandName());
            }
        }

        return dto;
    }

    /**
     * বিদ্যমান (Existing) ইনভেন্টরি রেকর্ড নতুন রিকোয়েস্টের ডেটা দিয়ে আপডেট করার পদ্ধতি।
     */
    public void updateEntityFromDto(BranchInventoryRequestDto dto, BranchInventory inventory) {
        if (dto == null || inventory == null) {
            return;
        }

        inventory.setQuantityOnHand(dto.getQuantityOnHand());

        // সাধারণত reserved কোয়ান্টিটি সরাসরি এভাবে আপডেট করা হয় না, এটি অর্ডার বা সেলস প্রসেসের মাধ্যমে বাড়ে/কমে।
        // তাই আপডেটের সময় এটিকে হাত দেওয়া হয়নি।
    }
}
