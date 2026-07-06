package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.MedicineMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.MedicineRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.MedicineResponseDto;
import com.mishkat.PharmacyManagement.entity.GenericMedicine;
import com.mishkat.PharmacyManagement.entity.Medicine;
import com.mishkat.PharmacyManagement.repository.GenericMedicineRepository;
import com.mishkat.PharmacyManagement.repository.MedicineRepository;
import com.mishkat.PharmacyManagement.service.MedicineService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepository;
    private final GenericMedicineRepository genericMedicineRepository;

    @Value("${image.upload.dir}")
    private String uploadDir;

    @Override
    @Transactional
    public MedicineResponseDto createMedicine(MedicineRequestDto dto, MultipartFile image) {
        if (medicineRepository.findByMedicineCode(dto.getMedicineCode()).isPresent()) {
            throw new RuntimeException("Medicine code already exists: " + dto.getMedicineCode());
        }

        Medicine medicine = MedicineMapper.toEntity(dto);

        GenericMedicine genericMedicine = genericMedicineRepository.findById(dto.getGenericMedicineId())
                .orElseThrow(() -> new RuntimeException("Generic Medicine not found with id: " + dto.getGenericMedicineId()));
        medicine.setGenericMedicine(genericMedicine);
        medicine.setIsActive(true);

        // Handle Image Upload
        if (image != null && !image.isEmpty()) {
            medicine.setImage(uploadImage(image, dto.getBrandName()));
        }

        Medicine savedMedicine = medicineRepository.save(medicine);
        return MedicineMapper.toDTO(savedMedicine);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineResponseDto> getAllMedicines() {
        return medicineRepository.findAll().stream()
                .map(MedicineMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineResponseDto getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));
        return MedicineMapper.toDTO(medicine);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineResponseDto getMedicineByCode(String medicineCode) {
        Medicine medicine = medicineRepository.findByMedicineCode(medicineCode)
                .orElseThrow(() -> new RuntimeException("Medicine not found with code: " + medicineCode));
        return MedicineMapper.toDTO(medicine);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineResponseDto> searchMedicinesByBrandName(String brandName) {
        return medicineRepository.findByBrandNameContainingIgnoreCase(brandName).stream()
                .map(MedicineMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineResponseDto> getLowStockMedicines() {
        return medicineRepository.findLowStockMedicines().stream()
                .map(MedicineMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MedicineResponseDto updateMedicine(Long id, MedicineRequestDto dto, MultipartFile image) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));

        if (dto.getMedicineCode() != null && !dto.getMedicineCode().equals(medicine.getMedicineCode())) {
            if (medicineRepository.findByMedicineCode(dto.getMedicineCode()).isPresent()) {
                throw new RuntimeException("Medicine code already exists: " + dto.getMedicineCode());
            }
            medicine.setMedicineCode(dto.getMedicineCode());
        }

        if (dto.getGenericMedicineId() != null &&
                (medicine.getGenericMedicine() == null || !medicine.getGenericMedicine().getId().equals(dto.getGenericMedicineId()))) {
            GenericMedicine genericMedicine = genericMedicineRepository.findById(dto.getGenericMedicineId())
                    .orElseThrow(() -> new RuntimeException("Generic Medicine not found with id: " + dto.getGenericMedicineId()));
            medicine.setGenericMedicine(genericMedicine);
        }

        MedicineMapper.updateEntity(medicine, dto);

        // Handle Image Upload Update
        if (image != null && !image.isEmpty()) {
            medicine.setImage(uploadImage(image, medicine.getBrandName()));
        }

        Medicine savedMedicine = medicineRepository.save(medicine);
        return MedicineMapper.toDTO(savedMedicine);
    }

    @Override
    @Transactional
    public void deleteMedicine(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));
        medicineRepository.delete(medicine);
    }

    // ── Helper Method for Image Upload ──────────────────────────────────────────

    private String uploadImage(MultipartFile file, String brandName) {
        try {
            // Created a specific folder for medicines
            Path path = Paths.get(uploadDir, "medicines");

            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            String ext = "";
            String original = file.getOriginalFilename();

            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf("."));
            }

            // Using UUID to prevent duplicate file names, replacing spaces in brand name
            String fileName = brandName.trim().replaceAll("\\s+", "_")
                    + "_" + UUID.randomUUID() + ext;
            Files.copy(file.getInputStream(), path.resolve(fileName));

            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }
}

