package com.mishkat.PharmacyManagement.dto.requestDTO;

import com.mishkat.PharmacyManagement.enums.PaymentMethod;
import com.mishkat.PharmacyManagement.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentRequestDto {
    @NotNull(message = "Parent sales invoice transaction binding reference index key ID is required")
    private Long invoiceId;

    @NotNull(message = "Clearing processing datetime is required")
    private LocalDateTime paymentDate;

    @NotNull(message = "Monetary volume velocity size amount is required")
    @Positive(message = "Transacted transfer value size must scale positive")
    private BigDecimal amount;

    @NotNull(message = "Channel processing route standard classification option selection is required")
    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;
}
