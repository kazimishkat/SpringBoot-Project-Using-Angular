package com.mishkat.PharmacyManagement.dto.requestDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BranchInventoryRequestDto {
    @NotNull(message = "Branch ID is required")
    private Long branchId;

    @NotNull(message = "Batch ID is required")
    private Long batchId;

    @NotNull(message = "Quantity on hand is required")
    @Min(value = 0, message = "Quantity on hand cannot be negative")
    private Integer quantityOnHand;
}
