package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.PurchaseOrderRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PurchaseOrderResponseDto;
import com.mishkat.PharmacyManagement.enums.PurchaseOrderStatus;
import com.mishkat.PharmacyManagement.service.PurchaseOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    // POST /api/purchase-orders
    @PostMapping
    public ResponseEntity<PurchaseOrderResponseDto> create(
            @Valid @RequestBody PurchaseOrderRequestDto dto) {
        return new ResponseEntity<>(purchaseOrderService.createPurchaseOrder(dto), HttpStatus.CREATED);
    }

    // GET /api/purchase-orders
    @GetMapping
    public ResponseEntity<List<PurchaseOrderResponseDto>> getAll() {
        List<PurchaseOrderResponseDto> list = purchaseOrderService.getAllPurchaseOrders();
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/purchase-orders/status/PENDING
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PurchaseOrderResponseDto>> getByStatus(@PathVariable PurchaseOrderStatus status) {
        List<PurchaseOrderResponseDto> list = purchaseOrderService.getPurchaseOrdersByStatus(status);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/purchase-orders/1
    @GetMapping("/{id}")
    public PurchaseOrderResponseDto getById(@PathVariable Long id) {
        return purchaseOrderService.getPurchaseOrderById(id);
    }

    // GET /api/purchase-orders/number/PO-001
    @GetMapping("/number/{poNumber}")
    public PurchaseOrderResponseDto getByPoNumber(@PathVariable String poNumber) {
        return purchaseOrderService.getPurchaseOrderByPoNumber(poNumber);
    }

    // PUT /api/purchase-orders/1
    @PutMapping("/{id}")
    public PurchaseOrderResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseOrderRequestDto dto) {
        return purchaseOrderService.updatePurchaseOrder(id, dto);
    }

    // PATCH /api/purchase-orders/1/status?status=RECEIVED
    @PatchMapping("/{id}/status")
    public PurchaseOrderResponseDto updateStatus(
            @PathVariable Long id,
            @RequestParam PurchaseOrderStatus status) {
        return purchaseOrderService.updatePurchaseOrderStatus(id, status);
    }

    // PATCH /api/purchase-orders/1/approve
    @PatchMapping("/{id}/approve")
    public ResponseEntity<PurchaseOrderResponseDto> approveOrder(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseOrderService.approvePurchaseOrder(id));
    }

    // PATCH /api/purchase-orders/1/reject
    @PatchMapping("/{id}/reject")
    public ResponseEntity<PurchaseOrderResponseDto> rejectOrder(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseOrderService.rejectPurchaseOrder(id));
    }

    // DELETE /api/purchase-orders/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        purchaseOrderService.deletePurchaseOrder(id);
        return ResponseEntity.ok("Purchase Order deleted successfully");
    }
}
