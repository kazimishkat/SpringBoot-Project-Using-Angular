package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.GoodsReceivedNoteRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.GoodsReceivedNoteResponseDto;
import com.mishkat.PharmacyManagement.enums.ApprovalStatus;
import com.mishkat.PharmacyManagement.service.GoodsReceivedNoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grn")
@RequiredArgsConstructor
public class GoodsReceivedNoteController {
    private final GoodsReceivedNoteService grnService;

    // POST /api/grns
    @PostMapping
    public ResponseEntity<GoodsReceivedNoteResponseDto> create(
            @Valid @RequestBody GoodsReceivedNoteRequestDto dto) {
        return new ResponseEntity<>(grnService.createGrn(dto), HttpStatus.CREATED);
    }

    // GET /api/grns
    @GetMapping
    public ResponseEntity<List<GoodsReceivedNoteResponseDto>> getAll() {
        List<GoodsReceivedNoteResponseDto> list = grnService.getAllGrns();
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/grns/1
    @GetMapping("/{id}")
    public GoodsReceivedNoteResponseDto getById(@PathVariable Long id) {
        return grnService.getGrnById(id);
    }

    // GET /api/grns/number/GRN-2023-001
    @GetMapping("/number/{grnNumber}")
    public GoodsReceivedNoteResponseDto getByGrnNumber(@PathVariable String grnNumber) {
        return grnService.getGrnByNumber(grnNumber);
    }

    // GET /api/grns/status/PENDING
    @GetMapping("/status/{status}")
    public ResponseEntity<List<GoodsReceivedNoteResponseDto>> getByStatus(
            @PathVariable String status) {
        try {
            ApprovalStatus approvalStatus = ApprovalStatus.valueOf(status.toUpperCase());
            List<GoodsReceivedNoteResponseDto> list = grnService.getGrnsByStatus(approvalStatus);
            return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT /api/grns/1
    @PutMapping("/{id}")
    public GoodsReceivedNoteResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody GoodsReceivedNoteRequestDto dto) {
        return grnService.updateGrn(id, dto);
    }

    // PATCH /api/grns/1/status?status=APPROVED
    @PatchMapping("/{id}/status")
    public GoodsReceivedNoteResponseDto updateStatus(
            @PathVariable Long id,
            @RequestParam ApprovalStatus status) {
        return grnService.updateApprovalStatus(id, status);
    }

    // DELETE /api/grns/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        grnService.deleteGrn(id);
        return ResponseEntity.ok("Goods Received Note deleted successfully");
    }
}
