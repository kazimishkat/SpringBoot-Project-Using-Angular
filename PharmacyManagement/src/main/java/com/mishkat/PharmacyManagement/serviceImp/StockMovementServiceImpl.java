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
    public StockMovementResponseDto createStockMovement(StockMovementRequestDto dto) {
        StockMovement movement = mapper.toEntity(dto);

        // Branch সেট করা
        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + dto.getBranchId()));
        movement.setBranch(branch);

        // MedicineBatch সেট করা
        MedicineBatch batch = medicineBatchRepository.findById(dto.getBatchId())
                .orElseThrow(() -> new RuntimeException("Medicine Batch not found with id: " + dto.getBatchId()));
        movement.setBatch(batch);

        StockMovement savedMovement = stockMovementRepository.save(movement);
        return mapper.toDTO(savedMovement);
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
                .orElseThrow(() -> new RuntimeException("Stock Movement not found with id: " + id));
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
