package com.mishkat.PharmacyManagement.dto.responseDTO;

import com.mishkat.PharmacyManagement.enums.BranchType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BranchResponseDto {
    private Long id;
    private String branchCode;
    private String name;
    private BranchType branchType;
    private AddressResponseDto address;
    private String phone;
    private String email;
    private String licenseNumber;
    private String managerName;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
