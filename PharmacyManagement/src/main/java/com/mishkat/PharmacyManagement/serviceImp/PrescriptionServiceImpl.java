package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.PrescriptionMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.PrescriptionRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PrescriptionResponseDto;
import com.mishkat.PharmacyManagement.entity.Customer;
import com.mishkat.PharmacyManagement.entity.Prescription;
import com.mishkat.PharmacyManagement.repository.CustomerRepository;
import com.mishkat.PharmacyManagement.repository.PrescriptionRepository;
import com.mishkat.PharmacyManagement.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final CustomerRepository customerRepository;

    private final PrescriptionMapper mapper = new PrescriptionMapper();

    @Value("${image.upload.dir}") // 🟢 স্প্রিং বুটের প্রোপার্টি থেকে সঠিক ডিরেক্টরি পাথ নেওয়া হলো
    private String uploadDir;

    @Override
    @Transactional
    public PrescriptionResponseDto createPrescription(PrescriptionRequestDto dto, MultipartFile file) {
        Prescription prescription = mapper.toEntity(dto);

        // 🟢 রাইডারের আর্কিটেকচার অনুসরণ করে ফাইল সিস্টেমে ফাইল রাইট করার মেথড কল
        if (file != null && !file.isEmpty()) {
            prescription.setScannedCopy(uploadFile(file, dto.getDoctorName() != null ? dto.getDoctorName() : "Prescription"));
            prescription.setFileType(file.getContentType());
        }

        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + dto.getCustomerId()));
            prescription.setCustomer(customer);
        }

        Prescription savedPrescription = prescriptionRepository.save(prescription);
        return mapper.toDTO(savedPrescription);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionResponseDto> getAllPrescriptions() {
        return prescriptionRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PrescriptionResponseDto getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + id));
        return mapper.toDTO(prescription);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionResponseDto> getPrescriptionsByCustomerId(Long customerId) {
        return prescriptionRepository.findByCustomerId(customerId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionResponseDto> searchByDoctorName(String doctorName) {
        return prescriptionRepository.findByDoctorNameContainingIgnoreCase(doctorName).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PrescriptionResponseDto updatePrescription(Long id, PrescriptionRequestDto dto, MultipartFile file) {
        Prescription existingPrescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + id));

        if (dto.getDoctorName() != null) existingPrescription.setDoctorName(dto.getDoctorName());
        if (dto.getHospitalOrClinic() != null) existingPrescription.setHospitalOrClinic(dto.getHospitalOrClinic());
        if (dto.getPrescriptionDate() != null) existingPrescription.setPrescriptionDate(dto.getPrescriptionDate());
        if (dto.getRemarks() != null) existingPrescription.setRemarks(dto.getRemarks());

        // আপডেটের সময় নতুন ফাইল আসলে তা রিপ্লেস করবে
        if (file != null && !file.isEmpty()) {
            existingPrescription.setScannedCopy(uploadFile(file, existingPrescription.getDoctorName()));
            existingPrescription.setFileType(file.getContentType());
        }

        if (dto.getCustomerId() != null &&
                (existingPrescription.getCustomer() == null || !existingPrescription.getCustomer().getId().equals(dto.getCustomerId()))) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + dto.getCustomerId()));
            existingPrescription.setCustomer(customer);
        }

        Prescription savedPrescription = prescriptionRepository.save(existingPrescription);
        return mapper.toDTO(savedPrescription);
    }

    @Override
    @Transactional
    public void deletePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + id));
        prescriptionRepository.delete(prescription);
    }

    @Override
    @Transactional(readOnly = true)
    public Prescription getPrescriptionEntityById(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + id));
    }

    // ── রাইডারের কোড স্ট্রাকচার অনুসরণ করে তৈরি করা ফাইল আপলোড মেথড ──
    private String uploadFile(MultipartFile file, String name) {
        try {
            Path path = Paths.get(uploadDir, "prescription"); // প্রেসক্রিপশন ফোল্ডারে ফাইলগুলো আলাদা জমা হবে

            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            String ext = "";
            String original = file.getOriginalFilename();

            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf("."));
            }

            String fileName = name.trim().replaceAll("\\s+", "_")
                    + "_" + UUID.randomUUID()
                    + ext;

            Files.copy(file.getInputStream(), path.resolve(fileName));
            return fileName;

        } catch (Exception e) {
            throw new RuntimeException("Prescription file upload failed");
        }
    }
}
