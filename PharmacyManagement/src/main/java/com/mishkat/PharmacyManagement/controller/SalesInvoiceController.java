package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.SalesInvoiceRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.SalesInvoiceResponseDto;
import com.mishkat.PharmacyManagement.enums.InvoiceStatus;
import com.mishkat.PharmacyManagement.service.SalesInvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/sales-invoices")
@RequiredArgsConstructor
public class SalesInvoiceController {
    private final SalesInvoiceService salesInvoiceService;

    // POST /api/sales-invoices
    @PostMapping
    public ResponseEntity<SalesInvoiceResponseDto> create(
            @Valid @RequestBody SalesInvoiceRequestDto dto) {
        return new ResponseEntity<>(salesInvoiceService.createInvoice(dto), HttpStatus.CREATED);
    }

    // GET /api/sales-invoices
    @GetMapping
    public ResponseEntity<List<SalesInvoiceResponseDto>> getAll() {
        List<SalesInvoiceResponseDto> list = salesInvoiceService.getAllInvoices();
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    // GET /api/sales-invoices/1
    @GetMapping("/{id}")
    public SalesInvoiceResponseDto getById(@PathVariable Long id) {
        return salesInvoiceService.getInvoiceById(id);
    }

    // GET /api/sales-invoices/number/INV-001
    @GetMapping("/number/{invoiceNumber}")
    public SalesInvoiceResponseDto getByInvoiceNumber(@PathVariable String invoiceNumber) {
        return salesInvoiceService.getInvoiceByNumber(invoiceNumber);
    }

    // GET /api/sales-invoices/branch/5
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<SalesInvoiceResponseDto>> getByBranchId(@PathVariable Long branchId) {
        List<SalesInvoiceResponseDto> list = salesInvoiceService.getInvoicesByBranchId(branchId);
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    // GET /api/sales-invoices/customer/10
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<SalesInvoiceResponseDto>> getByCustomerId(@PathVariable Long customerId) {
        List<SalesInvoiceResponseDto> list = salesInvoiceService.getInvoicesByCustomerId(customerId);
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    // GET /api/sales-invoices/status/PAID
    @GetMapping("/status/{status}")
    public ResponseEntity<List<SalesInvoiceResponseDto>> getByStatus(@PathVariable InvoiceStatus status) {
        List<SalesInvoiceResponseDto> list = salesInvoiceService.getInvoicesByStatus(status);
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    // GET /api/sales-invoices/due
    @GetMapping("/due")
    public ResponseEntity<List<SalesInvoiceResponseDto>> getDueInvoices(
            @RequestParam(defaultValue = "0.0") BigDecimal minimumDue) {
        List<SalesInvoiceResponseDto> list = salesInvoiceService.getDueInvoices(minimumDue);
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    // PUT /api/sales-invoices/1
    @PutMapping("/{id}")
    public SalesInvoiceResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody SalesInvoiceRequestDto dto) {
        return salesInvoiceService.updateInvoice(id, dto);
    }

    // PATCH /api/sales-invoices/1/status?status=CANCELLED
    @PatchMapping("/{id}/status")
    public SalesInvoiceResponseDto updateStatus(
            @PathVariable Long id,
            @RequestParam InvoiceStatus status) {
        return salesInvoiceService.updateInvoiceStatus(id, status);
    }

    // DELETE /api/sales-invoices/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        salesInvoiceService.deleteInvoice(id);
        return ResponseEntity.ok("Sales Invoice deleted successfully");
    }
}
