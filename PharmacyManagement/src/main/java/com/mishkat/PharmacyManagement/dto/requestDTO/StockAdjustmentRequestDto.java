package com.mishkat.PharmacyManagement.dto.requestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class StockAdjustmentRequestDto {
    @NotBlank(message = "Adjustment control receipt number is required")
    private String adjustmentNumber;

    @NotNull(message = "Execution Branch ID is required")
    private Long branchId;

    @NotNull(message = "Audit adjustment transaction timestamp date is required")
    private LocalDate adjustmentDate;

    private Long approvedById;

    @NotEmpty(message = "Adjustment document manifest list must include targets")
    @Valid
    private List<StockAdjustmentItemRequestDto> items;
}
