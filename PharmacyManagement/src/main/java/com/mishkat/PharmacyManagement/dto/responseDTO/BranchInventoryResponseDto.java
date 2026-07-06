package com.mishkat.PharmacyManagement.dto.responseDTO;

import lombok.Data;

@Data
public class BranchInventoryResponseDto {
    private Long id;
    private Long branchId;
    private String branchName;
    private Long batchId;
    private String batchNumber;
    private String medicineBrandName;
    private Integer quantityOnHand;
    private Integer quantityReserved;
}
