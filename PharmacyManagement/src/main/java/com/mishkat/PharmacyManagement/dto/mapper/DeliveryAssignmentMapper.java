package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.DeliveryAssignmentRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.DeliveryAssignmentResponseDto;
import com.mishkat.PharmacyManagement.entity.DeliveryAssignment;

public class DeliveryAssignmentMapper {
    public DeliveryAssignmentResponseDto toDTO(DeliveryAssignment entity) {
        if (entity == null) return null;

        DeliveryAssignmentResponseDto dto = new DeliveryAssignmentResponseDto();
        dto.setId(entity.getId());
        dto.setTrackingNumber(entity.getTrackingNumber());
        dto.setAssignedAt(entity.getAssignedAt());
        dto.setDeliveryStatus(entity.getDeliveryStatus());
        dto.setRemarks(entity.getRemarks());

        // প্যারেন্ট অনলাইন অর্ডার লিঙ্কিং
        if (entity.getOnlineOrder() != null) {
            dto.setOnlineOrderId(entity.getOnlineOrder().getId());
            dto.setOnlineOrderNumber(entity.getOnlineOrder().getOrderNumber());
        }

        // ডেলিভারি কোম্পানি লিঙ্কিং
        if (entity.getDeliveryCompany() != null) {
            dto.setDeliveryCompanyId(entity.getDeliveryCompany().getId());
            dto.setDeliveryCompanyName(entity.getDeliveryCompany().getCompanyName());
        }

        return dto;
    }

    public DeliveryAssignment toEntity(DeliveryAssignmentRequestDto dto) {
        if (dto == null) return null;

        DeliveryAssignment entity = new DeliveryAssignment();
        entity.setTrackingNumber(dto.getTrackingNumber());
        entity.setRemarks(dto.getRemarks());

        // Note: onlineOrderId এবং deliveryCompanyId অবজেক্ট দুটি সার্ভিস লেয়ার থেকে রিট্রিভ করে বসাতে হবে।
        return entity;
    }
}
