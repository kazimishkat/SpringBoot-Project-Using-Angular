package com.mishkat.PharmacyManagement.dto.responseDTO;

import com.mishkat.PharmacyManagement.enums.RequisitionPriority;
import com.mishkat.PharmacyManagement.enums.RequisitionStatus;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequisitionResponseDto {
    private Long id;
    private String requisitionNumber;
    private Long branchId;
    private String branchName;
    private LocalDate requisitionDate;
    private RequisitionStatus status;
    private RequisitionPriority priority;
    private Long requestedById;
    private String requestedByName;
    private LocalDateTime createdAt;
    private Long processedById;
    private String processedByName;
    private List<RequisitionItemResponseDto> items;

    @Data
    public static class RequisitionItemResponseDto {
        private Long id;
        private Long medicineId;
        private String brandName;
        private Integer requestedQuantity;
        private Integer approvedQuantity;
        private Integer fulfilledQuantity;
    }
}
