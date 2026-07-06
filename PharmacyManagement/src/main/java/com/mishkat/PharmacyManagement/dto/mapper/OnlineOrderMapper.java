package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.OnlineOrderItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.OnlineOrderRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.OnlineOrderItemResponseDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.OnlineOrderResponseDto;
import com.mishkat.PharmacyManagement.entity.OnlineOrder;
import com.mishkat.PharmacyManagement.entity.OnlineOrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OnlineOrderMapper {
    // =========================================================================
    // 1. Entity → Response DTO (কাস্টমার ও ফার্মাসিস্টকে ডাটা দেখানোর জন্য)
    // =========================================================================
    public OnlineOrderResponseDto toDTO(OnlineOrder entity) {
        if (entity == null) return null;

        OnlineOrderResponseDto dto = new OnlineOrderResponseDto();
        dto.setId(entity.getId());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setStatus(entity.getStatus());
        dto.setOrderDate(entity.getOrderDate());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setPaymentStatus(entity.getPaymentStatus());
        dto.setPaymentTransactionId(entity.getPaymentTransactionId());
        dto.setDeliveryAddress(entity.getDeliveryAddress());

        // Customer ও Branch ম্যাপিং (Lazy Loading সেফটি সহ)
        if (entity.getCustomer() != null) {
            dto.setCustomerId(entity.getCustomer().getId());
            dto.setCustomerName(entity.getCustomer().getName());
        }
        if (entity.getBranch() != null) {
            dto.setBranchId(entity.getBranch().getId());
            dto.setBranchName(entity.getBranch().getName());
        }
        if (entity.getPrescription() != null) {
            dto.setPrescriptionId(entity.getPrescription().getId());
        }

        // ── 🟢 আপনার সার্ভিস মেথড চেইনিং লজিক: ডেলিভারি পার্টনার ও ট্র্যাকিং সিঙ্ক ──
        if (entity.getDeliveryAssignment() != null) {
            dto.setTrackingNumber(entity.getDeliveryAssignment().getTrackingNumber());
            dto.setDeliveryStatus(entity.getDeliveryAssignment().getDeliveryStatus());

            if (entity.getDeliveryAssignment().getDeliveryCompany() != null) {
                dto.setDeliveryCompanyName(entity.getDeliveryAssignment().getDeliveryCompany().getCompanyName());
            }
        }

        // চাইল্ড আইটেম লিস্ট ম্যাপিং
        if (entity.getItems() != null) {
            dto.setItems(entity.getItems().stream()
                    .map(this::toItemDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private OnlineOrderItemResponseDto toItemDTO(OnlineOrderItem item) {
        if (item == null) return null;

        OnlineOrderItemResponseDto itemDto = new OnlineOrderItemResponseDto();
        itemDto.setId(item.getId());
        itemDto.setQuantity(item.getQuantity());
        itemDto.setPricePerUnit(item.getPricePerUnit());

        // quantity * pricePerUnit এর মাধ্যমে লাইন টোটাল হিসাব
        itemDto.setLineTotal(item.getQuantity() * item.getPricePerUnit());

        if (item.getMedicine() != null) {
            itemDto.setMedicineId(item.getMedicine().getId());
            itemDto.setMedicineBrandName(item.getMedicine().getBrandName());

            // আপনার Medicine এনটিটিতে থাকা GenericMedicine অবজেক্ট থেকে নাম নেওয়া হচ্ছে
            if (item.getMedicine().getGenericMedicine() != null) {
                itemDto.setMedicineGenericName(item.getMedicine().getGenericMedicine().getGenericName());
            }
        }
        return itemDto;
    }

    // =========================================================================
    // 2. Request DTO → Entity (কাস্টমারের অর্ডার ডাটাবেজে সেভ করার জন্য)
    // =========================================================================
    public OnlineOrder toEntity(OnlineOrderRequestDto dto) {
        if (dto == null) return null;

        OnlineOrder entity = new OnlineOrder();
        entity.setTotalAmount(dto.getTotalAmount());
        entity.setDeliveryAddress(dto.getDeliveryAddress());

        // Bi-directional Cascade লিঙ্কিং যাতে প্যারেন্টের সাথে চাইল্ডও অটো-সেভ হয়
        if (dto.getItems() != null) {
            List<OnlineOrderItem> items = dto.getItems().stream()
                    .map(itemDto -> {
                        OnlineOrderItem itemEntity = toItemEntity(itemDto);
                        itemEntity.setOnlineOrder(entity); // চাইল্ডকে প্যারেন্ট চেনাানো হলো
                        return itemEntity;
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
            entity.setItems(items);
        }

        // Note: branchId, customerId, prescriptionId সার্ভিস লেয়ারে রিপোজিটরি থেকে সেট করতে হবে।
        return entity;
    }

    private OnlineOrderItem toItemEntity(OnlineOrderItemRequestDto itemDto) {
        if (itemDto == null) return null;

        OnlineOrderItem itemEntity = new OnlineOrderItem();
        itemEntity.setQuantity(itemDto.getQuantity());
        itemEntity.setPricePerUnit(itemDto.getPricePerUnit());

        // Note: medicineId সার্ভিস লেয়ারে সেভ করার সময় ট্যাগ করা হবে।
        return itemEntity;
    }
}
