package com.mishkat.PharmacyManagement.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeliveryAssignmentRequestDto {
    @NotNull(message = "Online Order ID is required")
    private Long onlineOrderId;

    @NotNull(message = "Delivery Company ID is required")
    private Long deliveryCompanyId;

    @NotBlank(message = "Tracking number from courier is required")
    private String trackingNumber; // কুরিয়ারের স্লিপ বা ড্যাশবোর্ড থেকে জেনারেট হওয়া আইডি

    private String remarks;
}
