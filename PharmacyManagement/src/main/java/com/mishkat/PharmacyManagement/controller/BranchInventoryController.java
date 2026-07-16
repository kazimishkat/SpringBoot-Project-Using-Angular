package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.BranchInventoryRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.BranchInventoryResponseDto;
import com.mishkat.PharmacyManagement.service.BranchInventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/branch-inventories")
@RequiredArgsConstructor
public class BranchInventoryController {
    private final BranchInventoryService branchInventoryService;

    // GET /api/branch-inventories
    @GetMapping
    public ResponseEntity<List<BranchInventoryResponseDto>> getAllInventory() {
        List<BranchInventoryResponseDto> list = branchInventoryService.getAllInventory();
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    // GET /api/branch-inventories/1
    @GetMapping("/{id}")
    public ResponseEntity<BranchInventoryResponseDto> getInventoryById(@PathVariable Long id) {
        return ResponseEntity.ok(branchInventoryService.getInventoryById(id));
    }

    // GET /api/branch-inventories/branch/3
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<BranchInventoryResponseDto>> getInventoryByBranch(@PathVariable Long branchId) {
        List<BranchInventoryResponseDto> list = branchInventoryService.getInventoryByBranch(branchId);
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    // GET /api/branch-inventories/medicine/10
    @GetMapping("/medicine/{medicineId}")
    public ResponseEntity<List<BranchInventoryResponseDto>> getInventoryByMedicine(@PathVariable Long medicineId) {
        List<BranchInventoryResponseDto> list = branchInventoryService.getInventoryByMedicine(medicineId);
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    // GET /api/branch-inventories/low-stock?threshold=15
    @GetMapping("/low-stock")
    public ResponseEntity<List<BranchInventoryResponseDto>> getLowStock(
            @RequestParam(required = false) Integer threshold) {
        List<BranchInventoryResponseDto> list = branchInventoryService.getLowStock(threshold);
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    // GET /api/branch-inventories/out-of-stock
    @GetMapping("/out-of-stock")
    public ResponseEntity<List<BranchInventoryResponseDto>> getOutOfStock() {
        List<BranchInventoryResponseDto> list = branchInventoryService.getOutOfStock();
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    // GET /api/branch-inventories/expiring?beforeDate=2026-12-31
    @GetMapping("/expiring")
    public ResponseEntity<List<BranchInventoryResponseDto>> getExpiringInventory(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beforeDate) {
        List<BranchInventoryResponseDto> list = branchInventoryService.getExpiringInventory(beforeDate);
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }
}
