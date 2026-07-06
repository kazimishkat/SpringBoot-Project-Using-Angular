package com.mishkat.PharmacyManagement.dto.requestDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequisitionItemRequestDto {
    @NotNull(message = "Medicine ID is required")
    private Long medicineId;

    @NotNull(message = "Requested quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer requestedQuantity;
}
