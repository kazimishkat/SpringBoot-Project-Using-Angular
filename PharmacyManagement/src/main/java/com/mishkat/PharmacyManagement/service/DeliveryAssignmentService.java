package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.DeliveryAssignmentRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.DeliveryAssignmentResponseDto;

public interface DeliveryAssignmentService {

    DeliveryAssignmentResponseDto assignOrderToCourier(DeliveryAssignmentRequestDto dto);


    void updateLiveDeliveryStatus(String trackingNumber, String statusFromCourier);


    DeliveryAssignmentResponseDto getAssignmentByOrderId(Long onlineOrderId);
}
