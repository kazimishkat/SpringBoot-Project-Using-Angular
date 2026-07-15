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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineBatchServiceImpl implements MedicineBatchService {
    private final MedicineBatchRepository medicineBatchRepository;
    private final MedicineRepository medicineRepository;
    private final SupplierRepository supplierRepository;

    private final MedicineBatchMapper mapper = new MedicineBatchMapper();

    @Override
    @Transactional
    public MedicineBatch createOrUpdateBatch(Long medicineId, String batchNumber, Long supplierId,
                                             LocalDate manufactureDate, LocalDate expiryDate,
                                             BigDecimal purchasePrice, BigDecimal sellingPrice) {

        List<MedicineBatch> existingBatches = medicineBatchRepository.findByBatchNumber(batchNumber);
        Optional<MedicineBatch> matchingBatch = existingBatches.stream()
                .filter(b -> b.getMedicine().getId().equals(medicineId))
                .findFirst();

        if (matchingBatch.isPresent()) {
            MedicineBatch existing = matchingBatch.get();
            existing.setPurchasePrice(purchasePrice);
            existing.setSellingPrice(sellingPrice);
            existing.setExpiryDate(expiryDate);
            if (manufactureDate != null) existing.setManufactureDate(manufactureDate);
            return medicineBatchRepository.save(existing);
        }

        MedicineBatch newBatch = new MedicineBatch();

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new RuntimeException("Medicine configuration profile missing with ID: " + medicineId));
        newBatch.setMedicine(medicine);

        newBatch.setBatchNumber(batchNumber);
        newBatch.setManufactureDate(manufactureDate);
        newBatch.setExpiryDate(expiryDate);
        newBatch.setPurchasePrice(purchasePrice);
        newBatch.setSellingPrice(sellingPrice);
        newBatch.setIsActive(true);

        if (supplierId != null) {
            Supplier supplier = supplierRepository.findById(supplierId)
                    .orElseThrow(() -> new RuntimeException("Supplier profile missing with ID: " + supplierId));
            newBatch.setSupplier(supplier);
        }

        return medicineBatchRepository.save(newBatch);
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

        // আপনার ম্যাপারের updateEntityFromDto মেথড দিয়ে আপডেট করা
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
            batch.setSupplier(null);
        }

        MedicineBatch savedBatch = medicineBatchRepository.save(batch);
        return mapper.toDTO(savedBatch);
    }
}
