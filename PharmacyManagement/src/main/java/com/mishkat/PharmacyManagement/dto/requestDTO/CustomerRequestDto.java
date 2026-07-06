package com.mishkat.PharmacyManagement.dto.requestDTO;

import com.mishkat.PharmacyManagement.enums.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerRequestDto {
    @NotBlank(message = "Customer name is required")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    private Gender gender;
    private Integer age;

    @Valid
    private AddressRequestDto address;

    // 🟢 রাইডারের মতো অনলাইন পাসওয়ার্ড রিসিভ করার ফিল্ড
    @NotBlank(message = "Password is required for online registration")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}
