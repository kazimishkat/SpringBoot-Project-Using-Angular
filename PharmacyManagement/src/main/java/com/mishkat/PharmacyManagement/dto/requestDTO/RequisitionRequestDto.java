package com.mishkat.PharmacyManagement.dto.requestDTO;

import com.mishkat.PharmacyManagement.enums.RequisitionPriority;
import com.mishkat.PharmacyManagement.enums.RequisitionStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class RequisitionRequestDto {
    @NotBlank(message = "Requisition number is required")
    private String requisitionNumber;

    @NotNull(message = "Requesting Branch ID is required")
    private Long branchId;

    @NotNull(message = "Requisition date is required")
    private LocalDate requisitionDate;

    @NotNull(message = "Status is required")
    private RequisitionStatus status;

    private RequisitionPriority priority;
    private Long requestedById;

    @NotEmpty(message = "Requisition must contain items")
    @Valid
    private List<RequisitionItemRequestDto> items;
}
