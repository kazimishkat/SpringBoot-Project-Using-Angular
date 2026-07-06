package com.mishkat.PharmacyManagement.dto.requestDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OnlineOrderItemRequestDto {
    @NotNull(message = "Medicine ID is required")
    private Long medicineId; // 👈 কাস্টমার সরাসরি মেডিসিন সিলেক্ট করবে

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Price per unit is required")
    private Double pricePerUnit;
}
