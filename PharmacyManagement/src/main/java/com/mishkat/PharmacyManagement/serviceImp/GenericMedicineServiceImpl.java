package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.GenericMedicineMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.GenericMedicineRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.GenericMedicineResponseDto;
import com.mishkat.PharmacyManagement.entity.GenericMedicine;
import com.mishkat.PharmacyManagement.entity.MedicineCategory;
import com.mishkat.PharmacyManagement.repository.GenericMedicineRepository;
import com.mishkat.PharmacyManagement.repository.MedicineCategoryRepository;
import com.mishkat.PharmacyManagement.service.GenericMedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenericMedicineServiceImpl implements GenericMedicineService {
    private final GenericMedicineRepository genericMedicineRepository;
    private final MedicineCategoryRepository medicineCategoryRepository;

    @Override
    @Transactional
    public GenericMedicineResponseDto createGenericMedicine(GenericMedicineRequestDto dto) {
        // ডুপ্লিকেট জেনেরিক নাম চেক করা হচ্ছে
        if (genericMedicineRepository.findByGenericNameIgnoreCase(dto.getGenericName()).isPresent()) {
            throw new RuntimeException("Generic medicine already exists with name: " + dto.getGenericName());
        }

        GenericMedicine genericMedicine = GenericMedicineMapper.toEntity(dto);

        // ক্যাটাগরি আইডি থাকলে ডেটাবেস থেকে ক্যাটাগরি অবজেক্ট এনে সেট করা
        if (dto.getCategoryId() != null) {
            MedicineCategory category = medicineCategoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));
            genericMedicine.setCategory(category);
        }

        GenericMedicine savedMedicine = genericMedicineRepository.save(genericMedicine);
        return GenericMedicineMapper.toDTO(savedMedicine);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenericMedicineResponseDto> getAllGenericMedicines() {
        return genericMedicineRepository.findAll().stream()
                .map(GenericMedicineMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GenericMedicineResponseDto getGenericMedicineById(Long id) {
        GenericMedicine genericMedicine = genericMedicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Generic Medicine not found with id: " + id));
        return GenericMedicineMapper.toDTO(genericMedicine);
    }

    @Override
    @Transactional(readOnly = true)
    public GenericMedicineResponseDto getByGenericName(String genericName) {
        GenericMedicine genericMedicine = genericMedicineRepository.findByGenericNameIgnoreCase(genericName)
                .orElseThrow(() -> new RuntimeException("Generic Medicine not found with name: " + genericName));
        return GenericMedicineMapper.toDTO(genericMedicine);
    }

    @Override
    @Transactional
    public GenericMedicineResponseDto updateGenericMedicine(Long id, GenericMedicineRequestDto dto) {
        GenericMedicine genericMedicine = genericMedicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Generic Medicine not found with id: " + id));

        // আপডেট করার সময় নাম অন্য কোনো রেকর্ডের সাথে মিলে যাচ্ছে কি না চেক করা
        if (dto.getGenericName() != null && !dto.getGenericName().equalsIgnoreCase(genericMedicine.getGenericName())) {
            if (genericMedicineRepository.findByGenericNameIgnoreCase(dto.getGenericName()).isPresent()) {
                throw new RuntimeException("Generic medicine already exists with name: " + dto.getGenericName());
            }
        }

        // ম্যাপারের মাধ্যমে সাধারণ ফিল্ডগুলো আপডেট করা
        GenericMedicineMapper.updateEntity(genericMedicine, dto);

        // ক্যাটাগরি আইডি পরিবর্তন হলে নতুন ক্যাটাগরি সেট করা
        if (dto.getCategoryId() != null) {
            // যদি আগে ক্যাটাগরি না থাকে বা নতুন আইডি আগেরটার চেয়ে আলাদা হয়
            if (genericMedicine.getCategory() == null || !genericMedicine.getCategory().getId().equals(dto.getCategoryId())) {
                MedicineCategory category = medicineCategoryRepository.findById(dto.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));
                genericMedicine.setCategory(category);
            }
        }

        GenericMedicine savedMedicine = genericMedicineRepository.save(genericMedicine);
        return GenericMedicineMapper.toDTO(savedMedicine);
    }

    @Override
    @Transactional
    public void deleteGenericMedicine(Long id) {
        GenericMedicine genericMedicine = genericMedicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Generic Medicine not found with id: " + id));
        genericMedicineRepository.delete(genericMedicine);
    }
}
