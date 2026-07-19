package com.mishkat.PharmacyManagement.dto.responseDTO;


import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ChangePasswordResponseDto {
    private boolean success;
    private String message;
    private LocalDateTime changedAt;
}
