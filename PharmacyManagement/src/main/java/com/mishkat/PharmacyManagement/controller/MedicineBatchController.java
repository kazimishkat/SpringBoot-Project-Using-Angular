package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.MedicineBatchRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.MedicineBatchResponseDto;
import com.mishkat.PharmacyManagement.service.MedicineBatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/medicine-batches")
@RequiredArgsConstructor
public class MedicineBatchController {
    private final MedicineBatchService batchService;

    // POST /api/medicine-batches
    @PostMapping
    public ResponseEntity<MedicineBatchResponseDto> create(
            @Valid @RequestBody MedicineBatchRequestDto dto) {
        return new ResponseEntity<>(batchService.createBatch(dto), HttpStatus.CREATED);
    }

    // GET /api/medicine-batches
    @GetMapping
    public ResponseEntity<List<MedicineBatchResponseDto>> getAll() {
        List<MedicineBatchResponseDto> list = batchService.getAllBatches();
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/medicine-batches/1
    @GetMapping("/{id}")
    public MedicineBatchResponseDto getById(@PathVariable Long id) {
        return batchService.getBatchById(id);
    }

    // GET /api/medicine-batches/medicine/10
    @GetMapping("/medicine/{medicineId}")
    public ResponseEntity<List<MedicineBatchResponseDto>> getByMedicine(@PathVariable Long medicineId) {
        List<MedicineBatchResponseDto> list = batchService.getBatchesByMedicine(medicineId);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/medicine-batches/number/BATCH-001
    @GetMapping("/number/{batchNumber}")
    public ResponseEntity<List<MedicineBatchResponseDto>> getByBatchNumber(@PathVariable String batchNumber) {
        List<MedicineBatchResponseDto> list = batchService.getBatchesByNumber(batchNumber);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/medicine-batches/expiring?beforeDate=2024-12-31
    @GetMapping("/expiring")
    public ResponseEntity<List<MedicineBatchResponseDto>> getExpiringBatches(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beforeDate) {
        List<MedicineBatchResponseDto> list = batchService.getExpiringBatches(beforeDate);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // PUT /api/medicine-batches/1
    @PutMapping("/{id}")
    public MedicineBatchResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody MedicineBatchRequestDto dto) {
        return batchService.updateBatch(id, dto);
    }

    // DELETE /api/medicine-batches/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        batchService.deleteBatch(id);
        return ResponseEntity.ok("Medicine Batch deleted successfully");
    }
}
