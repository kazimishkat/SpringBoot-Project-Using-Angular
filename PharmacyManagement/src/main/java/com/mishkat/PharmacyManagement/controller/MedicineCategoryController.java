package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.MedicineCategoryRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.MedicineCategoryResponseDto;
import com.mishkat.PharmacyManagement.service.MedicineCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicine-categories")
@RequiredArgsConstructor
public class MedicineCategoryController {
    private final MedicineCategoryService categoryService;

    // POST /api/medicine-categories
    @PostMapping
    public ResponseEntity<MedicineCategoryResponseDto> create(
            @Valid @RequestBody MedicineCategoryRequestDto dto) {
        return new ResponseEntity<>(categoryService.createCategory(dto), HttpStatus.CREATED);
    }

    // GET /api/medicine-categories
    @GetMapping
    public ResponseEntity<List<MedicineCategoryResponseDto>> getAll() {
        List<MedicineCategoryResponseDto> list = categoryService.getAllCategories();
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/medicine-categories/1
    @GetMapping("/{id}")
    public MedicineCategoryResponseDto getById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    // GET /api/medicine-categories/name/Antibiotics
    @GetMapping("/name/{name}")
    public MedicineCategoryResponseDto getByName(@PathVariable String name) {
        return categoryService.getCategoryByName(name);
    }

    // PUT /api/medicine-categories/1
    @PutMapping("/{id}")
    public MedicineCategoryResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody MedicineCategoryRequestDto dto) {
        return categoryService.updateCategory(id, dto);
    }

    // DELETE /api/medicine-categories/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }
}
