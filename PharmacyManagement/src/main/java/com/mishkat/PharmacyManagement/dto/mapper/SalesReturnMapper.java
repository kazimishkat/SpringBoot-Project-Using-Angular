package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.SalesReturnItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.SalesReturnRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.SalesReturnResponseDto;
import com.mishkat.PharmacyManagement.entity.SalesReturn;
import com.mishkat.PharmacyManagement.entity.SalesReturnItem;
import com.mishkat.PharmacyManagement.enums.ApprovalStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SalesReturnMapper {
    // =========================================================================
    // 1. Entity থেকে Response DTO তে রূপান্তর (Client-কে ডাটা দেখানোর জন্য)
    // =========================================================================

    public SalesReturnResponseDto toDTO(SalesReturn entity) {
        if (entity == null) return null;

        SalesReturnResponseDto dto = new SalesReturnResponseDto();
        dto.setId(entity.getId());
        dto.setReturnNumber(entity.getReturnNumber());
        dto.setReturnDate(entity.getReturnDate());

        // 🌟 [NEW]: Entity থেকে Status DTO-তে সেটিং
        dto.setStatus(entity.getStatus());

        // Lazy Loading এর কারণে NullPointerException এড়াতে নাল-চেক করা হয়েছে
        if (entity.getInvoice() != null) {
            dto.setInvoiceId(entity.getInvoice().getId());
            dto.setInvoiceNumber(entity.getInvoice().getInvoiceNumber());
        }

        // যে ইউজার রিটার্ন প্রসেস করেছে তার তথ্য ম্যাপ করা হচ্ছে
        if (entity.getProcessedBy() != null) {
            dto.setProcessedById(entity.getProcessedBy().getId());
            dto.setProcessedByName(entity.getProcessedBy().getFullName());
        }

        // [লিস্ট ম্যাপিং]: রিটার্ন আইটেমগুলো ম্যাপ করা হচ্ছে
        if (entity.getItems() != null) {
            dto.setItems(entity.getItems().stream()
                    .map(this::toItemResponseDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    /**
     * একটি একক SalesReturnItem এন্টিটিকে Response Item DTO-তে রূপান্তর করার মেথড।
     */
    private SalesReturnResponseDto.SalesReturnItemResponseDto toItemResponseDto(SalesReturnItem item) {
        if (item == null) return null;

        SalesReturnResponseDto.SalesReturnItemResponseDto itemDto = new SalesReturnResponseDto.SalesReturnItemResponseDto();
        itemDto.setId(item.getId());
        itemDto.setQuantity(item.getQuantity());
        itemDto.setReason(item.getReason());
        itemDto.setRefundAmount(item.getRefundAmount());

        // [নেস্টেড অবজেক্ট]: MedicineBatch এবং তার ভেতরের Medicine থেকে ডাটা নেওয়া হচ্ছে
        if (item.getBatch() != null) {
            itemDto.setBatchId(item.getBatch().getId());
            itemDto.setBatchNumber(item.getBatch().getBatchNumber());

            // Medicine রিলেশন থেকে ব্র্যান্ডের নাম নেওয়া হচ্ছে
            if (item.getBatch().getMedicine() != null) {
                itemDto.setMedicineBrandName(item.getBatch().getMedicine().getBrandName());
            }
        }

        return itemDto;
    }


    // =========================================================================
    // 2. Request DTO থেকে Entity তে রূপান্তর (ডাটাবেজে ডাটা সেভ বা আপডেট করার জন্য)
    // =========================================================================

    public SalesReturn toEntity(SalesReturnRequestDto dto) {
        if (dto == null) return null;

        SalesReturn entity = new SalesReturn();
        entity.setReturnNumber(dto.getReturnNumber());
        entity.setReturnDate(dto.getReturnDate());

        // 🌟 [NEW]: DTO থেকে Status ম্যাপ করা (নাল পাঠালে ডিফল্ট PENDING হবে)
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : ApprovalStatus.PENDING);

        // JPA CascadeType.ALL কাজ করার জন্য প্যারেন্ট ও চাইল্ডের দ্বিমুখী সম্পর্ক (Bi-directional Link) তৈরি করা
        if (dto.getItems() != null) {
            List<SalesReturnItem> items = dto.getItems().stream()
                    .map(itemDto -> {
                        SalesReturnItem itemEntity = toItemEntity(itemDto);

                        // চাইল্ডকে বলা হচ্ছে তার প্যারেন্ট কে (SalesReturn)
                        itemEntity.setSalesReturn(entity);
                        return itemEntity;
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
            entity.setItems(items);
        }

        return entity;
    }

    /**
     * একটি একক SalesReturnItemRequestDto-কে SalesReturnItem এন্টিটিতে রূপান্তর করার মেথড।
     */
    private SalesReturnItem toItemEntity(SalesReturnItemRequestDto itemDto) {
        if (itemDto == null) return null;

        SalesReturnItem itemEntity = new SalesReturnItem();
        itemEntity.setQuantity(itemDto.getQuantity());
        itemEntity.setReason(itemDto.getReason());
        itemEntity.setRefundAmount(itemDto.getRefundAmount());

        return itemEntity;
    }
}
