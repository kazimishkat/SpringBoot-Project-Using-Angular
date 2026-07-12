package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.BranchRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.BranchResponseDto;
import com.mishkat.PharmacyManagement.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {
    private final BranchService branchService;

    // ── Branch Management ──────────────────────────────────────────

    // POST /api/branches
    @PostMapping
    public ResponseEntity<BranchResponseDto> create(
            @Valid @RequestBody BranchRequestDto dto) {
        return new ResponseEntity<>(branchService.createBranch(dto), HttpStatus.CREATED);
    }

    // GET /api/branches
    @GetMapping
    public ResponseEntity<List<BranchResponseDto>> getAll() {
        List<BranchResponseDto> list = branchService.getAllBranches();
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // Added: GET /api/branches/search?name=Dhanmondi
    @GetMapping("/search")
    public ResponseEntity<List<BranchResponseDto>> searchByName(@RequestParam String name) {
        List<BranchResponseDto> list = branchService.searchBranchesByName(name);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/branches/active
    @GetMapping("/active")
    public ResponseEntity<List<BranchResponseDto>> getActiveBranches() {
        List<BranchResponseDto> list = branchService.getActiveBranches();
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/branches/1
    @GetMapping("/{id}")
    public BranchResponseDto getById(@PathVariable Long id) {
        return branchService.getBranchById(id);
    }

    // GET /api/branches/code/BR-001
    @GetMapping("/code/{branchCode}")
    public BranchResponseDto getByCode(@PathVariable String branchCode) {
        return branchService.getBranchByCode(branchCode);
    }

    // PUT /api/branches/1
    @PutMapping("/{id}")
    public BranchResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody BranchRequestDto dto) {
        return branchService.updateBranch(id, dto);
    }

    // DELETE /api/branches/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}