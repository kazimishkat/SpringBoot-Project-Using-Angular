package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.StockAdjustmentRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.StockAdjustmentResponseDto;
import com.mishkat.PharmacyManagement.service.StockAdjustmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-adjustments")
@RequiredArgsConstructor
public class StockAdjustmentController {
    private final StockAdjustmentService stockAdjustmentService;

    // POST /api/stock-adjustments
    @PostMapping
    public ResponseEntity<StockAdjustmentResponseDto> create(
            @Valid @RequestBody StockAdjustmentRequestDto dto) {
        return new ResponseEntity<>(stockAdjustmentService.createStockAdjustment(dto), HttpStatus.CREATED);
    }

    // GET /api/stock-adjustments
    @GetMapping
    public ResponseEntity<List<StockAdjustmentResponseDto>> getAll() {
        List<StockAdjustmentResponseDto> list = stockAdjustmentService.getAllStockAdjustments();
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/stock-adjustments/1
    @GetMapping("/{id}")
    public StockAdjustmentResponseDto getById(@PathVariable Long id) {
        return stockAdjustmentService.getStockAdjustmentById(id);
    }

    // GET /api/stock-adjustments/number/ADJ-001
    @GetMapping("/number/{adjustmentNumber}")
    public StockAdjustmentResponseDto getByAdjustmentNumber(@PathVariable String adjustmentNumber) {
        return stockAdjustmentService.getStockAdjustmentByNumber(adjustmentNumber);
    }

    // GET /api/stock-adjustments/branch/5
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<StockAdjustmentResponseDto>> getByBranchId(@PathVariable Long branchId) {
        List<StockAdjustmentResponseDto> list = stockAdjustmentService.getStockAdjustmentsByBranchId(branchId);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // PUT /api/stock-adjustments/1
    @PutMapping("/{id}")
    public StockAdjustmentResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody StockAdjustmentRequestDto dto) {
        return stockAdjustmentService.updateStockAdjustment(id, dto);
    }

    // DELETE /api/stock-adjustments/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        stockAdjustmentService.deleteStockAdjustment(id);
        return ResponseEntity.ok("Stock Adjustment deleted successfully");
    }
}
