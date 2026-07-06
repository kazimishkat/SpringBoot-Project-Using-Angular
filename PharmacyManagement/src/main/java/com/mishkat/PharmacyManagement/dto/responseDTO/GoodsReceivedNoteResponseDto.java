package com.mishkat.PharmacyManagement.dto.responseDTO;

import com.mishkat.PharmacyManagement.enums.ApprovalStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GoodsReceivedNoteResponseDto {
    private Long id;
    private String grnNumber;
    private Long purchaseOrderId;
    private String poNumber;
    private LocalDate receivedDate;
    private Long receivedById;
    private String receivedByName;
    private ApprovalStatus approvalStatus;
    private LocalDateTime createdAt;
    private List<GoodsReceivedNoteItemResponseDto> items;

    @Data
    public static class GoodsReceivedNoteItemResponseDto {
        private Long id;
        private Long medicineId;
        private String brandName;
        private String batchNumber;
        private LocalDate manufactureDate;
        private LocalDate expiryDate;
        private Integer receivedQuantity;
        private BigDecimal purchasePrice;
        private BigDecimal sellingPrice;
    }
}
