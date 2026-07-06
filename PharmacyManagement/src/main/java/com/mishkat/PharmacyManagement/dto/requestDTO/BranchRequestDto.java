package com.mishkat.PharmacyManagement.dto.requestDTO;

import com.mishkat.PharmacyManagement.enums.BranchType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BranchRequestDto {
    @NotBlank(message = "Branch code is required")
    @Size(max = 20)
    private String branchCode;

    @NotBlank(message = "Branch name is required")
    @Size(max = 150)
    private String name;

    @NotNull(message = "Branch type is required")
    private BranchType branchType;

    @Valid
    @NotNull(message = "Address is required")
    private AddressRequestDto address;

    @Size(max = 20)
    private String phone;

    @Email(message = "Invalid email format")
    @Size(max = 100)
    private String email;

    @Size(max = 100)
    private String licenseNumber;

    @Size(max = 100)
    private String managerName;
}
