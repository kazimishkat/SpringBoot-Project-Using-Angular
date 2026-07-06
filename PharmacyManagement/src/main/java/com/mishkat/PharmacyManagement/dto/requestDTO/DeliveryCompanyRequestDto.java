package com.mishkat.PharmacyManagement.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DeliveryCompanyRequestDto {
    @NotBlank(message = "Delivery company name is required")
    @Size(max = 100)
    private String companyName;

    @Size(max = 100)
    private String contactPerson;

    @NotBlank(message = "Contact phone number is required")
    @Size(max = 20)
    private String phoneNumber;

    private String apiKey; // API ইন্টিগ্রেশনের সিক্রেট কি
}
