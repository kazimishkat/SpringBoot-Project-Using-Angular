package com.mishkat.PharmacyManagement.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GenericMedicineRequestDto {
    @NotBlank(message = "Generic name is required")
    @Size(max = 150)
    private String genericName;

    private Long categoryId;
    private String description;
    private String indication;
    private String sideEffects;
    private String contraindications;
}
