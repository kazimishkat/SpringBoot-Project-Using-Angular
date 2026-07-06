package com.mishkat.PharmacyManagement.dto.responseDTO;

import lombok.Data;

@Data
public class DeliveryCompanyResponseDto {
    private Long id;
    private String companyName;
    private String contactPerson;
    private String phoneNumber;
    private Boolean isActive;
}
