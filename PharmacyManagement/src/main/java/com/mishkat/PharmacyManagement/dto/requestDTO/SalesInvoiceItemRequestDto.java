package com.mishkat.PharmacyManagement.dto.requestDTO;

import com.mishkat.PharmacyManagement.enums.DiscountType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class SalesInvoiceItemRequestDto {
    @NotNull(message = "Batch identity binding reference is required")
    private Long batchId;

    @NotNull(message = "Sales request amount count is required")
    @Min(value = 1, message = "Line count purchase items must equal 1 or greater")
    private Integer quantity;

    @NotNull(message = "Current base catalog standard unit price evaluation required")
    private BigDecimal unitPrice;

    private DiscountType discountType;
    private BigDecimal discountValue;
}
