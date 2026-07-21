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
import com.mishkat.PharmacyManagement.service.BranchInventoryService;
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

    // ১. মেডিসিন ব্যাচ অটো-ক্রিয়েশনের জন্য সার্ভিস ইনজেক্ট
    private final MedicineBatchService medicineBatchService;

    // ২. [NEW] ব্র্যাঞ্চ ইনভেন্টরি স্টকে ডেটা অটো-লোড করার জন্য সার্ভিস ইনজেক্ট
    private final BranchInventoryService branchInventoryService;

    // ৩. ইন্টারনাল স্টক মুভমেন্ট ট্র্যাকিং লেজার ইঞ্জিন
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

        // যদি শুরুতেই APPROVED স্ট্যাটাসে GRN রিসিভ হয়, তবে ব্যাচ তৈরি, ইনভেন্টরি আপডেট এবং স্টক মুভমেন্ট হবে
        if (savedGrn.getApprovalStatus() == ApprovalStatus.APPROVED) {
            processInventoryAndStockMovement(savedGrn, false);
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

        ApprovalStatus oldStatus = existingGrn.getApprovalStatus();

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

        if (savedGrn.getApprovalStatus() == ApprovalStatus.APPROVED && oldStatus != ApprovalStatus.APPROVED) {
            processInventoryAndStockMovement(savedGrn, false);
        } else if (savedGrn.getApprovalStatus() == ApprovalStatus.CANCELLED && oldStatus == ApprovalStatus.APPROVED) {
            processInventoryAndStockMovement(savedGrn, true);
        }

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
            // ১. APPROVED হওয়ার সাথে সাথে ব্যাচ, ইনভেন্টরি লোড এবং স্টক লেজার আপডেট হবে
            processInventoryAndStockMovement(savedGrn, false);
        } else if (status == ApprovalStatus.CANCELLED && oldStatus == ApprovalStatus.APPROVED) {
            // যদি এপ্রুভড GRN ক্যান্সেল করা হয়, রিভার্স ইনভেন্টরি ও স্টক এন্ট্রি পড়বে (স্টক কমাবে)
            processInventoryAndStockMovement(savedGrn, true);
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
            processInventoryAndStockMovement(savedGrn, true); // রিভার্স ইনভেন্টরি ও স্টক আপডেট
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

    // ── 🟢 ইনভেন্টরি আপডেট, ব্যাচ প্রসেস এবং স্টক লেজার ট্র্যাকিং একত্রিত মেথড ──
    private void processInventoryAndStockMovement(GoodsReceivedNote grn, boolean isReverse) {
        if (grn.getItems() == null) return;

        PurchaseOrder po = grn.getPurchaseOrder();
        Long branchId = po != null && po.getBranch() != null ? po.getBranch().getId() : null;

        if (branchId == null) {
            throw new RuntimeException("Cannot process inventory: Branch information missing in Purchase Order.");
        }

        for (GoodsReceivedNoteItem item : grn.getItems()) {

            MedicineBatch createdBatch = null;
            // ১. ব্যাচ তৈরি/আপডেট করা (রিভার্স না হলে)
            if (!isReverse) {
                createdBatch = medicineBatchService.createOrUpdateBatch(
                        item.getMedicine().getId(),
                        item.getBatchNumber(),
                        po.getSupplier() != null ? po.getSupplier().getId() : null,
                        item.getManufactureDate(),
                        item.getExpiryDate(),
                        item.getPurchasePrice(),
                        item.getSellingPrice()
                );
            }

            // ২. তৈরি হওয়া/বিদ্যমান ব্যাচের আইডি নিশ্চিত করা
            Long batchId;
            if (createdBatch != null && createdBatch.getId() != null) {
                batchId = createdBatch.getId();
            } else {
                List<MedicineBatchResponseDto> detectedBatches = medicineBatchService.getBatchesByNumber(item.getBatchNumber());
                batchId = detectedBatches.stream()
                        .filter(b -> b.getMedicineId().equals(item.getMedicine().getId()))
                        .map(MedicineBatchResponseDto::getId)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Batch record not found for medicine batch number: " + item.getBatchNumber()));
            }

            // ৩. 🌟 [KEY FIX]: Branch Inventory-তে স্টক যোগ/বিয়োগ করা 🌟
            if (!isReverse) {
                // ইনকামিং গুডস: ব্র্যাঞ্চ ইনভেন্টরিতে স্টক অটো-লোড হবে
                branchInventoryService.addStock(branchId, batchId, item.getReceivedQuantity());
            } else {
                // ক্যান্সেল হওয়া GRN: ব্র্যাঞ্চ ইনভেন্টরি থেকে স্টক রিভার্স / বিয়োগ করা হবে
                branchInventoryService.deductStock(branchId, batchId, item.getReceivedQuantity());
            }

            // ৪. Purchase Order Item এ প্রাপ্ত পরিমাণ আপডেট করা
            if (po != null && po.getItems() != null) {
                po.getItems().stream()
                        .filter(poi -> poi.getMedicine() != null && poi.getMedicine().getId().equals(item.getMedicine().getId()))
                        .findFirst()
                        .ifPresent(poi -> {
                            int currentRec = poi.getReceivedQuantity() != null ? poi.getReceivedQuantity() : 0;
                            int newRec = isReverse ? Math.max(0, currentRec - item.getReceivedQuantity()) : (currentRec + item.getReceivedQuantity());
                            poi.setReceivedQuantity(newRec);
                        });
            }

            // ৫. স্টক লেজারে পজিটিভ/নেগেটিভ এন্ট্রি রেকর্ড করা
            StockMovementType movementType = isReverse ? StockMovementType.PURCHASE_RETURN : StockMovementType.PURCHASE_RECEIVED;
            String auditRemarks = isReverse
                    ? "REVERSE STOCK ENTRY: Cancelled Approved GRN with Number: " + grn.getGrnNumber()
                    : "Inventory stocked inward via GRN: " + grn.getGrnNumber();

            stockMovementService.recordMovement(
                    branchId,
                    batchId,
                    movementType,
                    item.getReceivedQuantity(),
                    "GOODS_RECEIVED_NOTE",
                    grn.getId(),
                    auditRemarks
            );
        }

        // ৬. Purchase Order এর স্ট্যাটাস RECEIVED এ আপডেট করা (ক্যান্সেল হলে APPROVED এ ফেরত দেওয়া)
        if (po != null) {
            if (!isReverse) {
                po.setStatus(com.mishkat.PharmacyManagement.enums.PurchaseOrderStatus.RECEIVED);
            } else {
                po.setStatus(com.mishkat.PharmacyManagement.enums.PurchaseOrderStatus.APPROVED);
            }
            purchaseOrderRepository.save(po);
        }
    }
}
