package com.mishkat.PharmacyManagement.dto.requestDTO;

import com.mishkat.PharmacyManagement.enums.TransferStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class StockTransferRequestDto {
    @NotBlank(message = "Transfer transaction number is required")
    private String transferNumber;

    private Long requisitionId; // Optional cross-reference linking

    @NotNull(message = "Source branch is required")
    private Long fromBranchId;

    @NotNull(message = "Destination branch is required")
    private Long toBranchId;

    @NotNull(message = "Transfer shipment date is required")
    private LocalDate transferDate;

    @NotNull(message = "Transfer initial status state is required")
    private TransferStatus status;

    private Long dispatchedById;

    @NotEmpty(message = "Stock transfer must contain inventory units")
    @Valid
    private List<StockTransferItemRequestDto> items;
}
