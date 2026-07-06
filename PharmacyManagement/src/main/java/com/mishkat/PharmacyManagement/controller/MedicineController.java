package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.MedicineRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.MedicineResponseDto;
import com.mishkat.PharmacyManagement.service.MedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineService medicineService;

    // POST /api/medicines — multipart: "medicine" (JSON) + "image" (file, optional)
    @PostMapping
    public ResponseEntity<MedicineResponseDto> create(
            @RequestPart("medicine") @Valid MedicineRequestDto dto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return new ResponseEntity<>(medicineService.createMedicine(dto, image), HttpStatus.CREATED);
    }

    // GET /api/medicines
    @GetMapping
    public ResponseEntity<List<MedicineResponseDto>> getAll() {
        List<MedicineResponseDto> list = medicineService.getAllMedicines();
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/medicines/1
    @GetMapping("/{id}")
    public MedicineResponseDto getById(@PathVariable Long id) {
        return medicineService.getMedicineById(id);
    }

    // GET /api/medicines/code/MED-001
    @GetMapping("/code/{medicineCode}")
    public MedicineResponseDto getByCode(@PathVariable String medicineCode) {
        return medicineService.getMedicineByCode(medicineCode);
    }

    // GET /api/medicines/search?brandName=Napa
    @GetMapping("/search")
    public ResponseEntity<List<MedicineResponseDto>> searchByBrandName(
            @RequestParam String brandName) {
        List<MedicineResponseDto> list = medicineService.searchMedicinesByBrandName(brandName);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/medicines/low-stock
    @GetMapping("/low-stock")
    public ResponseEntity<List<MedicineResponseDto>> getLowStockMedicines() {
        List<MedicineResponseDto> list = medicineService.getLowStockMedicines();
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // PUT /api/medicines/1 — multipart: "medicine" (JSON) + "image" (file, optional)
    @PutMapping("/{id}")
    public MedicineResponseDto update(
            @PathVariable Long id,
            @RequestPart("medicine") @Valid MedicineRequestDto dto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return medicineService.updateMedicine(id, dto, image);
    }

    // DELETE /api/medicines/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.ok("Medicine deleted successfully");
    }
}
