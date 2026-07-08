package com.mishkat.PharmacyManagement.dto.responseDTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SupplierResponseDto {

    private Long id;
    private String supplierCode;
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String tradeLicenseNo;
    private String taxId;
    private Boolean isActive;
    private AddressResponseDto address;

}
