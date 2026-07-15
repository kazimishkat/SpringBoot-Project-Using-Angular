package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.StockAdjustmentMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.StockAdjustmentItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.StockAdjustmentRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.StockAdjustmentResponseDto;
import com.mishkat.PharmacyManagement.entity.*;
import com.mishkat.PharmacyManagement.enums.StockMovementType;
import com.mishkat.PharmacyManagement.repository.BranchRepository;
import com.mishkat.PharmacyManagement.repository.MedicineBatchRepository;
import com.mishkat.PharmacyManagement.repository.StockAdjustmentRepository;
import com.mishkat.PharmacyManagement.repository.UserRepository;
import com.mishkat.PharmacyManagement.service.StockAdjustmentService;
import com.mishkat.PharmacyManagement.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockAdjustmentServiceImpl implements StockAdjustmentService {
    private final StockAdjustmentRepository stockAdjustmentRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final MedicineBatchRepository medicineBatchRepository;

    // Injecting Internal Stock Ledger Engine
    private final StockMovementService stockMovementService;

    private final StockAdjustmentMapper mapper = new StockAdjustmentMapper();

    @Override
    @Transactional
    public StockAdjustmentResponseDto createStockAdjustment(StockAdjustmentRequestDto dto) {
        if (stockAdjustmentRepository.findByAdjustmentNumber(dto.getAdjustmentNumber()).isPresent()) {
            throw new RuntimeException("Stock Adjustment already exists with number: " + dto.getAdjustmentNumber());
        }

        StockAdjustment adjustment = mapper.toEntity(dto);

        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + dto.getBranchId()));
        adjustment.setBranch(branch);

        if (dto.getApprovedById() != null) {
            User user = userRepository.findById(dto.getApprovedById())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getApprovedById()));
            adjustment.setApprovedBy(user);
        }

        if (adjustment.getItems() != null && dto.getItems() != null) {
            for (int i = 0; i < adjustment.getItems().size(); i++) {
                StockAdjustmentItem item = adjustment.getItems().get(i);
                StockAdjustmentItemRequestDto itemDto = dto.getItems().get(i);

                MedicineBatch batch = medicineBatchRepository.findById(itemDto.getBatchId())
                        .orElseThrow(() -> new RuntimeException("Medicine Batch not found with id: " + itemDto.getBatchId()));
                item.setBatch(batch);
            }
        }

        StockAdjustment savedAdjustment = stockAdjustmentRepository.save(adjustment);

        // Compute delta evaluation and commit to audit trail logs instantly
        logStockMovementsForAdjustment(savedAdjustment);

        return mapper.toDTO(savedAdjustment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockAdjustmentResponseDto> getAllStockAdjustments() {
        return stockAdjustmentRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StockAdjustmentResponseDto getStockAdjustmentById(Long id) {
        StockAdjustment adjustment = stockAdjustmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock Adjustment not found with id: " + id));
        return mapper.toDTO(adjustment);
    }

    @Override
    @Transactional(readOnly = true)
    public StockAdjustmentResponseDto getStockAdjustmentByNumber(String adjustmentNumber) {
        StockAdjustment adjustment = stockAdjustmentRepository.findByAdjustmentNumber(adjustmentNumber)
                .orElseThrow(() -> new RuntimeException("Stock Adjustment not found with number: " + adjustmentNumber));
        return mapper.toDTO(adjustment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockAdjustmentResponseDto> getStockAdjustmentsByBranchId(Long branchId) {
        return stockAdjustmentRepository.findByBranchId(branchId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StockAdjustmentResponseDto updateStockAdjustment(Long id, StockAdjustmentRequestDto dto) {
        StockAdjustment existingAdjustment = stockAdjustmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock Adjustment not found with id: " + id));

        if (dto.getAdjustmentNumber() != null && !existingAdjustment.getAdjustmentNumber().equals(dto.getAdjustmentNumber())) {
            if (stockAdjustmentRepository.findByAdjustmentNumber(dto.getAdjustmentNumber()).isPresent()) {
                throw new RuntimeException("Adjustment number already exists: " + dto.getAdjustmentNumber());
            }
        }

        mapper.updateEntityFromDto(dto, existingAdjustment);

        if (!existingAdjustment.getBranch().getId().equals(dto.getBranchId())) {
            Branch branch = branchRepository.findById(dto.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
            existingAdjustment.setBranch(branch);
        }

        if (dto.getApprovedById() != null) {
            if (existingAdjustment.getApprovedBy() == null || !existingAdjustment.getApprovedBy().getId().equals(dto.getApprovedById())) {
                User user = userRepository.findById(dto.getApprovedById())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                existingAdjustment.setApprovedBy(user);
            }
        }

        existingAdjustment.getItems().clear();
        if (dto.getItems() != null) {
            for (StockAdjustmentItemRequestDto itemDto : dto.getItems()) {
                StockAdjustmentItem newItem = new StockAdjustmentItem();
                newItem.setQuantityBefore(itemDto.getQuantityBefore());
                newItem.setQuantityAfter(itemDto.getQuantityAfter());
                newItem.setReason(itemDto.getReason());
                newItem.setRemarks(itemDto.getRemarks());

                MedicineBatch batch = medicineBatchRepository.findById(itemDto.getBatchId())
                        .orElseThrow(() -> new RuntimeException("Medicine Batch not found with id: " + itemDto.getBatchId()));

                newItem.setBatch(batch);
                newItem.setStockAdjustment(existingAdjustment);
                existingAdjustment.getItems().add(newItem);
            }
        }

        StockAdjustment savedAdjustment = stockAdjustmentRepository.save(existingAdjustment);

        // Ledger records are generally immutable, but re-triggering calculation updates current ledger states.
        logStockMovementsForAdjustment(savedAdjustment);

        return mapper.toDTO(savedAdjustment);
    }

    @Override
    @Transactional
    public void deleteStockAdjustment(Long id) {
        StockAdjustment adjustment = stockAdjustmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock Adjustment not found with id: " + id));
        stockAdjustmentRepository.delete(adjustment);
    }

    private void logStockMovementsForAdjustment(StockAdjustment adjustment) {
        if (adjustment.getItems() != null) {
            for (StockAdjustmentItem item : adjustment.getItems()) {
                int qBefore = item.getQuantityBefore() != null ? item.getQuantityBefore() : 0;
                int qAfter = item.getQuantityAfter() != null ? item.getQuantityAfter() : 0;
                int diff = qAfter - qBefore;

                if (diff == 0) continue; // No structural volume deviation detected

                StockMovementType detectedType;
                int trackingQuantity = Math.abs(diff);

                // Check delta polarity to classify system audit behavioral updates
                if (diff > 0) {
                    detectedType = StockMovementType.ADJUSTMENT_INCREASE;
                } else {
                    // Check explicitly if specific reasons map out to granular types
                    String reasonStr = item.getReason() != null ? item.getReason().name() : "";
                    if (reasonStr.contains("EXPIRED")) {
                        detectedType = StockMovementType.EXPIRED_WRITE_OFF;
                    } else if (reasonStr.contains("DAMAGED")) {
                        detectedType = StockMovementType.DAMAGED_WRITE_OFF;
                    } else {
                        detectedType = StockMovementType.ADJUSTMENT_DECREASE;
                    }
                }

                stockMovementService.recordMovement(
                        adjustment.getBranch().getId(),
                        item.getBatch().getId(),
                        detectedType,
                        trackingQuantity,
                        "STOCK_ADJUSTMENT",
                        adjustment.getId(),
                        item.getRemarks() != null ? item.getRemarks() : "Inventory audit log balance synchronization."
                );
            }
        }
    }
}
