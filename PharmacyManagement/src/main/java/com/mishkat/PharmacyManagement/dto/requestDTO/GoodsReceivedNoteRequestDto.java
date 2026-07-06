package com.mishkat.PharmacyManagement.dto.requestDTO;

import com.mishkat.PharmacyManagement.enums.ApprovalStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class GoodsReceivedNoteRequestDto {
    @NotBlank(message = "GRN number is required")
    private String grnNumber;

    @NotNull(message = "Purchase Order ID reference is required")
    private Long purchaseOrderId;

    @NotNull(message = "Received date is required")
    private LocalDate receivedDate;

    private Long receivedById;
    private ApprovalStatus approvalStatus;

    @NotEmpty(message = "GRN must contain at least one received item")
    @Valid
    private List<GoodsReceivedNoteItemRequestDto> items;
}
