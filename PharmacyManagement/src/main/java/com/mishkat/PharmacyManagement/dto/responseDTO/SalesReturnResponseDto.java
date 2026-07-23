package com.mishkat.PharmacyManagement.dto.responseDTO;

import com.mishkat.PharmacyManagement.enums.ApprovalStatus;
import com.mishkat.PharmacyManagement.enums.ReturnReason;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class SalesReturnResponseDto {
    private Long id;
    private String returnNumber;
    private Long invoiceId;
    private String invoiceNumber;
    private LocalDate returnDate;
    private ApprovalStatus status;
    private Long processedById;
    private String processedByName;
    private List<SalesReturnItemResponseDto> items;

    @Data
    public static class SalesReturnItemResponseDto {
        private Long id;
        private Long batchId;
        private String batchNumber;
        private String medicineBrandName;
        private Integer quantity;
        private ReturnReason reason;
        private BigDecimal refundAmount;
    }
}
