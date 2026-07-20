package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.ChangePasswordRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.UserRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.ChangePasswordResponseDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.UserResponseDto;
import com.mishkat.PharmacyManagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 🟢 POST /api/users (Multipart support for user profile image)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> create(
            @RequestPart("user") @Valid UserRequestDto dto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return new ResponseEntity<>(userService.createUser(dto, image), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll() {
        List<UserResponseDto> list = userService.getAllUsers();
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // 🟢 PUT /api/users/1 (Multipart update for profile image)
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> update(
            @PathVariable Long id,
            @RequestPart("user") @Valid UserRequestDto dto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(userService.updateUser(id, dto, image));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponseDto> changeStatus(@PathVariable Long id, @RequestParam Boolean enabled) {
        return ResponseEntity.ok(userService.toggleUserStatus(id, enabled));
    }

    @PatchMapping("/{id}/change-password")
    public ResponseEntity<ChangePasswordResponseDto> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequestDto dto) {
        ChangePasswordResponseDto response = userService.changePassword(id, dto);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}
