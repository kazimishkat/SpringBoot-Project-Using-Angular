package com.mishkat.PharmacyManagement.dto.responseDTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BranchInventoryResponseDto {
    private Long id;
    private Long branchId;
    private String branchName;
    private Long batchId;
    private String batchNumber;
    private String medicineBrandName;
    private Integer quantityOnHand;
    private Integer quantityReserved;

    // ── UI-তে ডিটেইলস প্যানেল এবং মাল্টিপল ফিল্টারিং গ্রিডের জন্য যুক্ত করা নতুন ফিল্ডস ──
    private String genericName;
    private String categoryName;
    private String supplierName;
    private Integer reorderLevel;
    private LocalDate expiryDate;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private LocalDateTime lastUpdated;
}