package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.authentication.AuthService;
import com.mishkat.PharmacyManagement.dto.mapper.UserMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.ChangePasswordRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.UserRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.ChangePasswordResponseDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.UserResponseDto;
import com.mishkat.PharmacyManagement.entity.Branch;
import com.mishkat.PharmacyManagement.entity.User;
import com.mishkat.PharmacyManagement.repository.BranchRepository;
import com.mishkat.PharmacyManagement.repository.UserRepository;
import com.mishkat.PharmacyManagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists: " + dto.getUsername());
        }
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + dto.getEmail());
        }

        User user = UserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        if (dto.getBranchId() != null) {
            Branch branch = branchRepository.findById(dto.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
            user.setBranch(branch);
        }
        User saved = userRepository.save(user);
        authService.sendVerificationEmail(user.getEmail());
        return UserMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return UserMapper.toDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        UserMapper.updateEntity(user, dto);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getBranchId() != null) {
            Branch branch = branchRepository.findById(dto.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
            user.setBranch(branch);
        }

        return UserMapper.toDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public UserResponseDto toggleUserStatus(Long id, Boolean enabled) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(enabled);
        return UserMapper.toDTO(userRepository.save(user));
    }

    // ── 🟢 নতুন পাসওয়ার্ড পরিবর্তনের বিজনেস লজিক ──
    @Override
    @Transactional
    public ChangePasswordResponseDto changePassword(Long id, ChangePasswordRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Password reset failed: User not found with id: " + id));

        // ১. কারেন্ট পাসওয়ার্ড ডাটাবেজের এনকোডেড পাসওয়ার্ডের সাথে মিলছে কি না চেক করা
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            ChangePasswordResponseDto response = new ChangePasswordResponseDto();
            response.setSuccess(false);
            response.setMessage("Current password does not match.");
            response.setChangedAt(LocalDateTime.now());
            return response;
        }

        // ২. নতুন পাসওয়ার্ড এবং কনফার্ম পাসওয়ার্ড মিলছে কি না চেক করা
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            ChangePasswordResponseDto response = new ChangePasswordResponseDto();
            response.setSuccess(false);
            response.setMessage("New password and confirmation password do not match.");
            response.setChangedAt(LocalDateTime.now());
            return response;
        }

        // ৩. নতুন পাসওয়ার্ড সিকিউরলি এনকোড করে ডাটাবেজে আপডেট করা
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        ChangePasswordResponseDto response = new ChangePasswordResponseDto();
        response.setSuccess(true);
        response.setMessage("Password changed successfully.");
        response.setChangedAt(LocalDateTime.now());
        return response;
    }
}
