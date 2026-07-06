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
        dto.setRole(user.getRole());
        dto.setEnabled(user.getEnabled());
        dto.setIsActive(user.getIsActive());
        dto.setCreatedAt(user.getCreatedAt());

        // Foreign Key Object (Branch) থেকে শুধু প্রয়োজনীয় ফিল্ড বের করে আনা হচ্ছে
        if (user.getBranch() != null) {
            dto.setBranchId(user.getBranch().getId());
            dto.setBranchName(user.getBranch().getName());
        }

        return dto;
    }

    // Request DTO → Entity (Branch Service Layer-এ সেট করতে হবে)
    public static User toEntity(UserRequestDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // পাসওয়ার্ড সার্ভিস লেয়ারে এনক্রিপ্ট (BCrypt) করতে হবে
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());
        user.setEnabled(true); // ডিফল্ট ভ্যালু

        // branchId Service Layer-এ ডেটাবেস থেকে খুঁজে User-এ সেট করতে হবে
        return user;
    }

    // Apply request DTO onto existing entity (for update)
    public static void updateEntity(User user, UserRequestDto dto) {
        if (dto == null) return;

        // null চেক করে আপডেট করা হচ্ছে, যাতে পুরনো ভ্যালু মুছে না যায়
        if (dto.getFullName() != null) user.setFullName(dto.getFullName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getRole() != null) user.setRole(dto.getRole());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword());

        // branchId চেঞ্জ হলে Service Layer থেকে আপডেট করতে হবে
    }
}
