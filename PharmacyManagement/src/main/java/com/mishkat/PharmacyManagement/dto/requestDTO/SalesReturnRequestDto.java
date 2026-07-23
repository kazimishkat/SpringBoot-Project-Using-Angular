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
public class SalesReturnRequestDto {
    @NotBlank(message = "Return transaction reference receipt index number is required")
    private String returnNumber;

    @NotNull(message = "Origin source sale receipt trace parent document invoice ID validation needed")
    private Long invoiceId;

    @NotNull(message = "Reversal tracking transaction calendar booking execution date is required")
    private LocalDate returnDate;

    private ApprovalStatus status;

    private Long processedById;

    @NotEmpty(message = "Reversal credit claims processing must detail at least one physical line unit item object entry")
    @Valid
    private List<SalesReturnItemRequestDto> items;
}
