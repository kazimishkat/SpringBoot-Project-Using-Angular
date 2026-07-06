package com.mishkat.PharmacyManagement.dto.requestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OnlineOrderRequestDto {
    @NotNull(message = "Branch ID is required")
    private Long branchId;

    private Long prescriptionId; // ঐচ্ছিক (যদি প্রেসক্রিপশন আপলোড করে)

    @NotNull(message = "Total amount is required")
    private Double totalAmount;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    @NotEmpty(message = "Order must contain at least one medicine item")
    @Valid
    private List<OnlineOrderItemRequestDto> items;
}
