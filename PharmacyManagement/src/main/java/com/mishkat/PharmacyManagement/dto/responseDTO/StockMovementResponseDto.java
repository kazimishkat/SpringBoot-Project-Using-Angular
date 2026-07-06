package com.mishkat.PharmacyManagement.dto.responseDTO;

import com.mishkat.PharmacyManagement.enums.StockMovementType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StockMovementResponseDto {
    private Long id;
    private Long branchId;
    private String branchName;
    private Long batchId;
    private String batchNumber;
    private String medicineName;
    private StockMovementType movementType;
    private Integer quantity;
    private String referenceType;
    private Long referenceId;
    private LocalDateTime createdAt;
    private String createdBy;
}
