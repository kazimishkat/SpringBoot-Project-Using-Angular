package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.SalesInvoiceRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PaymentResponseDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.SalesInvoiceResponseDto;
import com.mishkat.PharmacyManagement.enums.InvoiceStatus;
import com.mishkat.PharmacyManagement.service.SalesInvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sales-invoices")
@RequiredArgsConstructor
public class SalesInvoiceController {
    private final SalesInvoiceService salesInvoiceService;

    @PostMapping
    public ResponseEntity<SalesInvoiceResponseDto> create(@Valid @RequestBody SalesInvoiceRequestDto dto) {
        return new ResponseEntity<>(salesInvoiceService.createInvoice(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SalesInvoiceResponseDto>> getAll() {
        List<SalesInvoiceResponseDto> list = salesInvoiceService.getAllInvoices();
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public SalesInvoiceResponseDto getById(@PathVariable Long id) {
        return salesInvoiceService.getInvoiceById(id);
    }

    @GetMapping("/number/{invoiceNumber}")
    public SalesInvoiceResponseDto getByInvoiceNumber(@PathVariable String invoiceNumber) {
        return salesInvoiceService.getInvoiceByNumber(invoiceNumber);
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<SalesInvoiceResponseDto>> getByBranchId(@PathVariable Long branchId) {
        List<SalesInvoiceResponseDto> list = salesInvoiceService.getInvoicesByBranchId(branchId);
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<SalesInvoiceResponseDto>> getByCustomerId(@PathVariable Long customerId) {
        List<SalesInvoiceResponseDto> list = salesInvoiceService.getInvoicesByCustomerId(customerId);
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<SalesInvoiceResponseDto>> getByStatus(@PathVariable InvoiceStatus status) {
        List<SalesInvoiceResponseDto> list = salesInvoiceService.getInvoicesByStatus(status);
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    @GetMapping("/due")
    public ResponseEntity<List<SalesInvoiceResponseDto>> getDueInvoices(
            @RequestParam(defaultValue = "0.0") BigDecimal minimumDue) {
        List<SalesInvoiceResponseDto> list = salesInvoiceService.getDueInvoices(minimumDue);
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    // 🌟 [NEW]: Search Invoices
    @GetMapping("/search")
    public ResponseEntity<List<SalesInvoiceResponseDto>> search(@RequestParam String query) {
        return ResponseEntity.ok(salesInvoiceService.searchInvoices(query));
    }

    // 🌟 [NEW]: Filter Invoices
    @GetMapping("/filter")
    public ResponseEntity<List<SalesInvoiceResponseDto>> filter(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) InvoiceStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(salesInvoiceService.filterInvoices(customerId, status, startDate, endDate));
    }

    // 🌟 [NEW]: Get Payments for an Invoice
    @GetMapping("/{id}/payments")
    public ResponseEntity<List<PaymentResponseDto>> getPayments(@PathVariable Long id) {
        return ResponseEntity.ok(salesInvoiceService.getInvoicePayments(id));
    }

    // 🌟 [NEW]: Print/Download PDF Endpoint
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        byte[] pdf = salesInvoiceService.printInvoicePdf(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PutMapping("/{id}")
    public SalesInvoiceResponseDto update(@PathVariable Long id, @Valid @RequestBody SalesInvoiceRequestDto dto) {
        return salesInvoiceService.updateInvoice(id, dto);
    }

    @PatchMapping("/{id}/status")
    public SalesInvoiceResponseDto updateStatus(@PathVariable Long id, @RequestParam InvoiceStatus status) {
        return salesInvoiceService.updateInvoiceStatus(id, status);
    }

    // 🌟 [NEW]: Cancel Invoice
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<SalesInvoiceResponseDto> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(salesInvoiceService.cancelInvoice(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        salesInvoiceService.deleteInvoice(id);
        return ResponseEntity.ok("Sales Invoice deleted successfully");
    }
}
