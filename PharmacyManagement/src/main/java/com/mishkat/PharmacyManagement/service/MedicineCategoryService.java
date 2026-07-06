package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.MedicineCategoryRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.MedicineCategoryResponseDto;

import java.util.List;

public interface MedicineCategoryService {
    MedicineCategoryResponseDto createCategory(MedicineCategoryRequestDto dto);

    List<MedicineCategoryResponseDto> getAllCategories();

    MedicineCategoryResponseDto getCategoryById(Long id);

    MedicineCategoryResponseDto getCategoryByName(String name);

    MedicineCategoryResponseDto updateCategory(Long id, MedicineCategoryRequestDto dto);

    void deleteCategory(Long id);
}
