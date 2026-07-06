package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.BranchInventoryRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.BranchInventoryResponseDto;
import com.mishkat.PharmacyManagement.service.BranchInventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branch-inventories")
@RequiredArgsConstructor
public class BranchInventoryController {
    private final BranchInventoryService branchInventoryService;

    // ── Inventory Management ──────────────────────────────────────────

    // POST /api/branch-inventories
    @PostMapping
    public ResponseEntity<BranchInventoryResponseDto> create(
            @Valid @RequestBody BranchInventoryRequestDto dto) {
        return new ResponseEntity<>(branchInventoryService.createInventory(dto), HttpStatus.CREATED);
    }

    // GET /api/branch-inventories
    @GetMapping
    public ResponseEntity<List<BranchInventoryResponseDto>> getAll() {
        List<BranchInventoryResponseDto> list = branchInventoryService.getAllInventories();
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/branch-inventories/1
    @GetMapping("/{id}")
    public BranchInventoryResponseDto getById(@PathVariable Long id) {
        return branchInventoryService.getInventoryById(id);
    }

    // GET /api/branch-inventories/branch/3 — all inventories at a specific branch
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<BranchInventoryResponseDto>> getByBranch(@PathVariable Long branchId) {
        List<BranchInventoryResponseDto> list = branchInventoryService.getInventoriesByBranch(branchId);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/branch-inventories/branch/1/batch/5 — specific inventory by branch and batch
    @GetMapping("/branch/{branchId}/batch/{batchId}")
    public BranchInventoryResponseDto getByBranchAndBatch(
            @PathVariable Long branchId,
            @PathVariable Long batchId) {
        return branchInventoryService.getInventoryByBranchAndBatch(branchId, batchId);
    }

    // GET /api/branch-inventories/branch/1/medicine/10/total — total quantity of a medicine in a branch
    @GetMapping("/branch/{branchId}/medicine/{medicineId}/total")
    public ResponseEntity<Integer> getTotalQuantityByBranchAndMedicine(
            @PathVariable Long branchId,
            @PathVariable Long medicineId) {
        Integer total = branchInventoryService.getTotalQuantityByBranchAndMedicine(branchId, medicineId);
        return ResponseEntity.ok(total);
    }

    // PUT /api/branch-inventories/1
    @PutMapping("/{id}")
    public BranchInventoryResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody BranchInventoryRequestDto dto) {
        return branchInventoryService.updateInventory(id, dto);
    }

    // DELETE /api/branch-inventories/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        branchInventoryService.deleteInventory(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}
