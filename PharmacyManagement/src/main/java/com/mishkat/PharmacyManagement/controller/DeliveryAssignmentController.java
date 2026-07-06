package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.DeliveryAssignmentRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.DeliveryAssignmentResponseDto;
import com.mishkat.PharmacyManagement.service.DeliveryAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery-assignments")
@RequiredArgsConstructor
public class DeliveryAssignmentController {
    private final DeliveryAssignmentService deliveryAssignmentService;


    // POST /api/delivery-assignments/assign
    @PostMapping("/assign")
    public ResponseEntity<DeliveryAssignmentResponseDto> assignToCourier(
            @Valid @RequestBody DeliveryAssignmentRequestDto dto) {
        return new ResponseEntity<>(deliveryAssignmentService.assignOrderToCourier(dto), HttpStatus.CREATED);
    }


    // GET /api/delivery-assignments/order/1
    @GetMapping("/order/{onlineOrderId}")
    public ResponseEntity<DeliveryAssignmentResponseDto> getAssignmentDetails(
            @PathVariable Long onlineOrderId) {
        return ResponseEntity.ok(deliveryAssignmentService.getAssignmentByOrderId(onlineOrderId));
    }

    // PATCH /api/delivery-assignments/webhook/update-status?trackingNumber=TRK7723901&courierStatus=DELIVERED
    @PatchMapping("/webhook/update-status")
    public ResponseEntity<String> courierWebhookListener(
            @RequestParam String trackingNumber,
            @RequestParam String courierStatus) {

        deliveryAssignmentService.updateLiveDeliveryStatus(trackingNumber, courierStatus);
        return ResponseEntity.ok("Pharmacy internal logistics system successfully synchronized with courier payload.");
    }
}
