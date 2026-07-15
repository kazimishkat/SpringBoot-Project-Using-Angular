package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.StockMovementMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.StockMovementRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.StockMovementResponseDto;
import com.mishkat.PharmacyManagement.entity.Branch;
import com.mishkat.PharmacyManagement.entity.MedicineBatch;
import com.mishkat.PharmacyManagement.entity.StockMovement;
import com.mishkat.PharmacyManagement.enums.StockMovementType;
import com.mishkat.PharmacyManagement.repository.BranchRepository;
import com.mishkat.PharmacyManagement.repository.MedicineBatchRepository;
import com.mishkat.PharmacyManagement.repository.StockMovementRepository;
import com.mishkat.PharmacyManagement.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockMovementServiceImpl implements StockMovementService {
    private final StockMovementRepository stockMovementRepository;
    private final BranchRepository branchRepository;
    private final MedicineBatchRepository medicineBatchRepository;

    private final StockMovementMapper mapper = new StockMovementMapper();

    @Override
    @Transactional
    public void recordMovement(Long branchId, Long batchId, StockMovementType movementType,
                               Integer quantity, String referenceType, Long referenceId, String remarks) {

        // ১. লক্ষ্য করা ব্রাঞ্চ ডাটাবেজে আছে কি না তা নিশ্চিত করা
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Stock record linkage failed: Branch missing with ID: " + branchId));

        // ২. মেডিসিন ইনভেন্টরি ব্যাচ ভ্যালিডেশন করা
        MedicineBatch batch = medicineBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Stock record linkage failed: Medicine Batch missing with ID: " + batchId));

        // ৩. ইমিউটেবল (Immutable) অডিট লগ অবজেক্ট তৈরি করা
        StockMovement movement = new StockMovement();
        movement.setBranch(branch);
        movement.setBatch(batch);
        movement.setMovementType(movementType);
        movement.setQuantity(quantity);
        movement.setReferenceType(referenceType);
        movement.setReferenceId(referenceId);
        movement.setMovementDate(LocalDateTime.now());
        movement.setRemarks(remarks);

        // অটোমেটিকভাবে ব্যাকএন্ড লেজারে সেভ হবে
        stockMovementRepository.save(movement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockMovementResponseDto> getAllStockMovements() {
        return stockMovementRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StockMovementResponseDto getStockMovementById(Long id) {
        StockMovement movement = stockMovementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock Ledger Entry not found with ID: " + id));
        return mapper.toDTO(movement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockMovementResponseDto> getMovementsByBranchId(Long branchId) {
        return stockMovementRepository.findByBranchId(branchId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockMovementResponseDto> getMovementsByBatchId(Long batchId) {
        return stockMovementRepository.findByBatchId(batchId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockMovementResponseDto> getMovementsByBranchAndType(Long branchId, StockMovementType movementType) {
        return stockMovementRepository.findByBranchIdAndMovementType(branchId, movementType).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockMovementResponseDto> getMovementsByReference(String referenceType, Long referenceId) {
        return stockMovementRepository.findByReferenceTypeAndReferenceId(referenceType, referenceId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
