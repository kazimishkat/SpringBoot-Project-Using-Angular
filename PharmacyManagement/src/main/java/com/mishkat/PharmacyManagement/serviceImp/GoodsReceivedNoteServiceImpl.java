package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.GoodsReceivedNoteMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.GoodsReceivedNoteItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.GoodsReceivedNoteRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.GoodsReceivedNoteResponseDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.MedicineBatchResponseDto;
import com.mishkat.PharmacyManagement.entity.*;
import com.mishkat.PharmacyManagement.enums.ApprovalStatus;
import com.mishkat.PharmacyManagement.enums.StockMovementType;
import com.mishkat.PharmacyManagement.repository.GoodsReceivedNoteRepository;
import com.mishkat.PharmacyManagement.repository.MedicineRepository;
import com.mishkat.PharmacyManagement.repository.PurchaseOrderRepository;
import com.mishkat.PharmacyManagement.repository.UserRepository;
import com.mishkat.PharmacyManagement.service.GoodsReceivedNoteService;
import com.mishkat.PharmacyManagement.service.MedicineBatchService;
import com.mishkat.PharmacyManagement.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodsReceivedNoteServiceImpl implements GoodsReceivedNoteService {
    private final GoodsReceivedNoteRepository grnRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final UserRepository userRepository;
    private final MedicineRepository medicineRepository;

    // ১. মেডিসিন ব্যাচ অটো-ক্রিয়েশনের জন্য সার্ভিস ইনজেক্ট করা হলো
    private final MedicineBatchService medicineBatchService;

    // ইন্টারনাল স্টক মুভমেন্ট ট্র্যাকিং লেজার ইঞ্জিন
    private final StockMovementService stockMovementService;

    @Override
    @Transactional
    public GoodsReceivedNoteResponseDto receiveGoods(GoodsReceivedNoteRequestDto dto) {
        if (grnRepository.findByGrnNumber(dto.getGrnNumber()).isPresent()) {
            throw new RuntimeException("GRN Number already exists: " + dto.getGrnNumber());
        }

        GoodsReceivedNote grn = GoodsReceivedNoteMapper.toEntity(dto);

        PurchaseOrder po = purchaseOrderRepository.findById(dto.getPurchaseOrderId())
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id: " + dto.getPurchaseOrderId()));
        grn.setPurchaseOrder(po);

        if (dto.getReceivedById() != null) {
            User user = userRepository.findById(dto.getReceivedById())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getReceivedById()));
            grn.setReceivedBy(user);
        }

        if (grn.getItems() != null && dto.getItems() != null) {
            for (int i = 0; i < grn.getItems().size(); i++) {
                GoodsReceivedNoteItem item = grn.getItems().get(i);
                GoodsReceivedNoteItemRequestDto itemDto = dto.getItems().get(i);

                Medicine medicine = medicineRepository.findById(itemDto.getMedicineId())
                        .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + itemDto.getMedicineId()));
                item.setMedicine(medicine);
            }
        }

        if (grn.getApprovalStatus() == null) {
            grn.setApprovalStatus(ApprovalStatus.PENDING);
        }

        GoodsReceivedNote savedGrn = grnRepository.save(grn);

        // যদি শুরুতেই APPROVED স্ট্যাটাসে GRN রিসিভ হয়, তবে ব্যাচ তৈরি হবে এবং স্টক মুভমেন্ট হবে
        if (savedGrn.getApprovalStatus() == ApprovalStatus.APPROVED) {
            processMedicineBatches(savedGrn);
            logStockMovementsForGRN(savedGrn, false);
        }

        return GoodsReceivedNoteMapper.toDTO(savedGrn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoodsReceivedNoteResponseDto> getAllGrns() {
        return grnRepository.findAll().stream()
                .map(GoodsReceivedNoteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GoodsReceivedNoteResponseDto getGrnById(Long id) {
        GoodsReceivedNote grn = grnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GRN not found with id: " + id));
        return GoodsReceivedNoteMapper.toDTO(grn);
    }

    @Override
    @Transactional(readOnly = true)
    public GoodsReceivedNoteResponseDto getGrnByNumber(String grnNumber) {
        GoodsReceivedNote grn = grnRepository.findByGrnNumber(grnNumber)
                .orElseThrow(() -> new RuntimeException("GRN not found with number: " + grnNumber));
        return GoodsReceivedNoteMapper.toDTO(grn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoodsReceivedNoteResponseDto> getGrnsByStatus(ApprovalStatus status) {
        return grnRepository.findByApprovalStatus(status).stream()
                .map(GoodsReceivedNoteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoodsReceivedNoteResponseDto> getGrnByPurchaseOrder(Long purchaseOrderId) {
        return grnRepository.findByPurchaseOrderId(purchaseOrderId).stream()
                .map(GoodsReceivedNoteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GoodsReceivedNoteResponseDto updateGrn(Long id, GoodsReceivedNoteRequestDto dto) {
        GoodsReceivedNote existingGrn = grnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GRN not found with id: " + id));

        if (existingGrn.getApprovalStatus() == ApprovalStatus.CANCELLED) {
            throw new RuntimeException("Operation blocked: Cancelled GRN cannot be updated.");
        }

        if (!existingGrn.getGrnNumber().equals(dto.getGrnNumber()) &&
                grnRepository.findByGrnNumber(dto.getGrnNumber()).isPresent()) {
            throw new RuntimeException("GRN Number already exists: " + dto.getGrnNumber());
        }

        GoodsReceivedNote updatedData = GoodsReceivedNoteMapper.toEntity(dto);

        existingGrn.setGrnNumber(updatedData.getGrnNumber());
        existingGrn.setReceivedDate(updatedData.getReceivedDate());

        if (dto.getApprovalStatus() != null) {
            existingGrn.setApprovalStatus(dto.getApprovalStatus());
        }

        if (!existingGrn.getPurchaseOrder().getId().equals(dto.getPurchaseOrderId())) {
            PurchaseOrder po = purchaseOrderRepository.findById(dto.getPurchaseOrderId())
                    .orElseThrow(() -> new RuntimeException("PO not found"));
            existingGrn.setPurchaseOrder(po);
        }

        existingGrn.getItems().clear();
        if (updatedData.getItems() != null && dto.getItems() != null) {
            for (int i = 0; i < updatedData.getItems().size(); i++) {
                GoodsReceivedNoteItem newItem = updatedData.getItems().get(i);
                GoodsReceivedNoteItemRequestDto itemDto = dto.getItems().get(i);

                Medicine medicine = medicineRepository.findById(itemDto.getMedicineId())
                        .orElseThrow(() -> new RuntimeException("Medicine not found"));

                newItem.setMedicine(medicine);
                newItem.setGrn(existingGrn);
                existingGrn.getItems().add(newItem);
            }
        }

        GoodsReceivedNote savedGrn = grnRepository.save(existingGrn);
        return GoodsReceivedNoteMapper.toDTO(savedGrn);
    }

    @Override
    @Transactional
    public GoodsReceivedNoteResponseDto updateApprovalStatus(Long id, ApprovalStatus status) {
        GoodsReceivedNote grn = grnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GRN not found with ID: " + id));

        ApprovalStatus oldStatus = grn.getApprovalStatus();

        if (oldStatus == ApprovalStatus.CANCELLED) {
            throw new RuntimeException("Operation blocked: Cannot change status of an already Cancelled GRN.");
        }

        grn.setApprovalStatus(status);
        GoodsReceivedNote savedGrn = grnRepository.save(grn);

        // স্ট্যাটাস ট্রানজিশন লজিক
        if (status == ApprovalStatus.APPROVED && oldStatus != ApprovalStatus.APPROVED) {
            // ১. APPROVED হওয়ার সাথে সাথে অটোমেটিক ব্যাচ তৈরি হবে
            processMedicineBatches(savedGrn);
            // ২. স্টক লেজারে পজিটিভ এন্ট্রি পড়বে
            logStockMovementsForGRN(savedGrn, false);
        } else if (status == ApprovalStatus.CANCELLED && oldStatus == ApprovalStatus.APPROVED) {
            // যদি এপ্রুভড GRN ক্যান্সেল করা হয়, রিভার্স স্টক এন্ট্রি পড়বে (স্টক কমাবে)
            logStockMovementsForGRN(savedGrn, true);
        }

        return GoodsReceivedNoteMapper.toDTO(savedGrn);
    }

    @Override
    @Transactional
    public GoodsReceivedNoteResponseDto cancelGrn(Long id) {
        GoodsReceivedNote grn = grnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cancellation failed: GRN not found with ID: " + id));

        ApprovalStatus oldStatus = grn.getApprovalStatus();

        if (oldStatus == ApprovalStatus.CANCELLED) {
            throw new RuntimeException("This GRN is already cancelled.");
        }

        grn.setApprovalStatus(ApprovalStatus.CANCELLED);
        GoodsReceivedNote savedGrn = grnRepository.save(grn);

        if (oldStatus == ApprovalStatus.APPROVED) {
            logStockMovementsForGRN(savedGrn, true); // রিভার্স স্টক এন্ট্রি পড়বে
        }

        return GoodsReceivedNoteMapper.toDTO(savedGrn);
    }

    @Override
    @Transactional(readOnly = true)
    public GoodsReceivedNoteResponseDto printGrn(Long id) {
        GoodsReceivedNote grn = grnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Print operation failed: GRN not found with ID: " + id));
        return GoodsReceivedNoteMapper.toDTO(grn);
    }

    // ── 🟢 GRN আইটেমগুলো থেকে অটোমেটিকভাবে ব্যাচ তৈরি বা ম্যাপ করার লজিক ──
    private void processMedicineBatches(GoodsReceivedNote grn) {
        if (grn.getItems() != null) {
            for (GoodsReceivedNoteItem item : grn.getItems()) {
                // (GRN Item মডেলে batchNumber, manufactureDate, expiryDate, purchasePrice, sellingPrice ফিল্ডগুলো থাকতে হবে)
                medicineBatchService.createOrUpdateBatch(
                        item.getMedicine().getId(),
                        item.getBatchNumber(),
                        grn.getPurchaseOrder().getSupplier().getId(),
                        item.getManufactureDate(),
                        item.getExpiryDate(),
                        item.getPurchasePrice(),
                        item.getSellingPrice()
                );
            }
        }
    }

    // স্টক মুভমেন্ট ট্র্যাকিং লেজার এন্ট্রি তৈরি করার উন্নত রিভার্সিবল মেথড
    private void logStockMovementsForGRN(GoodsReceivedNote grn, boolean isReverse) {
        if (grn.getItems() != null) {
            for (GoodsReceivedNoteItem item : grn.getItems()) {

                // [গুরুত্বপূর্ণ]: পূর্বে ১L হার্ডকোড করা ছিল। এখন ডাটাবেজে তৈরি হওয়া রিয়েল ব্যাচ আইডি ম্যাপ করা হচ্ছে।
                // এটি করার জন্য ব্যাচ নাম্বার ও মেডিসিন আইডি দিয়ে তৈরি হওয়া ব্যাচের রিয়েল প্রাইমারি আইডি ডাটাবেজ থেকে খুঁজে আনা হচ্ছে।
                List<MedicineBatchResponseDto> detectedBatches = medicineBatchService.getBatchesByNumber(item.getBatchNumber());
                Long batchId = detectedBatches.stream()
                        .filter(b -> b.getMedicineId().equals(item.getMedicine().getId()))
                        .map(com.mishkat.PharmacyManagement.dto.responseDTO.MedicineBatchResponseDto::getId)
                        .findFirst()
                        .orElse(1L); // ফলব্যাক হিসেবে ১L রাখা হলো

                StockMovementType movementType = isReverse ? StockMovementType.PURCHASE_RETURN : StockMovementType.PURCHASE_RECEIVED;
                String auditRemarks = isReverse
                        ? "REVERSE STOCK ENTRY: Cancelled Approved GRN with Number: " + grn.getGrnNumber()
                        : "Inventory stocked inward via GRN: " + grn.getGrnNumber();

                stockMovementService.recordMovement(
                        grn.getPurchaseOrder().getBranch().getId(),
                        batchId,
                        movementType,
                        item.getReceivedQuantity(),
                        "GOODS_RECEIVED_NOTE",
                        grn.getId(),
                        auditRemarks
                );
            }
        }
    }
}
