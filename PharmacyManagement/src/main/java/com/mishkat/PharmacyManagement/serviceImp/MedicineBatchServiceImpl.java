package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.MedicineBatchMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.MedicineBatchRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.MedicineBatchResponseDto;
import com.mishkat.PharmacyManagement.entity.Medicine;
import com.mishkat.PharmacyManagement.entity.MedicineBatch;
import com.mishkat.PharmacyManagement.entity.Supplier;
import com.mishkat.PharmacyManagement.repository.MedicineBatchRepository;
import com.mishkat.PharmacyManagement.repository.MedicineRepository;
import com.mishkat.PharmacyManagement.repository.SupplierRepository;
import com.mishkat.PharmacyManagement.service.MedicineBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineBatchServiceImpl implements MedicineBatchService {
    private final MedicineBatchRepository medicineBatchRepository;
    private final MedicineRepository medicineRepository;
    private final SupplierRepository supplierRepository;

    // ম্যাপারের মেথডগুলো স্ট্যাটিক না হওয়ায় ইনস্ট্যান্স তৈরি করা হলো
    private final MedicineBatchMapper mapper = new MedicineBatchMapper();

    @Override
    @Transactional
    public MedicineBatchResponseDto createBatch(MedicineBatchRequestDto dto) {
        MedicineBatch batch = mapper.toEntity(dto);

        // Medicine অবজেক্ট খুঁজে সেট করা
        Medicine medicine = medicineRepository.findById(dto.getMedicineId())
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + dto.getMedicineId()));
        batch.setMedicine(medicine);

        // Supplier অবজেক্ট খুঁজে সেট করা (যদি থাকে)
        if (dto.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + dto.getSupplierId()));
            batch.setSupplier(supplier);
        }

        batch.setIsActive(true); // নতুন ব্যাচ অ্যাকটিভ থাকবে

        MedicineBatch savedBatch = medicineBatchRepository.save(batch);
        return mapper.toDTO(savedBatch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineBatchResponseDto> getAllBatches() {
        return medicineBatchRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineBatchResponseDto getBatchById(Long id) {
        MedicineBatch batch = medicineBatchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine Batch not found with id: " + id));
        return mapper.toDTO(batch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineBatchResponseDto> getBatchesByMedicine(Long medicineId) {
        return medicineBatchRepository.findByMedicineId(medicineId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineBatchResponseDto> getBatchesByNumber(String batchNumber) {
        return medicineBatchRepository.findByBatchNumber(batchNumber).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineBatchResponseDto> getExpiringBatches(LocalDate date) {
        return medicineBatchRepository.findByExpiryDateBeforeAndIsActiveTrue(date).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MedicineBatchResponseDto updateBatch(Long id, MedicineBatchRequestDto dto) {
        MedicineBatch batch = medicineBatchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine Batch not found with id: " + id));

        // সাধারণ ফিল্ডগুলো ম্যাপারের মাধ্যমে আপডেট করা
        mapper.updateEntityFromDto(dto, batch);

        // Medicine আইডি পরিবর্তন হলে আপডেট করা
        if (dto.getMedicineId() != null && !batch.getMedicine().getId().equals(dto.getMedicineId())) {
            Medicine medicine = medicineRepository.findById(dto.getMedicineId())
                    .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + dto.getMedicineId()));
            batch.setMedicine(medicine);
        }

        // Supplier আইডি পরিবর্তন হলে আপডেট করা
        if (dto.getSupplierId() != null) {
            if (batch.getSupplier() == null || !batch.getSupplier().getId().equals(dto.getSupplierId())) {
                Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                        .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + dto.getSupplierId()));
                batch.setSupplier(supplier);
            }
        } else {
            // যদি রিকোয়েস্টে সাপ্লায়ার না থাকে, তবে নাল করে দেওয়া
            batch.setSupplier(null);
        }

        MedicineBatch savedBatch = medicineBatchRepository.save(batch);
        return mapper.toDTO(savedBatch);
    }

    @Override
    @Transactional
    public void deleteBatch(Long id) {
        MedicineBatch batch = medicineBatchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine Batch not found with id: " + id));
        medicineBatchRepository.delete(batch);
    }
}
