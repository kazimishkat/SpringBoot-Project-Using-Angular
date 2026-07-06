package com.mishkat.PharmacyManagement.dto.requestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SupplierRequestDto {
    @NotBlank(message = "Supplier code is required")
    @Size(max = 20)
    private String supplierCode;

    @NotBlank(message = "Supplier name is required")
    @Size(max = 150)
    private String name;

    @Size(max = 100)
    private String contactPerson;

    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    @Valid
    private AddressRequestDto address;

    private String tradeLicenseNo;
    private String taxId;
}
