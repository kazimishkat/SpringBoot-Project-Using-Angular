package com.mishkat.PharmacyManagement.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MedicineCategoryRequestDto {
    @NotBlank(message = "Category name is required")
    @Size(max = 100)
    private String name;

    private String description;
}
