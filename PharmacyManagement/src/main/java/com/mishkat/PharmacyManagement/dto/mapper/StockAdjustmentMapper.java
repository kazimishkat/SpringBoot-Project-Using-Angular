package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.StockAdjustmentItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.StockAdjustmentRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.StockAdjustmentResponseDto;
import com.mishkat.PharmacyManagement.entity.StockAdjustment;
import com.mishkat.PharmacyManagement.entity.StockAdjustmentItem;

import java.util.List;
import java.util.stream.Collectors;

public class StockAdjustmentMapper {
    /**
     * Request DTO থেকে প্রধান StockAdjustment Entity-তে রূপান্তর।
     * লক্ষ্য করুন: চাইল্ড আইটেমগুলোর (items) রিলেশনশিপ সঠিকভাবে বজায় রাখার জন্য
     * এখানে প্রতিটি Item-এর সাথে Parent Adjustment অবজেক্টকে বাইন্ড (Set) করতে হবে।
     */
    public StockAdjustment toEntity(StockAdjustmentRequestDto dto) {
        if (dto == null) {
            return null;
        }

        StockAdjustment adjustment = new StockAdjustment();
        adjustment.setAdjustmentNumber(dto.getAdjustmentNumber());
        adjustment.setAdjustmentDate(dto.getAdjustmentDate());

        // চাইল্ড আইটেম লিস্ট ম্যাপিং
        if (dto.getItems() != null) {
            List<StockAdjustmentItem> items = dto.getItems().stream()
                    .map(itemDto -> {
                        StockAdjustmentItem item = toItemEntity(itemDto);
                        // JPA Cascade-এর সুবিধা পাওয়ার জন্য চাইল্ডের সাথে প্যারেন্ট অবজেক্টের লিঙ্কিং করা হচ্ছে
                        item.setStockAdjustment(adjustment);
                        return item;
                    })
                    .collect(Collectors.toList());
            adjustment.setItems(items);
        }

        // নোট: branchId এবং approvedById-এর অরিজিনাল অবজেক্টগুলো
        // সার্ভিস লেয়ারে ডেটাবেজ থেকে তুলে এনে সেট করে দিতে হবে।

        return adjustment;
    }

    /**
     * StockAdjustment Entity থেকে Response DTO-তে রূপান্তর।
     */
    public StockAdjustmentResponseDto toDTO(StockAdjustment adjustment) {
        if (adjustment == null) {
            return null;
        }

        StockAdjustmentResponseDto dto = new StockAdjustmentResponseDto();
        dto.setId(adjustment.getId());
        dto.setAdjustmentNumber(adjustment.getAdjustmentNumber());
        dto.setAdjustmentDate(adjustment.getAdjustmentDate());

        // Branch ডাটা ফ্ল্যাট ম্যাপিং
        if (adjustment.getBranch() != null) {
            dto.setBranchId(adjustment.getBranch().getId());
            dto.setBranchName(adjustment.getBranch().getName());
        }

        // অনুমোদনকারী ব্যবহারকারীর নাম ম্যাপিং
        if (adjustment.getApprovedBy() != null) {
            dto.setApprovedBy(adjustment.getApprovedBy().getUsername()); // ধরে নেওয়া হয়েছে User এনটিটিতে username আছে
        }

        // চাইল্ড আইটেমগুলোর লিস্টকে Response DTO লিস্টে রূপান্তর করা হচ্ছে
        if (adjustment.getItems() != null) {
            List<StockAdjustmentResponseDto.StockAdjustmentItemResponseDto> itemDtos = adjustment.getItems().stream()
                    .map(this::toItemResponseDto)
                    .collect(Collectors.toList());
            dto.setItems(itemDtos);
        }

        return dto;
    }

    /**
     * চাইল্ড আইটেম (StockAdjustmentItem) Request DTO থেকে Entity-তে রূপান্তরের অভ্যন্তরীণ মেথড।
     */
    private StockAdjustmentItem toItemEntity(StockAdjustmentItemRequestDto dto) {
        if (dto == null) {
            return null;
        }

        StockAdjustmentItem item = new StockAdjustmentItem();
        item.setQuantityBefore(dto.getQuantityBefore());
        item.setQuantityAfter(dto.getQuantityAfter());
        item.setReason(dto.getReason());
        item.setRemarks(dto.getRemarks());

        // নোট: batchId-এর মূল MedicineBatch অবজেক্টটি সার্ভিস লেয়ারে সেট করতে হবে।

        return item;
    }

    /**
     * চাইল্ড আইটেম Entity থেকে static inner class Response DTO-তে রূপান্তরের অভ্যন্তরীণ মেথড।
     */
    private StockAdjustmentResponseDto.StockAdjustmentItemResponseDto toItemResponseDto(StockAdjustmentItem item) {
        if (item == null) {
            return null;
        }

        StockAdjustmentResponseDto.StockAdjustmentItemResponseDto dto = new StockAdjustmentResponseDto.StockAdjustmentItemResponseDto();
        dto.setId(item.getId());
        dto.setQuantityBefore(item.getQuantityBefore());
        dto.setQuantityAfter(item.getQuantityAfter());
        dto.setReason(item.getReason());
        dto.setRemarks(item.getRemarks());

        // Batch এবং তার ভেতরের Medicine অবজেক্ট চেইন থেকে ডেটা ম্যাপ করা হচ্ছে
        if (item.getBatch() != null) {
            dto.setBatchId(item.getBatch().getId());
            dto.setBatchNumber(item.getBatch().getBatchNumber());

            if (item.getBatch().getMedicine() != null) {
                dto.setMedicineBrandName(item.getBatch().getMedicine().getBrandName());
            }
        }

        return dto;
    }

    /**
     * বিদ্যমান স্টক অ্যাডজাস্টমেন্ট রেকর্ড আপডেটের মেথড।
     * স্টক অ্যাডজাস্টমেন্ট সাধারণত ইনভেন্টরি অডিটের মূল প্রমাণ দলিল, তাই এর আইটেমগুলো সরাসরি এডিট না করে
     * পুরোনো লিস্ট ক্লিয়ার করে নতুন আইটেম অ্যাসাইন করা বা সার্ভিস লেয়ারে পুঙ্খানুপুঙ্খভাবে হ্যান্ডেল করা শ্রেয়।
     */
    public void updateEntityFromDto(StockAdjustmentRequestDto dto, StockAdjustment adjustment) {
        if (dto == null || adjustment == null) {
            return;
        }

        adjustment.setAdjustmentNumber(dto.getAdjustmentNumber());
        adjustment.setAdjustmentDate(dto.getAdjustmentDate());

        // চাইল্ড আইটেমগুলোর আপডেট সার্ভিস লেয়ারে Orphan Removal লজিক মেনে করা সবথেকে নিরাপদ।
    }
}
