package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.SalesReturnRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.SalesReturnResponseDto;
import com.mishkat.PharmacyManagement.enums.ApprovalStatus;
import com.mishkat.PharmacyManagement.service.SalesReturnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sales-returns")
@RequiredArgsConstructor
public class SalesReturnController {
    private final SalesReturnService salesReturnService;

    @PostMapping
    public ResponseEntity<SalesReturnResponseDto> create(@Valid @RequestBody SalesReturnRequestDto dto) {
        return new ResponseEntity<>(salesReturnService.createSalesReturn(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SalesReturnResponseDto>> getAll() {
        List<SalesReturnResponseDto> list = salesReturnService.getAllSalesReturns();
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public SalesReturnResponseDto getById(@PathVariable Long id) {
        return salesReturnService.getSalesReturnById(id);
    }

    @GetMapping("/number/{returnNumber}")
    public SalesReturnResponseDto getByReturnNumber(@PathVariable String returnNumber) {
        return salesReturnService.getSalesReturnByNumber(returnNumber);
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<SalesReturnResponseDto>> getByInvoiceId(@PathVariable Long invoiceId) {
        List<SalesReturnResponseDto> list = salesReturnService.getSalesReturnsByInvoiceId(invoiceId);
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    // 🌟 [NEW]: Approve Sales Return
    @PatchMapping("/{id}/approve")
    public ResponseEntity<SalesReturnResponseDto> approve(@PathVariable Long id) {
        return ResponseEntity.ok(salesReturnService.approveReturn(id));
    }

    // 🌟 [NEW]: Reject Sales Return
    @PatchMapping("/{id}/reject")
    public ResponseEntity<SalesReturnResponseDto> reject(@PathVariable Long id) {
        return ResponseEntity.ok(salesReturnService.rejectReturn(id));
    }

    // 🌟 [NEW]: Search Returns
    @GetMapping("/search")
    public ResponseEntity<List<SalesReturnResponseDto>> search(@RequestParam String query) {
        return ResponseEntity.ok(salesReturnService.searchReturns(query));
    }

    // 🌟 [NEW]: Filter Returns
    @GetMapping("/filter")
    public ResponseEntity<List<SalesReturnResponseDto>> filter(
            @RequestParam(required = false) Long invoiceId,
            @RequestParam(required = false) ApprovalStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(salesReturnService.filterReturns(invoiceId, status, startDate, endDate));
    }

    @PutMapping("/{id}")
    public SalesReturnResponseDto update(@PathVariable Long id, @Valid @RequestBody SalesReturnRequestDto dto) {
        return salesReturnService.updateSalesReturn(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        salesReturnService.deleteSalesReturn(id);
        return ResponseEntity.ok("Sales Return deleted successfully");
    }
}
