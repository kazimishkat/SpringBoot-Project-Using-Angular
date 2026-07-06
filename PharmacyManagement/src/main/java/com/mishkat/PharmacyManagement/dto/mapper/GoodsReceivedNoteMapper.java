package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.GoodsReceivedNoteItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.GoodsReceivedNoteRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.GoodsReceivedNoteResponseDto;
import com.mishkat.PharmacyManagement.entity.GoodsReceivedNote;
import com.mishkat.PharmacyManagement.entity.GoodsReceivedNoteItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GoodsReceivedNoteMapper {
    // 🟢 Entity → Response DTO
    public static GoodsReceivedNoteResponseDto toDTO(GoodsReceivedNote grn) {
        if (grn == null) return null;

        GoodsReceivedNoteResponseDto dto = new GoodsReceivedNoteResponseDto();
        dto.setId(grn.getId());
        dto.setGrnNumber(grn.getGrnNumber());
        dto.setReceivedDate(grn.getReceivedDate());
        dto.setApprovalStatus(grn.getApprovalStatus());
        dto.setCreatedAt(grn.getCreatedAt()); // BaseEntity থেকে অটো-অডিটেড টাইম

        // PurchaseOrder রিলেশন থেকে আইডি ও পিও নাম্বার ফ্ল্যাট ফিল্ডে বসানো
        if (grn.getPurchaseOrder() != null) {
            dto.setPurchaseOrderId(grn.getPurchaseOrder().getId());
            dto.setPoNumber(grn.getPurchaseOrder().getPoNumber());
        }

        // User (Received By) রিলেশন হ্যান্ডেলিং
        if (grn.getReceivedBy() != null) {
            dto.setReceivedById(grn.getReceivedBy().getId());
            dto.setReceivedByName(grn.getReceivedBy().getFullName());
        }

        // চাইল্ড লিস্ট (Items) কনভার্শন - নিচের হেল্পার মেথড ব্যবহার করে লিস্ট ম্যাপ করা হচ্ছে
        if (grn.getItems() != null) {
            List<GoodsReceivedNoteResponseDto.GoodsReceivedNoteItemResponseDto> itemDTOs = grn.getItems().stream()
                    .map(GoodsReceivedNoteMapper::toItemDTO)
                    .collect(Collectors.toList());
            dto.setItems(itemDTOs);
        }

        return dto;
    }

    // 💡 Helper Method: চাইল্ড Item Entity থেকে Item Response DTO
    private static GoodsReceivedNoteResponseDto.GoodsReceivedNoteItemResponseDto toItemDTO(GoodsReceivedNoteItem item) {
        if (item == null) return null;

        GoodsReceivedNoteResponseDto.GoodsReceivedNoteItemResponseDto itemDto = new GoodsReceivedNoteResponseDto.GoodsReceivedNoteItemResponseDto();
        itemDto.setId(item.getId());
        itemDto.setBatchNumber(item.getBatchNumber());
        itemDto.setManufactureDate(item.getManufactureDate());
        itemDto.setExpiryDate(item.getExpiryDate());
        itemDto.setReceivedQuantity(item.getReceivedQuantity());
        itemDto.setPurchasePrice(item.getPurchasePrice());
        itemDto.setSellingPrice(item.getSellingPrice());

        // Medicine অবজেক্ট থেকে আইডি এবং নাম এক্সট্রাক্ট করা হচ্ছে
        if (item.getMedicine() != null) {
            itemDto.setMedicineId(item.getMedicine().getId());
            itemDto.setBrandName(item.getMedicine().getBrandName());
        }

        return itemDto;
    }

    // 🟢 Request DTO → Entity
    public static GoodsReceivedNote toEntity(GoodsReceivedNoteRequestDto dto) {
        if (dto == null) return null;

        GoodsReceivedNote grn = new GoodsReceivedNote();
        grn.setGrnNumber(dto.getGrnNumber());
        grn.setReceivedDate(dto.getReceivedDate());
        grn.setApprovalStatus(dto.getApprovalStatus());

        // Note: purchaseOrderId এবং receivedById এর মূল অবজেক্টগুলো
        // সার্ভিস লেয়ারে ডেটাবেস থেকে খুঁজে এনে grn.setPurchaseOrder() এবং grn.setReceivedBy() করতে হবে।

        // চাইল্ড লিস্ট আইটেমগুলোকে রিকোয়েস্ট DTO থেকে Entity-তে রূপান্তর করা
        if (dto.getItems() != null) {
            List<GoodsReceivedNoteItem> items = new ArrayList<>();
            for (GoodsReceivedNoteItemRequestDto itemDto : dto.getItems()) {
                GoodsReceivedNoteItem item = new GoodsReceivedNoteItem();
                item.setBatchNumber(itemDto.getBatchNumber());
                item.setManufactureDate(itemDto.getManufactureDate());
                item.setExpiryDate(itemDto.getExpiryDate());
                item.setReceivedQuantity(itemDto.getReceivedQuantity());
                item.setPurchasePrice(itemDto.getPurchasePrice());
                item.setSellingPrice(itemDto.getSellingPrice());

                // ⚠️ CRITICAL: চাইল্ড আইটেমে প্যারেন্ট নোটের ব্যাক-রেফারেন্স দেওয়া অত্যন্ত জরুরি,
                // অন্যথায় JPA চাইল্ড সেভ করার সময় grn_id ডাটাবেজে নাল (null) হয়ে ট্রানজেকশন ফেইল করবে।
                item.setGrn(grn);

                // Note: itemDto.getMedicineId() এর মেডিসিন অবজেক্টটি সার্ভিস লেয়ারে সেট করতে হবে।
                items.add(item);
            }
            grn.setItems(items);
        }

        return grn;
    }
}
