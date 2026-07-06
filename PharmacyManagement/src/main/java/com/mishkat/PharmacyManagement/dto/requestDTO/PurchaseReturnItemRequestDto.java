package com.mishkat.PharmacyManagement.dto.requestDTO;

import com.mishkat.PharmacyManagement.enums.ReturnReason;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PurchaseReturnItemRequestDto {
    @NotNull(message = "Batch ID is required")
    private Long batchId;

    @NotNull(message = "Return quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private ReturnReason reason;
    private BigDecimal creditAmount;
}
