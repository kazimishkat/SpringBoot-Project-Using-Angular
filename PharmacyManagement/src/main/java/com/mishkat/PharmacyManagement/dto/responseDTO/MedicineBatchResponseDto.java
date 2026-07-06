package com.mishkat.PharmacyManagement.dto.responseDTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MedicineBatchResponseDto {
    private Long id;
    private Long medicineId;
    private String brandName;
    private String batchNumber;
    private Long supplierId;
    private String supplierName;
    private LocalDate manufactureDate;
    private LocalDate expiryDate;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private Boolean isActive;
}
