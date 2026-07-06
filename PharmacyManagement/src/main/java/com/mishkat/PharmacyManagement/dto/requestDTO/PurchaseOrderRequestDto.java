package com.mishkat.PharmacyManagement.dto.requestDTO;

import com.mishkat.PharmacyManagement.enums.PurchaseOrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseOrderRequestDto {
    @NotBlank(message = "PO number is required")
    private String poNumber;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotNull(message = "Branch ID is required")
    private Long branchId;

    @NotNull(message = "Order date is required")
    private LocalDate orderDate;

    private LocalDate expectedDeliveryDate;

    @NotNull(message = "Status is required")
    private PurchaseOrderStatus status;

    @NotEmpty(message = "Purchase order must contain at least one item")
    @Valid
    private List<PurchaseOrderItemRequestDto> items;
}
