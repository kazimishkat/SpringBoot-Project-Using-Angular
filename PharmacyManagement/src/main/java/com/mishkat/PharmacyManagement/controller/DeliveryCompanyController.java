package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.DeliveryCompanyRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.DeliveryCompanyResponseDto;
import com.mishkat.PharmacyManagement.service.DeliveryCompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery-companies")
@RequiredArgsConstructor
public class DeliveryCompanyController {
    private final DeliveryCompanyService deliveryCompanyService;

    // POST /api/delivery-companies
    @PostMapping
    public ResponseEntity<DeliveryCompanyResponseDto> registerCompany(
            @Valid @RequestBody DeliveryCompanyRequestDto dto) {
        return new ResponseEntity<>(deliveryCompanyService.registerCompany(dto), HttpStatus.CREATED);
    }


    // GET /api/delivery-companies/active
    @GetMapping("/active")
    public ResponseEntity<List<DeliveryCompanyResponseDto>> getAllActiveCompanies() {
        List<DeliveryCompanyResponseDto> list = deliveryCompanyService.getActiveCompanies();
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }


    // GET /api/delivery-companies/1
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryCompanyResponseDto> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryCompanyService.getCompanyById(id));
    }


    // PATCH /api/delivery-companies/1/toggle-status?isActive=false
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<DeliveryCompanyResponseDto> toggleCompanyStatus(
            @PathVariable Long id,
            @RequestParam Boolean isActive) {
        return ResponseEntity.ok(deliveryCompanyService.updateCompanyStatus(id, isActive));
    }
}
