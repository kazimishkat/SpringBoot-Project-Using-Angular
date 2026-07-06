package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.PaymentRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PaymentResponseDto;
import com.mishkat.PharmacyManagement.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    // POST /api/payments
    @PostMapping
    public ResponseEntity<PaymentResponseDto> create(
            @Valid @RequestBody PaymentRequestDto dto) {
        return new ResponseEntity<>(paymentService.createPayment(dto), HttpStatus.CREATED);
    }

    // GET /api/payments
    @GetMapping
    public ResponseEntity<List<PaymentResponseDto>> getAll() {
        List<PaymentResponseDto> list = paymentService.getAllPayments();
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/payments/1
    @GetMapping("/{id}")
    public PaymentResponseDto getById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    // GET /api/payments/invoice/5
    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<PaymentResponseDto>> getByInvoiceId(@PathVariable Long invoiceId) {
        List<PaymentResponseDto> list = paymentService.getPaymentsByInvoiceId(invoiceId);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // DELETE /api/payments/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok("Payment deleted successfully");
    }
}
