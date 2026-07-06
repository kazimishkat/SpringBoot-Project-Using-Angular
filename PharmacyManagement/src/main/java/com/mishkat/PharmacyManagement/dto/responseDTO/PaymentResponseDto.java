package com.mishkat.PharmacyManagement.dto.responseDTO;

import com.mishkat.PharmacyManagement.enums.PaymentMethod;
import com.mishkat.PharmacyManagement.enums.PaymentStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDto {
    private Long id;
    private Long invoiceId;
    private String invoiceNumber;
    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionNumber;
    private LocalDateTime createdAt;
}
