package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.ChangePasswordRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.UserRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.ChangePasswordResponseDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.UserResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto dto, MultipartFile image);
    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserById(Long id);
    UserResponseDto updateUser(Long id, UserRequestDto dto, MultipartFile image);
    void deleteUser(Long id);
    UserResponseDto toggleUserStatus(Long id, Boolean enabled);
    ChangePasswordResponseDto changePassword(Long id, ChangePasswordRequestDto dto);
}

