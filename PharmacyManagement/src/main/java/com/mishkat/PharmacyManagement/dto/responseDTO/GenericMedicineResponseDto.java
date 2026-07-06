package com.mishkat.PharmacyManagement.dto.responseDTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GenericMedicineResponseDto {
    private Long id;
    private String genericName;
    private Long categoryId;
    private String categoryName;
    private String description;
    private String indication;
    private String sideEffects;
    private String contraindications;
    private Boolean isActive;
}
