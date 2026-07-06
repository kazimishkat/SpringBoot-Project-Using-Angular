package com.mishkat.PharmacyManagement.dto.requestDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GoodsReceivedNoteItemRequestDto {
    @NotNull(message = "Medicine ID is required")
    private Long medicineId;

    @NotBlank(message = "Batch number is required")
    private String batchNumber;

    private LocalDate manufactureDate;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

    @NotNull(message = "Received quantity is required")
    @Min(value = 1, message = "Received quantity must be at least 1")
    private Integer receivedQuantity;

    @NotNull(message = "Purchase price is required")
    private BigDecimal purchasePrice;

    @NotNull(message = "Selling price is required")
    private BigDecimal sellingPrice;
}
