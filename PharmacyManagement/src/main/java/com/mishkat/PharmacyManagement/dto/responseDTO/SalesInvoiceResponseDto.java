package com.mishkat.PharmacyManagement.dto.responseDTO;

import com.mishkat.PharmacyManagement.enums.DiscountType;
import com.mishkat.PharmacyManagement.enums.InvoiceStatus;
import com.mishkat.PharmacyManagement.enums.PaymentMethod;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SalesInvoiceResponseDto {
    private Long id;
    private String invoiceNumber;
    private Long branchId;
    private String branchName;
    private Long customerId;
    private String customerName;
    private Long prescriptionId;
    private String soldByName;
    private LocalDateTime invoiceDate;
    private BigDecimal subTotal;
    private BigDecimal discountAmount;
    private BigDecimal vatAmount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal dueAmount;
    private InvoiceStatus status;
    // ── 🟢 [NEW]: Payment Method Field ──
    private PaymentMethod paymentMethod;
    private List<SalesInvoiceItemResponseDto> items;
    // ── 🟢 নতুন যুক্ত করা হলো: ফ্রন্ট-এন্ড কাস্টমার যেন তার ইনভয়েসের সাথে মূল অর্ডার লিংক দেখতে পায় ──
    private Long onlineOrderId;
    private String onlineOrderNumber;

    @Data
    public static class SalesInvoiceItemResponseDto {
        private Long id;
        private Long batchId;
        private String batchNumber;
        private String medicineBrandName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private DiscountType discountType;
        private BigDecimal discountValue;
        private BigDecimal taxAmount;
        private BigDecimal totalAmount;
    }
}
