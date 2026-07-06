package com.mishkat.PharmacyManagement.dto.responseDTO;

import com.mishkat.PharmacyManagement.enums.ReturnReason;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseReturnResponseDto {
    private Long id;
    private String returnNumber;
    private Long supplierId;
    private String supplierName;
    private Long branchId;
    private String branchName;
    private LocalDate returnDate;
    private String createdBy;
    private List<PurchaseReturnItemResponseDto> items;

    @Data
    public static class PurchaseReturnItemResponseDto {
        private Long id;
        private Long batchId;
        private String batchNumber;
        private String medicineBrandName;
        private Integer quantity;
        private ReturnReason reason;
        private BigDecimal creditAmount;
    }
}
