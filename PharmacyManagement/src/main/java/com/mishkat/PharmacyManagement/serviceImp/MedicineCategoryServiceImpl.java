package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.MedicineCategoryMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.MedicineCategoryRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.MedicineCategoryResponseDto;
import com.mishkat.PharmacyManagement.entity.MedicineCategory;
import com.mishkat.PharmacyManagement.repository.MedicineCategoryRepository;
import com.mishkat.PharmacyManagement.service.MedicineCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineCategoryServiceImpl implements MedicineCategoryService {
    private final MedicineCategoryRepository categoryRepository;

    // ম্যাপারের মেথডগুলো স্ট্যাটিক না হওয়ায় ইনস্ট্যান্স তৈরি করা হলো
    private final MedicineCategoryMapper mapper = new MedicineCategoryMapper();

    @Override
    @Transactional
    public MedicineCategoryResponseDto createCategory(MedicineCategoryRequestDto dto) {
        // একই নামের ক্যাটাগরি আগে থেকেই আছে কি না তা চেক করা
        if (categoryRepository.findByNameIgnoreCase(dto.getName()).isPresent()) {
            throw new RuntimeException("Category already exists with name: " + dto.getName());
        }

        MedicineCategory category = mapper.toEntity(dto);
        category.setIsActive(true); // নতুন ক্যাটাগরি ডিফল্টভাবে অ্যাকটিভ থাকবে

        MedicineCategory savedCategory = categoryRepository.save(category);
        return mapper.toDTO(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineCategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineCategoryResponseDto getCategoryById(Long id) {
        MedicineCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return mapper.toDTO(category);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineCategoryResponseDto getCategoryByName(String name) {
        MedicineCategory category = categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Category not found with name: " + name));
        return mapper.toDTO(category);
    }

    @Override
    @Transactional
    public MedicineCategoryResponseDto updateCategory(Long id, MedicineCategoryRequestDto dto) {
        MedicineCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        // যদি ক্যাটাগরির নাম পরিবর্তন করা হয়, তবে নতুন নামটা ডুপ্লিকেট কি না চেক করতে হবে
        if (dto.getName() != null && !dto.getName().equalsIgnoreCase(category.getName())) {
            if (categoryRepository.findByNameIgnoreCase(dto.getName()).isPresent()) {
                throw new RuntimeException("Category already exists with name: " + dto.getName());
            }
        }

        // ম্যাপারের সাহায্যে পুরানো ডেটার ওপর নতুন ডেটা আপডেট করা
        mapper.updateEntityFromDto(dto, category);

        MedicineCategory savedCategory = categoryRepository.save(category);
        return mapper.toDTO(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        MedicineCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        categoryRepository.delete(category);
    }
}
