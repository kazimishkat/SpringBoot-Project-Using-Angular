package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.PurchaseReturnItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.PurchaseReturnRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PurchaseReturnResponseDto;
import com.mishkat.PharmacyManagement.entity.PurchaseReturn;
import com.mishkat.PharmacyManagement.entity.PurchaseReturnItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PurchaseReturnMapper {
    // =========================================================================
    // 1. Entity থেকে Response DTO তে রূপান্তর (API Response পাঠানোর জন্য)
    // =========================================================================

    public PurchaseReturnResponseDto toDTO(PurchaseReturn entity) {
        if (entity == null) return null;

        PurchaseReturnResponseDto dto = new PurchaseReturnResponseDto();
        dto.setId(entity.getId());
        dto.setReturnNumber(entity.getReturnNumber());
        dto.setReturnDate(entity.getReturnDate());
        dto.setCreatedBy(entity.getCreatedBy()); // BaseEntity থেকে inherited ফিল্ড

        // [গুরুত্বপূর্ণ]: অলস লোডিং (Lazy Loading) এর কারণে Supplier অবজেক্ট নাল কি না তা চেক করা জরুরি
        if (entity.getSupplier() != null) {
            dto.setSupplierId(entity.getSupplier().getId());
            dto.setSupplierName(entity.getSupplier().getName()); // Supplier এন্টিটিতে getName() মেথড আছে ধরে নেওয়া হয়েছে
        }

        // Branch অবজেক্ট থেকে আইডি এবং নাম DTO তে সেট করা হচ্ছে
        if (entity.getBranch() != null) {
            dto.setBranchId(entity.getBranch().getId());
            dto.setBranchName(entity.getBranch().getName()); // Branch এন্টিটিতে getName() মেথড আছে ধরে নেওয়া হয়েছে
        }

        // [গুরুত্বপূর্ণ]: চাইল্ড লিস্ট (Items) ম্যাপ করা হচ্ছে স্ট্রিম (Stream) ব্যবহার করে
        if (entity.getItems() != null) {
            dto.setItems(entity.getItems().stream()
                    .map(this::toItemResponseDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    /**
     * একক PurchaseReturnItem এন্টিটিকে Response Item DTO তে রূপান্তর করার প্রাইভেট মেথড।
     */
    private PurchaseReturnResponseDto.PurchaseReturnItemResponseDto toItemResponseDto(PurchaseReturnItem item) {
        if (item == null) return null;

        PurchaseReturnResponseDto.PurchaseReturnItemResponseDto itemDto = new PurchaseReturnResponseDto.PurchaseReturnItemResponseDto();
        itemDto.setId(item.getId());
        itemDto.setQuantity(item.getQuantity());
        itemDto.setReason(item.getReason());
        itemDto.setCreditAmount(item.getCreditAmount());

        // [নেস্টেড রিলেশনশিপ]: Batch এবং Medicine অবজেক্টের গভীরতা (Depth) হ্যান্ডেল করা হচ্ছে নাল-চেক সহ
        if (item.getBatch() != null) {
            itemDto.setBatchId(item.getBatch().getId());
            itemDto.setBatchNumber(item.getBatch().getBatchNumber()); // MedicineBatch এ getBatchNumber() আছে ধরে নেওয়া হয়েছে

            // Medicine রিলেশনশিপ থেকে ব্র্যান্ডের নাম নেওয়া হচ্ছে
            if (item.getBatch().getMedicine() != null) {
                itemDto.setMedicineBrandName(item.getBatch().getMedicine().getBrandName());
            }
        }
        return itemDto;
    }


    // =========================================================================
    // 2. Request DTO থেকে Entity তে রূপান্তর (নতুন ডাটা সেভ বা আপডেট করার জন্য)
    // =========================================================================

    public PurchaseReturn toEntity(PurchaseReturnRequestDto dto) {
        if (dto == null) return null;

        PurchaseReturn entity = new PurchaseReturn();
        entity.setReturnNumber(dto.getReturnNumber());
        entity.setReturnDate(dto.getReturnDate());

        // [গুরুত্বপূর্ণ]: চাইল্ড আইটেমগুলোর সাথে প্যারেন্ট এন্টিটির দ্বিমুখী সম্পর্ক (Bi-directional Link) তৈরি করা
        if (dto.getItems() != null) {
            List<PurchaseReturnItem> items = dto.getItems().stream()
                    .map(itemDto -> {
                        PurchaseReturnItem itemEntity = toItemEntity(itemDto);

                        // [সতর্কতা]: CascadeType.ALL কাজ করার জন্য চাইল্ডের ভেতর প্যারেন্ট অবজেক্ট রেফারেন্স অবশ্যই সেট করতে হবে
                        itemEntity.setPurchaseReturn(entity);
                        return itemEntity;
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
            entity.setItems(items);
        }

        // [বিশেষ দ্রষ্টব্য]: Supplier এবং Branch এর মত ফরেন-কি (Foreign Key) অবজেক্টগুলো এখানে সরাসরি ম্যাপ করা হয়নি।
        // এগুলোকে আপনার Service Layer-এ রেপোজিটরি (Repository.findById()) থেকে তুলে এনে এই entity-তে সেট করতে হবে।

        return entity;
    }

    /**
     * একক Request Item DTO কে PurchaseReturnItem এন্টিটিতে রূপান্তর করার প্রাইভেট মেথড।
     */
    private PurchaseReturnItem toItemEntity(PurchaseReturnItemRequestDto itemDto) {
        if (itemDto == null) return null;

        PurchaseReturnItem itemEntity = new PurchaseReturnItem();
        itemEntity.setQuantity(itemDto.getQuantity());
        itemEntity.setReason(itemDto.getReason());
        itemEntity.setCreditAmount(itemDto.getCreditAmount());

        // [বিশেষ দ্রষ্টব্য]: MedicineBatch এন্টিটি অবজেক্টটি Service Layer-এ ব্যাচ আইডি (batchId) দিয়ে ডাটাবেজ থেকে খুঁজে এখানে সেট করতে হবে।

        return itemEntity;
    }
}
