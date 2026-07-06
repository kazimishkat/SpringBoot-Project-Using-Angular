package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.StockMovementRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.StockMovementResponseDto;
import com.mishkat.PharmacyManagement.enums.StockMovementType;
import com.mishkat.PharmacyManagement.service.StockMovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
@RequiredArgsConstructor
public class StockMovementController {
    private final StockMovementService stockMovementService;

    // POST /api/stock-movements
    @PostMapping
    public ResponseEntity<StockMovementResponseDto> create(
            @Valid @RequestBody StockMovementRequestDto dto) {
        return new ResponseEntity<>(stockMovementService.createStockMovement(dto), HttpStatus.CREATED);
    }

    // GET /api/stock-movements
    @GetMapping
    public ResponseEntity<List<StockMovementResponseDto>> getAll() {
        List<StockMovementResponseDto> list = stockMovementService.getAllStockMovements();
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/stock-movements/1
    @GetMapping("/{id}")
    public StockMovementResponseDto getById(@PathVariable Long id) {
        return stockMovementService.getStockMovementById(id);
    }

    // GET /api/stock-movements/branch/5
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<StockMovementResponseDto>> getByBranchId(@PathVariable Long branchId) {
        List<StockMovementResponseDto> list = stockMovementService.getMovementsByBranchId(branchId);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/stock-movements/batch/10
    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<StockMovementResponseDto>> getByBatchId(@PathVariable Long batchId) {
        List<StockMovementResponseDto> list = stockMovementService.getMovementsByBatchId(batchId);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/stock-movements/branch/5/type/IN
    @GetMapping("/branch/{branchId}/type/{movementType}")
    public ResponseEntity<List<StockMovementResponseDto>> getByBranchAndType(
            @PathVariable Long branchId,
            @PathVariable StockMovementType movementType) {
        List<StockMovementResponseDto> list = stockMovementService.getMovementsByBranchAndType(branchId, movementType);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/stock-movements/reference?type=SALES_INVOICE&id=25
    @GetMapping("/reference")
    public ResponseEntity<List<StockMovementResponseDto>> getByReference(
            @RequestParam String type,
            @RequestParam Long id) {
        List<StockMovementResponseDto> list = stockMovementService.getMovementsByReference(type, id);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }
}
