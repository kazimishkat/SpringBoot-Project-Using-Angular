package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.SupplierRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.SupplierResponseDto;
import com.mishkat.PharmacyManagement.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    // ── Supplier profile management (Central Manager / Super Admin Actions) ──

    // POST /api/suppliers
    @PostMapping
    public ResponseEntity<SupplierResponseDto> createSupplier(@Valid @RequestBody SupplierRequestDto dto) {
        SupplierResponseDto response = supplierService.createSupplier(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET /api/suppliers
    @GetMapping
    public ResponseEntity<List<SupplierResponseDto>> getAllSuppliers() {
        List<SupplierResponseDto> list = supplierService.getAllSuppliers();
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/suppliers/1
    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    // GET /api/suppliers/code/SPL-VNDR-82
    @GetMapping("/code/{supplierCode}")
    public ResponseEntity<SupplierResponseDto> getSupplierByCode(@PathVariable String supplierCode) {
        return ResponseEntity.ok(supplierService.getSupplierByCode(supplierCode));
    }

    // PUT /api/suppliers/1
    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SupplierRequestDto dto) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, dto));
    }

    // DELETE /api/suppliers/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok("Supplier corporate account purged from registry records successfully.");
    }
}
