package com.mishkat.PharmacyManagement.dto.responseDTO;

import com.mishkat.PharmacyManagement.enums.TransferStatus;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class StockTransferResponseDto {
    private Long id;
    private String transferNumber;
    private Long requisitionId;
    private String requisitionNumber;
    private Long fromBranchId;
    private String fromBranchName;
    private Long toBranchId;
    private String toBranchName;
    private LocalDate transferDate;
    private TransferStatus status;
    private String dispatchedBy;
    private List<StockTransferItemResponseDto> items;

    @Data
    public static class StockTransferItemResponseDto {
        private Long id;
        private Long batchId;
        private String batchNumber;
        private String medicineBrandName;
        private Integer sentQuantity;
        private Integer receivedQuantity;
    }
}
