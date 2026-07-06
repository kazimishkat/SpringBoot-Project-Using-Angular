package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.PurchaseOrderMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.PurchaseOrderItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.PurchaseOrderRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PurchaseOrderResponseDto;
import com.mishkat.PharmacyManagement.entity.*;
import com.mishkat.PharmacyManagement.enums.PurchaseOrderStatus;
import com.mishkat.PharmacyManagement.repository.BranchRepository;
import com.mishkat.PharmacyManagement.repository.MedicineRepository;
import com.mishkat.PharmacyManagement.repository.PurchaseOrderRepository;
import com.mishkat.PharmacyManagement.repository.SupplierRepository;
import com.mishkat.PharmacyManagement.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    // সমস্ত প্রয়োজনীয় Repository ইনজেক্ট করা হলো
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final BranchRepository branchRepository;
    private final MedicineRepository medicineRepository;

    // ম্যাপারের মেথডগুলো নন-স্ট্যাটিক হওয়ায় ইনস্ট্যান্স তৈরি করা হলো
    private final PurchaseOrderMapper mapper = new PurchaseOrderMapper();

    @Override
    @Transactional
    public PurchaseOrderResponseDto createPurchaseOrder(PurchaseOrderRequestDto dto) {
        // ১. PO Number ইউনিক কি না চেক করা
        if (purchaseOrderRepository.findByPoNumber(dto.getPoNumber()).isPresent()) {
            throw new RuntimeException("Purchase Order already exists with PO Number: " + dto.getPoNumber());
        }

        PurchaseOrder order = mapper.toEntity(dto);

        // ২. Supplier সেট করা
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + dto.getSupplierId()));
        order.setSupplier(supplier);

        // ৩. Branch সেট করা
        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + dto.getBranchId()));
        order.setBranch(branch);

        // ৪. প্রতিটি Item-এর জন্য Medicine সেট করা
        if (order.getItems() != null && dto.getItems() != null) {
            for (int i = 0; i < order.getItems().size(); i++) {
                PurchaseOrderItem item = order.getItems().get(i);
                PurchaseOrderItemRequestDto itemDto = dto.getItems().get(i);

                Medicine medicine = medicineRepository.findById(itemDto.getMedicineId())
                        .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + itemDto.getMedicineId()));
                item.setMedicine(medicine);
            }
        }

        // [নোট]: createdByUser সাধারণত Spring Security Context থেকে নেওয়া হয়।
        // তাই Request DTO থেকে এটা সেট করার প্রয়োজন নেই।

        PurchaseOrder savedOrder = purchaseOrderRepository.save(order);
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
        // Repository-তে findByPoNumber মেথডটি থাকতে হবে
        PurchaseOrder order = purchaseOrderRepository.findByPoNumber(poNumber)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with PO Number: " + poNumber));
        return mapper.toDTO(order);
    }

    @Override
    @Transactional
    public PurchaseOrderResponseDto updatePurchaseOrder(Long id, PurchaseOrderRequestDto dto) {
        PurchaseOrder existingOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id: " + id));

        // PO Number পরিবর্তন হলে ডুপ্লিকেট চেক
        if (dto.getPoNumber() != null && !existingOrder.getPoNumber().equals(dto.getPoNumber())) {
            if (purchaseOrderRepository.findByPoNumber(dto.getPoNumber()).isPresent()) {
                throw new RuntimeException("PO Number already exists: " + dto.getPoNumber());
            }
        }

        // সাধারণ ফিল্ড আপডেট
        mapper.updateEntityFromDto(dto, existingOrder);

        // Supplier আপডেট
        if (dto.getSupplierId() != null && !existingOrder.getSupplier().getId().equals(dto.getSupplierId())) {
            Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + dto.getSupplierId()));
            existingOrder.setSupplier(supplier);
        }

        // Branch আপডেট
        if (dto.getBranchId() != null && !existingOrder.getBranch().getId().equals(dto.getBranchId())) {
            Branch branch = branchRepository.findById(dto.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found with id: " + dto.getBranchId()));
            existingOrder.setBranch(branch);
        }

        // Items আপডেট (পুরোনো লিস্ট ক্লিয়ার করে নতুন আইটেম যোগ করা - orphanRemoval এটি সামলাবে)
        if (dto.getItems() != null) {
            existingOrder.getItems().clear(); // পুরোনো আইটেম রিমুভ
            for (PurchaseOrderItemRequestDto itemDto : dto.getItems()) {
                PurchaseOrderItem newItem = new PurchaseOrderItem();
                newItem.setOrderedQuantity(itemDto.getOrderedQuantity());
                newItem.setUnitPrice(itemDto.getUnitPrice());
                newItem.setReceivedQuantity(0); // নতুন করে অর্ডার আপডেট হলে রিসিভ কোয়ান্টিটি 0 হবে

                Medicine medicine = medicineRepository.findById(itemDto.getMedicineId())
                        .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + itemDto.getMedicineId()));
                newItem.setMedicine(medicine);

                newItem.setPurchaseOrder(existingOrder); // Parent লিংক করা
                existingOrder.getItems().add(newItem);
            }
        }

        PurchaseOrder savedOrder = purchaseOrderRepository.save(existingOrder);
        return mapper.toDTO(savedOrder);
    }

    @Override
    @Transactional
    public PurchaseOrderResponseDto updatePurchaseOrderStatus(Long id, PurchaseOrderStatus status) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id: " + id));

        order.setStatus(status);
        return mapper.toDTO(purchaseOrderRepository.save(order));
    }

    @Override
    @Transactional
    public void deletePurchaseOrder(Long id) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id: " + id));
        purchaseOrderRepository.delete(order);
    }
}
