package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.PurchaseOrderItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.PurchaseOrderRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PurchaseOrderResponseDto;
import com.mishkat.PharmacyManagement.entity.PurchaseOrder;
import com.mishkat.PharmacyManagement.entity.PurchaseOrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class PurchaseOrderMapper {
    /**
     * Request DTO থেকে প্রধান PurchaseOrder Entity-তে রূপান্তর।
     * নতুন PO তৈরি করার সময় 'receivedQuantity' ডিফল্টভাবে ০ সেট হবে (যা Entity ফিল্ডে ডিক্লেয়ার করা আছে)।
     */
    public PurchaseOrder toEntity(PurchaseOrderRequestDto dto) {
        if (dto == null) {
            return null;
        }

        PurchaseOrder order = new PurchaseOrder();
        order.setPoNumber(dto.getPoNumber());
        order.setOrderDate(dto.getOrderDate());
        order.setExpectedDeliveryDate(dto.getExpectedDeliveryDate());
        order.setStatus(dto.getStatus());

        // চাইল্ড আইটেমগুলোর লিস্ট ম্যাপিং এবং প্যারেন্ট বাইন্ডিং
        if (dto.getItems() != null) {
            List<PurchaseOrderItem> items = dto.getItems().stream()
                    .map(itemDto -> {
                        PurchaseOrderItem item = toItemEntity(itemDto);
                        // Bi-directional mapping নিশ্চিত করতে চাইল্ড অবজেক্টে প্যারেন্ট সেট করা হচ্ছে
                        item.setPurchaseOrder(order);
                        return item;
                    })
                    .collect(Collectors.toList());
            order.setItems(items);
        }

        // নোট: supplierId, branchId এবং createdBy-এর মূল অবজেক্টগুলো
        // সার্ভিস লেয়ারে রেপজিটরি থেকে খুঁজে নিয়ে এসে সেট করতে হবে।

        return order;
    }

    /**
     * PurchaseOrder Entity থেকে Response DTO-তে রূপান্তর।
     */
    public PurchaseOrderResponseDto toDTO(PurchaseOrder order) {
        if (order == null) {
            return null;
        }

        PurchaseOrderResponseDto dto = new PurchaseOrderResponseDto();
        dto.setId(order.getId());
        dto.setPoNumber(order.getPoNumber());
        dto.setOrderDate(order.getOrderDate());
        dto.setExpectedDeliveryDate(order.getExpectedDeliveryDate());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());

        // অডিট ট্র্যাকিং: কে এই অর্ডারটি তৈরি করেছে তার নাম ম্যাপ করা হচ্ছে
        if (order.getCreatedByUser() != null) {
            dto.setCreatedBy(order.getCreatedByUser().getUsername()); // ধরে নেওয়া হয়েছে User-এ username আছে
        }

        // Supplier-এর ডেটা ফ্ল্যাট করা হচ্ছে
        if (order.getSupplier() != null) {
            dto.setSupplierId(order.getSupplier().getId());
            dto.setSupplierName(order.getSupplier().getName());
        }

        // Branch-এর ডেটা ফ্ল্যাট করা হচ্ছে
        if (order.getBranch() != null) {
            dto.setBranchId(order.getBranch().getId());
            dto.setBranchName(order.getBranch().getName());
        }

        // চাইল্ড আইটেমগুলোর লিস্ট রূপান্তর
        if (order.getItems() != null) {
            List<PurchaseOrderResponseDto.PurchaseOrderItemResponseDto> itemDtos = order.getItems().stream()
                    .map(this::toItemResponseDto)
                    .collect(Collectors.toList());
            dto.setItems(itemDtos);
        }

        return dto;
    }

    /**
     * চাইল্ড আইটেম (PurchaseOrderItem) Request DTO থেকে Entity-তে রূপান্তরের অভ্যন্তরীণ মেথড।
     */
    private PurchaseOrderItem toItemEntity(PurchaseOrderItemRequestDto dto) {
        if (dto == null) {
            return null;
        }

        PurchaseOrderItem item = new PurchaseOrderItem();
        item.setOrderedQuantity(dto.getOrderedQuantity());
        item.setUnitPrice(dto.getUnitPrice());
        // receivedQuantity নতুন অর্ডারের সময় ডিফল্টভাবে ০ থাকবে, যা এনটিটিতেই ডিফাইন করা আছে।

        // নোট: medicineId-এর মূল Medicine অবজেক্টটি সার্ভিস লেয়ারে সেট করতে হবে।

        return item;
    }

    /**
     * চাইল্ড আইটেম Entity থেকে static inner class Response DTO-তে রূপান্তরের অভ্যন্তরীণ মেথড।
     */
    private PurchaseOrderResponseDto.PurchaseOrderItemResponseDto toItemResponseDto(PurchaseOrderItem item) {
        if (item == null) {
            return null;
        }

        PurchaseOrderResponseDto.PurchaseOrderItemResponseDto dto = new PurchaseOrderResponseDto.PurchaseOrderItemResponseDto();
        dto.setId(item.getId());
        dto.setOrderedQuantity(item.getOrderedQuantity());
        dto.setReceivedQuantity(item.getReceivedQuantity());
        dto.setUnitPrice(item.getUnitPrice());

        // Medicine অবজেক্ট থেকে ডেটা এক্সট্রাক্ট করা হচ্ছে
        if (item.getMedicine() != null) {
            dto.setMedicineId(item.getMedicine().getId());
            dto.setBrandName(item.getMedicine().getBrandName());
            dto.setMedicineCode(item.getMedicine().getMedicineCode()); // ধরে নেওয়া হয়েছে Medicine-এ medicineCode আছে
        }

        return dto;
    }

    /**
     * বিদ্যমান Purchase Order আপডেট করার পদ্ধতি (যেমন: ডেলিভারি ডেট বা স্ট্যাটাস এডিট করা)।
     */
    public void updateEntityFromDto(PurchaseOrderRequestDto dto, PurchaseOrder order) {
        if (dto == null || order == null) {
            return;
        }

        order.setPoNumber(dto.getPoNumber());
        order.setOrderDate(dto.getOrderDate());
        order.setExpectedDeliveryDate(dto.getExpectedDeliveryDate());
        order.setStatus(dto.getStatus());

        // চাইল্ড আইটেমের মডিফিকেশন বা আংশিক মালপত্র রিসিভ করার (Goods Receive) লজিকগুলো
        // সার্ভিস লেয়ারে ট্রানজেকশনাল উপায়ে হ্যান্ডেল করা নিরাপদ।
    }
}
