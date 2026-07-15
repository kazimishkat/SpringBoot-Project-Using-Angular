package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.PurchaseOrderRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PurchaseOrderResponseDto;
import com.mishkat.PharmacyManagement.enums.PurchaseOrderStatus;

import java.util.List;

public interface PurchaseOrderService {
    PurchaseOrderResponseDto createPurchaseOrder(PurchaseOrderRequestDto dto);

    List<PurchaseOrderResponseDto> getAllPurchaseOrders();

    PurchaseOrderResponseDto getPurchaseOrderById(Long id);

    PurchaseOrderResponseDto getPurchaseOrderByPoNumber(String poNumber);

    PurchaseOrderResponseDto updatePurchaseOrder(Long id, PurchaseOrderRequestDto dto);

    PurchaseOrderResponseDto updatePurchaseOrderStatus(Long id, PurchaseOrderStatus status);

    void deletePurchaseOrder(Long id);

    // ── Angular Service Compatibility Methods ──
    PurchaseOrderResponseDto approvePurchaseOrder(Long id);

    PurchaseOrderResponseDto rejectPurchaseOrder(Long id);

    List<PurchaseOrderResponseDto> getPurchaseOrdersByStatus(PurchaseOrderStatus status);
}
