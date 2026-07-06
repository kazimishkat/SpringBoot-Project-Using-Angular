package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.StockAdjustmentMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.StockAdjustmentItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.StockAdjustmentRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.StockAdjustmentResponseDto;
import com.mishkat.PharmacyManagement.entity.*;
import com.mishkat.PharmacyManagement.repository.BranchRepository;
import com.mishkat.PharmacyManagement.repository.MedicineBatchRepository;
import com.mishkat.PharmacyManagement.repository.StockAdjustmentRepository;
import com.mishkat.PharmacyManagement.repository.UserRepository;
import com.mishkat.PharmacyManagement.service.StockAdjustmentService;
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

    // ম্যাপারের মেথডগুলো নন-স্ট্যাটিক হওয়ায় একটি ইনস্ট্যান্স তৈরি করা হলো
    private final StockAdjustmentMapper mapper = new StockAdjustmentMapper();

    @Override
    @Transactional
    public StockAdjustmentResponseDto createStockAdjustment(StockAdjustmentRequestDto dto) {
        // ১. Adjustment Number ইউনিক কি না চেক করা
        if (stockAdjustmentRepository.findByAdjustmentNumber(dto.getAdjustmentNumber()).isPresent()) {
            throw new RuntimeException("Stock Adjustment already exists with number: " + dto.getAdjustmentNumber());
        }

        // ২. DTO থেকে Entity-তে রূপান্তর
        StockAdjustment adjustment = mapper.toEntity(dto);

        // ৩. Branch অবজেক্ট সেট করা
        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + dto.getBranchId()));
        adjustment.setBranch(branch);

        // ৪. Approved By (User) অবজেক্ট সেট করা (যদি থাকে)
        if (dto.getApprovedById() != null) {
            User user = userRepository.findById(dto.getApprovedById())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getApprovedById()));
            adjustment.setApprovedBy(user);
        }

        // ৫. চাইল্ড আইটেমগুলোর জন্য MedicineBatch সেট করা
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

        // Adjustment Number পরিবর্তন হলে ডুপ্লিকেট চেক
        if (dto.getAdjustmentNumber() != null && !existingAdjustment.getAdjustmentNumber().equals(dto.getAdjustmentNumber())) {
            if (stockAdjustmentRepository.findByAdjustmentNumber(dto.getAdjustmentNumber()).isPresent()) {
                throw new RuntimeException("Adjustment number already exists: " + dto.getAdjustmentNumber());
            }
        }

        // সাধারণ ফিল্ড আপডেট
        mapper.updateEntityFromDto(dto, existingAdjustment);

        // Branch আপডেট
        if (!existingAdjustment.getBranch().getId().equals(dto.getBranchId())) {
            Branch branch = branchRepository.findById(dto.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
            existingAdjustment.setBranch(branch);
        }

        // Approved By আপডেট
        if (dto.getApprovedById() != null) {
            if (existingAdjustment.getApprovedBy() == null || !existingAdjustment.getApprovedBy().getId().equals(dto.getApprovedById())) {
                User user = userRepository.findById(dto.getApprovedById())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                existingAdjustment.setApprovedBy(user);
            }
        }

        // ম্যাপারের নির্দেশনা অনুযায়ী Orphan Removal লজিক মেনে আইটেম আপডেট
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
                newItem.setStockAdjustment(existingAdjustment); // Parent-Child লিংক
                existingAdjustment.getItems().add(newItem);
            }
        }

        StockAdjustment savedAdjustment = stockAdjustmentRepository.save(existingAdjustment);
        return mapper.toDTO(savedAdjustment);
    }

    @Override
    @Transactional
    public void deleteStockAdjustment(Long id) {
        StockAdjustment adjustment = stockAdjustmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock Adjustment not found with id: " + id));
        stockAdjustmentRepository.delete(adjustment);
    }
}
