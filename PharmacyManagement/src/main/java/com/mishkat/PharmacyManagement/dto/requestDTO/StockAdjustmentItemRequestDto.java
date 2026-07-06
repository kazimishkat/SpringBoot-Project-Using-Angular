package com.mishkat.PharmacyManagement.dto.requestDTO;

import com.mishkat.PharmacyManagement.enums.AdjustmentReason;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockAdjustmentItemRequestDto {
    @NotNull(message = "Batch ID is required")
    private Long batchId;

    @NotNull(message = "Snapshot quantity before change is required")
    @Min(0)
    private Integer quantityBefore;

    @NotNull(message = "New reconciled count target quantity after is required")
    @Min(0)
    private Integer quantityAfter;

    @NotNull(message = "Reason code category classification is required")
    private AdjustmentReason reason;

    private String remarks;
}
