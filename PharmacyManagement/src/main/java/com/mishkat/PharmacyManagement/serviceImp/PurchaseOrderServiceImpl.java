package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.PurchaseOrderMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.PurchaseOrderItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.PurchaseOrderRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PurchaseOrderResponseDto;
import com.mishkat.PharmacyManagement.entity.*;
import com.mishkat.PharmacyManagement.enums.PurchaseOrderStatus;
import com.mishkat.PharmacyManagement.enums.StockMovementType;
import com.mishkat.PharmacyManagement.repository.BranchRepository;
import com.mishkat.PharmacyManagement.repository.MedicineRepository;
import com.mishkat.PharmacyManagement.repository.PurchaseOrderRepository;
import com.mishkat.PharmacyManagement.repository.SupplierRepository;
import com.mishkat.PharmacyManagement.service.PurchaseOrderService;
import com.mishkat.PharmacyManagement.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final BranchRepository branchRepository;
    private final MedicineRepository medicineRepository;

    // Injecting Stock Movement Internal Audit Ledger
    private final StockMovementService stockMovementService;

    private final PurchaseOrderMapper mapper = new PurchaseOrderMapper();

    @Override
    @Transactional
    public PurchaseOrderResponseDto createPurchaseOrder(PurchaseOrderRequestDto dto) {
        if (purchaseOrderRepository.findByPoNumber(dto.getPoNumber()).isPresent()) {
            throw new RuntimeException("Purchase Order already exists with PO Number: " + dto.getPoNumber());
        }

        PurchaseOrder order = mapper.toEntity(dto);

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + dto.getSupplierId()));
        order.setSupplier(supplier);

        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + dto.getBranchId()));
        order.setBranch(branch);

        if (order.getItems() != null && dto.getItems() != null) {
            for (int i = 0; i < order.getItems().size(); i++) {
                PurchaseOrderItem item = order.getItems().get(i);
                PurchaseOrderItemRequestDto itemDto = dto.getItems().get(i);

                Medicine medicine = medicineRepository.findById(itemDto.getMedicineId())
                        .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + itemDto.getMedicineId()));
                item.setMedicine(medicine);
            }
        }

        PurchaseOrder savedOrder = purchaseOrderRepository.save(order);

        // If initial status state configuration falls straight into RECEIVED, write ledger entries
        if (savedOrder.getStatus() == PurchaseOrderStatus.RECEIVED) {
            logStockMovementsForPO(savedOrder);
        }

        return mapper.toDTO(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponseDto> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderResponseDto getPurchaseOrderById(Long id) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id: " + id));
        return mapper.toDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderResponseDto getPurchaseOrderByPoNumber(String poNumber) {
        PurchaseOrder order = purchaseOrderRepository.findByPoNumber(poNumber)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with PO Number: " + poNumber));
        return mapper.toDTO(order);
    }

    @Override
    @Transactional
    public PurchaseOrderResponseDto updatePurchaseOrder(Long id, PurchaseOrderRequestDto dto) {
        PurchaseOrder existingOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id: " + id));

        if (dto.getPoNumber() != null && !existingOrder.getPoNumber().equals(dto.getPoNumber())) {
            if (purchaseOrderRepository.findByPoNumber(dto.getPoNumber()).isPresent()) {
                throw new RuntimeException("PO Number already exists: " + dto.getPoNumber());
            }
        }

        mapper.updateEntityFromDto(dto, existingOrder);

        if (dto.getSupplierId() != null && !existingOrder.getSupplier().getId().equals(dto.getSupplierId())) {
            Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + dto.getSupplierId()));
            existingOrder.setSupplier(supplier);
        }

        if (dto.getBranchId() != null && !existingOrder.getBranch().getId().equals(dto.getBranchId())) {
            Branch branch = branchRepository.findById(dto.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found with id: " + dto.getBranchId()));
            existingOrder.setBranch(branch);
        }

        if (dto.getItems() != null) {
            existingOrder.getItems().clear();
            for (PurchaseOrderItemRequestDto itemDto : dto.getItems()) {
                PurchaseOrderItem newItem = new PurchaseOrderItem();
                newItem.setOrderedQuantity(itemDto.getOrderedQuantity());
                newItem.setUnitPrice(itemDto.getUnitPrice());
                newItem.setReceivedQuantity(0);

                Medicine medicine = medicineRepository.findById(itemDto.getMedicineId())
                        .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + itemDto.getMedicineId()));
                newItem.setMedicine(medicine);

                newItem.setPurchaseOrder(existingOrder);
                existingOrder.getItems().add(newItem);
            }
        }

        PurchaseOrder savedOrder = purchaseOrderRepository.save(existingOrder);

        if (savedOrder.getStatus() == PurchaseOrderStatus.RECEIVED) {
            logStockMovementsForPO(savedOrder);
        }

        return mapper.toDTO(savedOrder);
    }

    @Override
    @Transactional
    public PurchaseOrderResponseDto updatePurchaseOrderStatus(Long id, PurchaseOrderStatus status) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id: " + id));

        PurchaseOrderStatus oldStatus = order.getStatus();
        order.setStatus(status);
        PurchaseOrder savedOrder = purchaseOrderRepository.save(order);

        // State Machine validation guard to avoid multiple dynamic hits to stock ledger
        if (status == PurchaseOrderStatus.RECEIVED && oldStatus != PurchaseOrderStatus.RECEIVED) {
            logStockMovementsForPO(savedOrder);
        }

        return mapper.toDTO(savedOrder);
    }

    @Override
    @Transactional
    public void deletePurchaseOrder(Long id) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id: " + id));
        purchaseOrderRepository.delete(order);
    }

    // ── Angular Required Method Implementations ──

    @Override
    @Transactional
    public PurchaseOrderResponseDto approvePurchaseOrder(Long id) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order execution failed: Invalid identity key " + id));
        order.setStatus(PurchaseOrderStatus.APPROVED);
        return mapper.toDTO(purchaseOrderRepository.save(order));
    }

    @Override
    @Transactional
    public PurchaseOrderResponseDto rejectPurchaseOrder(Long id) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order execution failed: Invalid identity key " + id));
        order.setStatus(PurchaseOrderStatus.REJECTED);
        return mapper.toDTO(purchaseOrderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponseDto> getPurchaseOrdersByStatus(PurchaseOrderStatus status) {
        return purchaseOrderRepository.findByStatus(status).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    // Helper logic maps order quantities onto stock movement audit traces
    private void logStockMovementsForPO(PurchaseOrder order) {
        if (order.getItems() != null) {
            for (PurchaseOrderItem item : order.getItems()) {
                // [বিঃদ্রঃ]: আপনার রিয়েল ইনভেন্টরিতে ব্যাচ ক্রিয়েশন বা ট্র্যাকিং চেইনের জন্য
                // যে আইডি জেনারেট হয় তা এখানে পাস করবেন (ডিফল্ট হিসেবে ১ এল রাখা হয়েছে)।
                Long detectedBatchId = 1L;

                stockMovementService.recordMovement(
                        order.getBranch().getId(),
                        detectedBatchId,
                        StockMovementType.PURCHASE_RECEIVED,
                        item.getOrderedQuantity(),
                        "PURCHASE_ORDER",
                        order.getId(),
                        "Stock inward generated via Purchase Order fulfillment matching invoice tracker: " + order.getPoNumber()
                );
            }
        }
    }
}
