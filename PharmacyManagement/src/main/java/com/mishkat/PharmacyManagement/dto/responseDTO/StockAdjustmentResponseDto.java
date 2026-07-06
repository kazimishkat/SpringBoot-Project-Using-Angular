package com.mishkat.PharmacyManagement.dto.responseDTO;

import com.mishkat.PharmacyManagement.enums.AdjustmentReason;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class StockAdjustmentResponseDto {
    private Long id;
    private String adjustmentNumber;
    private Long branchId;
    private String branchName;
    private LocalDate adjustmentDate;
    private String approvedBy;
    private List<StockAdjustmentItemResponseDto> items;

    @Data
    public static class StockAdjustmentItemResponseDto {
        private Long id;
        private Long batchId;
        private String batchNumber;
        private String medicineBrandName;
        private Integer quantityBefore;
        private Integer quantityAfter;
        private AdjustmentReason reason;
        private String remarks;
    }
}
