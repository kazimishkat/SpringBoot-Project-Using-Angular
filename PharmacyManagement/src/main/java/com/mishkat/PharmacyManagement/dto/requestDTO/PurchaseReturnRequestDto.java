package com.mishkat.PharmacyManagement.dto.requestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseReturnRequestDto {
    @NotBlank(message = "Return number is required")
    private String returnNumber;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotNull(message = "Branch ID is required")
    private Long branchId;

    @NotNull(message = "Return date is required")
    private LocalDate returnDate;

    @NotEmpty(message = "Return payload must contain items")
    @Valid
    private List<PurchaseReturnItemRequestDto> items;
}
