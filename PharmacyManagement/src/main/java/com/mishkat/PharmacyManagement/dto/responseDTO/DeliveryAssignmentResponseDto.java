package com.mishkat.PharmacyManagement.dto.responseDTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeliveryAssignmentResponseDto {
    private Long id;
    private Long onlineOrderId;
    private String onlineOrderNumber;
    private Long deliveryCompanyId;
    private String deliveryCompanyName;
    private String trackingNumber;
    private LocalDateTime assignedAt;
    private String deliveryStatus;
    private String remarks;
}
