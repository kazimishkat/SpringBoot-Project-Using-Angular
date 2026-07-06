package com.mishkat.PharmacyManagement.dto.requestDTO;

import com.mishkat.PharmacyManagement.enums.StockMovementType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockMovementRequestDto {
    @NotNull(message = "Branch scope binding ID is required")
    private Long branchId;

    @NotNull(message = "Target asset specific batch record tracker sequence ID is required")
    private Long batchId;

    @NotNull(message = "Movement transaction processing type behavior is required")
    private StockMovementType movementType;

    @NotNull(message = "Absolute volumetric quantum magnitude is required")
    private Integer quantity;

    private String referenceType;
    private Long referenceId;
}
