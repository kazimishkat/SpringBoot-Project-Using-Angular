package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.UserRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.UserResponseDto;
import com.mishkat.PharmacyManagement.entity.User;

public class UserMapper {
    // Entity → Response DTO
    public static UserResponseDto toDTO(User user) {
        if (user == null) return null;

        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setImage(user.getImage()); // 🟢 প্রোফাইল ছবি ম্যাপিং
        dto.setRole(user.getRole());
        dto.setEnabled(user.getEnabled());
        dto.setIsActive(user.getIsActive());
        dto.setCreatedAt(user.getCreatedAt());

        if (user.getBranch() != null) {
            dto.setBranchId(user.getBranch().getId());
            dto.setBranchName(user.getBranch().getName());
        }

        return dto;
    }

    public static User toEntity(UserRequestDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());
        user.setEnabled(true);

        return user;
    }

    public static void updateEntity(User user, UserRequestDto dto) {
        if (dto == null) return;

        if (dto.getFullName() != null) user.setFullName(dto.getFullName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getRole() != null) user.setRole(dto.getRole());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) user.setPassword(dto.getPassword());
    }

}
