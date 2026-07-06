package com.mishkat.PharmacyManagement.dto.responseDTO;

import com.mishkat.PharmacyManagement.enums.PurchaseOrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseOrderResponseDto {
    private Long id;
    private String poNumber;
    private Long supplierId;
    private String supplierName;
    private Long branchId;
    private String branchName;
    private LocalDate orderDate;
    private LocalDate expectedDeliveryDate;
    private PurchaseOrderStatus status;
    private String createdBy;
    private LocalDateTime createdAt;
    private List<PurchaseOrderItemResponseDto> items;

    @Data
    public static class PurchaseOrderItemResponseDto {
        private Long id;
        private Long medicineId;
        private String medicineCode;
        private String brandName;
        private Integer orderedQuantity;
        private Integer receivedQuantity;
        private BigDecimal unitPrice;
    }
}
