package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.OnlineOrderRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.OnlineOrderResponseDto;
import com.mishkat.PharmacyManagement.enums.OnlineOrderStatus;
import com.mishkat.PharmacyManagement.service.OnlineOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/online-orders")
@RequiredArgsConstructor
public class OnlineOrderController {
    private final OnlineOrderService onlineOrderService;


    // POST /api/online-orders/place?customerId=1&transactionId=TXN992348
    @PostMapping("/place")
    public ResponseEntity<OnlineOrderResponseDto> placeNewOrder(
            @Valid @RequestBody OnlineOrderRequestDto dto,
            @RequestParam Long customerId,
            @RequestParam(required = false) String transactionId) {

        OnlineOrderResponseDto response = onlineOrderService.placeOrder(dto, customerId, transactionId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    // GET /api/online-orders/customer/1/history
    @GetMapping("/customer/{customerId}/history")
    public ResponseEntity<List<OnlineOrderResponseDto>> getCustomerOrderHistory(
            @PathVariable Long customerId) {

        List<OnlineOrderResponseDto> history = onlineOrderService.getOrderHistoryByCustomerId(customerId);
        return history.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(history);
    }


    // GET /api/online-orders/track/ORD-1718293849
    @GetMapping("/track/{orderNumber}")
    public ResponseEntity<OnlineOrderResponseDto> trackOrderByNumber(
            @PathVariable String orderNumber) {
        return ResponseEntity.ok(onlineOrderService.getOrderByNumber(orderNumber));
    }


    // GET /api/online-orders/branch/5/pending
    @GetMapping("/branch/{branchId}/pending")
    public ResponseEntity<List<OnlineOrderResponseDto>> getPendingOrders(@PathVariable Long branchId) {
        List<OnlineOrderResponseDto> pendingList = onlineOrderService.getPendingOrdersByBranch(branchId);
        return pendingList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pendingList);
    }


    // PATCH /api/online-orders/1/status?status=CONFIRMED
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OnlineOrderResponseDto> changeOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OnlineOrderStatus status) {
        return ResponseEntity.ok(onlineOrderService.updateOrderStatus(orderId, status));
    }
}
