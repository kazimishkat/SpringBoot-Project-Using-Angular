package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.GenericMedicineRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.GenericMedicineResponseDto;
import com.mishkat.PharmacyManagement.service.GenericMedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/generic-medicines")
@RequiredArgsConstructor
public class GenericMedicineController {
    private final GenericMedicineService genericMedicineService;

    // POST /api/generic-medicines
    @PostMapping
    public ResponseEntity<GenericMedicineResponseDto> create(
            @Valid @RequestBody GenericMedicineRequestDto dto) {
        return new ResponseEntity<>(genericMedicineService.createGenericMedicine(dto), HttpStatus.CREATED);
    }

    // GET /api/generic-medicines
    @GetMapping
    public ResponseEntity<List<GenericMedicineResponseDto>> getAll() {
        List<GenericMedicineResponseDto> list = genericMedicineService.getAllGenericMedicines();
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/generic-medicines/1
    @GetMapping("/{id}")
    public GenericMedicineResponseDto getById(@PathVariable Long id) {
        return genericMedicineService.getGenericMedicineById(id);
    }

    // GET /api/generic-medicines/name/Paracetamol
    @GetMapping("/name/{genericName}")
    public GenericMedicineResponseDto getByGenericName(@PathVariable String genericName) {
        return genericMedicineService.getByGenericName(genericName);
    }

    // PUT /api/generic-medicines/1
    @PutMapping("/{id}")
    public GenericMedicineResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody GenericMedicineRequestDto dto) {
        return genericMedicineService.updateGenericMedicine(id, dto);
    }

    // DELETE /api/generic-medicines/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        genericMedicineService.deleteGenericMedicine(id);
        return ResponseEntity.ok("Generic Medicine deleted successfully");
    }
}
