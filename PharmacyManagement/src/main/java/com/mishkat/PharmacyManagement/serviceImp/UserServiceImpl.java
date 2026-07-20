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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @Value("${image.upload.dir}")
    private String uploadDir;

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto dto, MultipartFile image) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists: " + dto.getUsername());
        }
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + dto.getEmail());
        }

        User user = UserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEnabled(false);

        if (dto.getBranchId() != null) {
            Branch branch = branchRepository.findById(dto.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
            user.setBranch(branch);
        }

        if (image != null && !image.isEmpty()) {
            user.setImage(uploadImage(image, dto.getUsername()));
        }

        User savedUser = userRepository.save(user);
        authService.sendVerificationEmail(savedUser.getEmail());
        return UserMapper.toDTO(savedUser);
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
    public UserResponseDto updateUser(Long id, UserRequestDto dto, MultipartFile image) {
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

        if (image != null && !image.isEmpty()) {
            user.setImage(uploadImage(image, user.getUsername()));
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

    @Override
    @Transactional
    public ChangePasswordResponseDto changePassword(Long id, ChangePasswordRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Password reset failed: User not found with id: " + id));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            ChangePasswordResponseDto response = new ChangePasswordResponseDto();
            response.setSuccess(false);
            response.setMessage("Current password does not match.");
            response.setChangedAt(LocalDateTime.now());
            return response;
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            ChangePasswordResponseDto response = new ChangePasswordResponseDto();
            response.setSuccess(false);
            response.setMessage("New password and confirmation password do not match.");
            response.setChangedAt(LocalDateTime.now());
            return response;
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        ChangePasswordResponseDto response = new ChangePasswordResponseDto();
        response.setSuccess(true);
        response.setMessage("Password changed successfully.");
        response.setChangedAt(LocalDateTime.now());
        return response;
    }

    private String uploadImage(MultipartFile file, String name) {
        try {
            Path path = Paths.get(uploadDir, "user");
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            String ext = "";
            String original = file.getOriginalFilename();
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf("."));
            }

            String fileName = name.trim().replaceAll("\\s+", "_") + "_" + UUID.randomUUID() + ext;
            Files.copy(file.getInputStream(), path.resolve(fileName));
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("User image upload failed: " + e.getMessage());
        }
    }
}
