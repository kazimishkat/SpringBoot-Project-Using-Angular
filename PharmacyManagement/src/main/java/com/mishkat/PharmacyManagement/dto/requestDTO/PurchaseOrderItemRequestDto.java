package com.mishkat.PharmacyManagement.dto.requestDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PurchaseOrderItemRequestDto {
    @NotNull(message = "Medicine ID is required")
    private Long medicineId;

    @NotNull(message = "Ordered quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer orderedQuantity;

    @NotNull(message = "Unit price is required")
    private BigDecimal unitPrice;
}
