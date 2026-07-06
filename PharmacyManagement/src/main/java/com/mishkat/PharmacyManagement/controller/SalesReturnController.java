package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.SalesReturnRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.SalesReturnResponseDto;
import com.mishkat.PharmacyManagement.service.SalesReturnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales-returns")
@RequiredArgsConstructor
public class SalesReturnController {
    private final SalesReturnService salesReturnService;

    // POST /api/sales-returns
    @PostMapping
    public ResponseEntity<SalesReturnResponseDto> create(
            @Valid @RequestBody SalesReturnRequestDto dto) {
        return new ResponseEntity<>(salesReturnService.createSalesReturn(dto), HttpStatus.CREATED);
    }

    // GET /api/sales-returns
    @GetMapping
    public ResponseEntity<List<SalesReturnResponseDto>> getAll() {
        List<SalesReturnResponseDto> list = salesReturnService.getAllSalesReturns();
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/sales-returns/1
    @GetMapping("/{id}")
    public SalesReturnResponseDto getById(@PathVariable Long id) {
        return salesReturnService.getSalesReturnById(id);
    }

    // GET /api/sales-returns/number/SR-001
    @GetMapping("/number/{returnNumber}")
    public SalesReturnResponseDto getByReturnNumber(@PathVariable String returnNumber) {
        return salesReturnService.getSalesReturnByNumber(returnNumber);
    }

    // GET /api/sales-returns/invoice/5
    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<SalesReturnResponseDto>> getByInvoiceId(@PathVariable Long invoiceId) {
        List<SalesReturnResponseDto> list = salesReturnService.getSalesReturnsByInvoiceId(invoiceId);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // PUT /api/sales-returns/1
    @PutMapping("/{id}")
    public SalesReturnResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody SalesReturnRequestDto dto) {
        return salesReturnService.updateSalesReturn(id, dto);
    }

    // DELETE /api/sales-returns/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        salesReturnService.deleteSalesReturn(id);
        return ResponseEntity.ok("Sales Return deleted successfully");
    }
}
