package com.mishkat.PharmacyManagement.dto.requestDTO;

import com.mishkat.PharmacyManagement.enums.InvoiceStatus;
import com.mishkat.PharmacyManagement.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SalesInvoiceRequestDto {
    @NotBlank(message = "Invoice serial layout signature unique string code is required")
    private String invoiceNumber;

    @NotNull(message = "Operating branch node context anchor ID is required")
    private Long branchId;

    private Long customerId;
    private Long prescriptionId;

    @NotNull(message = "Serving staff user handler verification identification ID is required")
    private Long soldById;

    @NotNull(message = "Posting ledger date is required")
    private LocalDateTime invoiceDate;

    @NotNull(message = "Gross summary cumulative item valuation subtotal tracking is required")
    private BigDecimal subTotal;

    private BigDecimal discountAmount;
    private BigDecimal vatAmount;

    @NotNull(message = "Net liability aggregated total cash collection target required")
    private BigDecimal totalAmount;

    @NotNull(message = "Direct initial downpayment tracking volume required")
    private BigDecimal paidAmount;

    @NotNull(message = "Postponed debit tracking computation index required")
    private BigDecimal dueAmount;

    @NotNull(message = "State invoice validation constraint check required")
    private InvoiceStatus status;

    // ── 🟢 [NEW]: Payment Method Field ──
    private PaymentMethod paymentMethod;

    @NotEmpty(message = "Sales order invoice checklist cannot run itemless empty requests")
    @Valid
    private List<SalesInvoiceItemRequestDto> items;

    // ── 🟢 নতুন যুক্ত করা হলো: অনলাইন অর্ডার থেকে ইনভয়েস জেনারেট করার ক্রস-রেফারেন্স আইডি ──
    private Long onlineOrderId;
}
