package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.UserMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.UserRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.UserResponseDto;
import com.mishkat.PharmacyManagement.entity.Branch;
import com.mishkat.PharmacyManagement.entity.User;
import com.mishkat.PharmacyManagement.repository.BranchRepository;
import com.mishkat.PharmacyManagement.repository.UserRepository;
import com.mishkat.PharmacyManagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

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
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // পাসওয়ার্ড এনক্রিপশন

        if (dto.getBranchId() != null) {
            Branch branch = branchRepository.findById(dto.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
            user.setBranch(branch);
        }

        return UserMapper.toDTO(userRepository.save(user));
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

        if (dto.getPassword() != null) {
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

    // 🟢 নতুন মেথড: ইউজারের স্ট্যাটাস পরিবর্তন করা
    @Override
    @Transactional
    public UserResponseDto toggleUserStatus(Long id, Boolean enabled) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(enabled);
        return UserMapper.toDTO(userRepository.save(user));
    }
}
