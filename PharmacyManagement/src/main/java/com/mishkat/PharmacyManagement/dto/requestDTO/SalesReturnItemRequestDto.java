package com.mishkat.PharmacyManagement.dto.requestDTO;

import com.mishkat.PharmacyManagement.enums.ReturnReason;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class SalesReturnItemRequestDto {
    @NotNull(message = "Target trace batch index item pointer signature ID is required")
    private Long batchId;

    @NotNull(message = "Reversal count is required")
    @Min(value = 1, message = "Returned items minimum baseline volume entry starts at 1 unit")
    private Integer quantity;

    private ReturnReason reason;
    private BigDecimal refundAmount;
}
