package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.RequisitionItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.RequisitionRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.RequisitionResponseDto;
import com.mishkat.PharmacyManagement.entity.Requisition;
import com.mishkat.PharmacyManagement.entity.RequisitionItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RequisitionMapper {
    // =========================================================================
    // 1. Entity থেকে Response DTO তে রূপান্তর (Client-কে ডাটা দেখানোর জন্য)
    // =========================================================================

    public RequisitionResponseDto toDTO(Requisition entity) {
        if (entity == null) return null;

        RequisitionResponseDto dto = new RequisitionResponseDto();
        dto.setId(entity.getId());
        dto.setRequisitionNumber(entity.getRequisitionNumber());
        dto.setRequisitionDate(entity.getRequisitionDate());
        dto.setStatus(entity.getStatus());
        dto.setPriority(entity.getPriority());
        dto.setCreatedAt(entity.getCreatedAt()); // BaseEntity থেকে টাইমস্ট্যাম্প নেওয়া হচ্ছে

        // [গুরুত্বপূর্ণ]: Lazy Loading হ্যান্ডেল করার জন্য এবং NullPointerException এড়াতে নাল-চেক করা হয়েছে
        if (entity.getBranch() != null) {
            dto.setBranchId(entity.getBranch().getId());
            dto.setBranchName(entity.getBranch().getName()); // Branch এন্টিটিতে getName() আছে ধরে নেওয়া হয়েছে
        }

        // রিকুয়েস্ট কে করেছে (User) তার ডাটা ম্যাপ করা হচ্ছে
        if (entity.getRequestedBy() != null) {
            dto.setRequestedById(entity.getRequestedBy().getId());
            dto.setRequestedByName(entity.getRequestedBy().getFullName()); // আপনার User ডিজাইন অনুযায়ী মেথড পরিবর্তন করতে পারেন
        }

        // [লিস্ট ম্যাপিং]: স্ট্রিম (Stream) ব্যবহার করে চাইল্ড আইটেমগুলোর লিস্ট কনভার্ট করা হচ্ছে
        if (entity.getItems() != null) {
            dto.setItems(entity.getItems().stream()
                    .map(this::toItemResponseDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    /**
     * একটি একক RequisitionItem এন্টিটিকে Response Item DTO-তে রূপান্তর করার মেথড।
     */
    private RequisitionResponseDto.RequisitionItemResponseDto toItemResponseDto(RequisitionItem item) {
        if (item == null) return null;

        RequisitionResponseDto.RequisitionItemResponseDto itemDto = new RequisitionResponseDto.RequisitionItemResponseDto();
        itemDto.setId(item.getId());
        itemDto.setRequestedQuantity(item.getRequestedQuantity());
        itemDto.setApprovedQuantity(item.getApprovedQuantity());
        itemDto.setFulfilledQuantity(item.getFulfilledQuantity());

        // [নেস্টেড অবজেক্ট]: মেডিসিন রিলেশনশিপ থেকে আইডি এবং ব্র্যান্ডের নাম নেওয়া হচ্ছে
        if (item.getMedicine() != null) {
            itemDto.setMedicineId(item.getMedicine().getId());
            itemDto.setBrandName(item.getMedicine().getBrandName()); // Medicine এন্টিটিতে getBrandName() আছে ধরে নেওয়া হয়েছে
        }

        return itemDto;
    }


    // =========================================================================
    // 2. Request DTO থেকে Entity তে রূপান্তর (ডাটাবেজে ডাটা সেভ বা আপডেট করার জন্য)
    // =========================================================================

    public Requisition toEntity(RequisitionRequestDto dto) {
        if (dto == null) return null;

        Requisition entity = new Requisition();
        entity.setRequisitionNumber(dto.getRequisitionNumber());
        entity.setRequisitionDate(dto.getRequisitionDate());
        entity.setStatus(dto.getStatus());
        entity.setPriority(dto.getPriority());

        // [নোট]: remarks ফিল্ডটি DTO-তে না থাকলে এখানে ইগনোর করা যেতে পারে বা সার্ভিসে সেট করা যাবে।

        // [খুবই গুরুত্বপূর্ণ]: JPA CascadeType.ALL চাইল্ড আইটেমগুলোকে স্বয়ংক্রিয়ভাবে সেভ করার জন্য,
        // প্রতিটি চাইল্ডের ভেতর প্যারেন্ট এন্টিটির (Requisition) রেফারেন্স সেট করে দিতে হবে (Bi-directional mapping)।
        if (dto.getItems() != null) {
            List<RequisitionItem> items = dto.getItems().stream()
                    .map(itemDto -> {
                        RequisitionItem itemEntity = toItemEntity(itemDto);
                        itemEntity.setRequisition(entity); // চাইল্ডকে বলা হচ্ছে তার প্যারেন্ট কে
                        return itemEntity;
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
            entity.setItems(items);
        }

        // [বিশেষ দ্রষ্টব্য]: branchId এবং requestedById-এর মত রিলেশনাল অবজেক্টগুলো ডাটাবেজ ফরেন-কি ডিপেন্ডেন্সি।
        // এগুলোকে আপনার RequisitionService লেয়ারে রেপোজিটরি (Repository.findById()) থেকে তুলে এনে এই entity-তে সেট করতে হবে।

        return entity;
    }

    /**
     * একটি একক RequisitionItemRequestDto-কে RequisitionItem এন্টিটিতে রূপান্তর করার মেথড।
     */
    private RequisitionItem toItemEntity(RequisitionItemRequestDto itemDto) {
        if (itemDto == null) return null;

        RequisitionItem itemEntity = new RequisitionItem();
        itemEntity.setRequestedQuantity(itemDto.getRequestedQuantity());

        // নতুন রিকুয়েস্ট তৈরি হওয়ার সময় ডিফল্ট মান ০ সেট করা হচ্ছে, যা এন্টিটির ডিফল্ট মানের সাথে মিলে যায়
        itemEntity.setFulfilledQuantity(0);

        // [বিশেষ দ্রষ্টব্য]: medicineId ব্যবহার করে আসল Medicine অবজেক্টটি আপনার Service Layer-এ
        // MedicineRepository থেকে খুঁজে বের করে এই itemEntity-তে সেট করতে হবে।

        return itemEntity;
    }
}
