package com.mishkat.PharmacyManagement.dto.responseDTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MedicineCategoryResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
