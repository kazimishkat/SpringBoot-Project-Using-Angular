package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.UserRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto dto);
    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserById(Long id);
    UserResponseDto updateUser(Long id, UserRequestDto dto);
    void deleteUser(Long id);

    // 🟢 নতুন যুক্ত করা হলো: ইউজার সচল বা অচল (Block/Unblock) করার জন্য
    UserResponseDto toggleUserStatus(Long id, Boolean enabled);
}
