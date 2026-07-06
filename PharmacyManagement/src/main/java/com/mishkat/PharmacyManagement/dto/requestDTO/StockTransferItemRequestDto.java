package com.mishkat.PharmacyManagement.dto.requestDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockTransferItemRequestDto {
    @NotNull(message = "Batch ID is required")
    private Long batchId;

    @NotNull(message = "Sent quantity is required")
    @Min(value = 1, message = "Sent quantity must be at least 1")
    private Integer sentQuantity;
}
