package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.RequisitionRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.RequisitionResponseDto;
import com.mishkat.PharmacyManagement.enums.RequisitionStatus;
import com.mishkat.PharmacyManagement.service.RequisitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requisitions")
@RequiredArgsConstructor
public class RequisitionController {
    private final RequisitionService requisitionService;

    // POST /api/requisitions
    @PostMapping
    public ResponseEntity<RequisitionResponseDto> create(
            @Valid @RequestBody RequisitionRequestDto dto) {
        return new ResponseEntity<>(requisitionService.createRequisition(dto), HttpStatus.CREATED);
    }

    // GET /api/requisitions
    @GetMapping
    public ResponseEntity<List<RequisitionResponseDto>> getAll() {
        List<RequisitionResponseDto> list = requisitionService.getAllRequisitions();
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/requisitions/1
    @GetMapping("/{id}")
    public RequisitionResponseDto getById(@PathVariable Long id) {
        return requisitionService.getRequisitionById(id);
    }

    // GET /api/requisitions/number/REQ-001
    @GetMapping("/number/{requisitionNumber}")
    public RequisitionResponseDto getByRequisitionNumber(@PathVariable String requisitionNumber) {
        return requisitionService.getRequisitionByNumber(requisitionNumber);
    }

    // GET /api/requisitions/branch/5
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<RequisitionResponseDto>> getByBranchId(@PathVariable Long branchId) {
        List<RequisitionResponseDto> list = requisitionService.getRequisitionsByBranchId(branchId);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/requisitions/status/PENDING
    @GetMapping("/status/{status}")
    public ResponseEntity<List<RequisitionResponseDto>> getByStatus(@PathVariable RequisitionStatus status) {
        List<RequisitionResponseDto> list = requisitionService.getRequisitionsByStatus(status);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // PUT /api/requisitions/1
    @PutMapping("/{id}")
    public RequisitionResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody RequisitionRequestDto dto) {
        return requisitionService.updateRequisition(id, dto);
    }

    // PATCH /api/requisitions/1/status?status=APPROVED
    @PatchMapping("/{id}/status")
    public RequisitionResponseDto updateStatus(
            @PathVariable Long id,
            @RequestParam RequisitionStatus status) {
        return requisitionService.updateRequisitionStatus(id, status);
    }

    // DELETE /api/requisitions/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        requisitionService.deleteRequisition(id);
        return ResponseEntity.ok("Requisition deleted successfully");
    }
}
