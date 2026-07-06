package com.mishkat.PharmacyManagement.dto.responseDTO;

import com.mishkat.PharmacyManagement.enums.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private UserRole role;
    private Long branchId;
    private String branchName;
    private Boolean enabled;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
