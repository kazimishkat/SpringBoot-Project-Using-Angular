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

    // POST /api/grn
    @PostMapping
    public ResponseEntity<GoodsReceivedNoteResponseDto> receiveGoods(
            @Valid @RequestBody GoodsReceivedNoteRequestDto dto) {
        return new ResponseEntity<>(grnService.receiveGoods(dto), HttpStatus.CREATED);
    }

    // GET /api/grn
    @GetMapping
    public ResponseEntity<List<GoodsReceivedNoteResponseDto>> getAllGrns() {
        List<GoodsReceivedNoteResponseDto> list = grnService.getAllGrns();
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/grn/1
    @GetMapping("/{id}")
    public GoodsReceivedNoteResponseDto getGrnById(@PathVariable Long id) {
        return grnService.getGrnById(id);
    }

    // GET /api/grn/number/GRN-2023-001
    @GetMapping("/number/{grnNumber}")
    public GoodsReceivedNoteResponseDto getByGrnNumber(@PathVariable String grnNumber) {
        return grnService.getGrnByNumber(grnNumber);
    }

    // GET /api/grn/purchase-order/5
    @GetMapping("/purchase-order/{purchaseOrderId}")
    public ResponseEntity<List<GoodsReceivedNoteResponseDto>> getGrnByPurchaseOrder(@PathVariable Long purchaseOrderId) {
        List<GoodsReceivedNoteResponseDto> list = grnService.getGrnByPurchaseOrder(purchaseOrderId);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/grn/status/PENDING
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

    // GET /api/grn/1/print
    @GetMapping("/{id}/print")
    public ResponseEntity<GoodsReceivedNoteResponseDto> printGrn(@PathVariable Long id) {
        return ResponseEntity.ok(grnService.printGrn(id));
    }

    // PUT /api/grn/1
    @PutMapping("/{id}")
    public GoodsReceivedNoteResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody GoodsReceivedNoteRequestDto dto) {
        return grnService.updateGrn(id, dto);
    }

    // PATCH /api/grn/1/status?status=APPROVED
    @PatchMapping("/{id}/status")
    public GoodsReceivedNoteResponseDto updateStatus(
            @PathVariable Long id,
            @RequestParam ApprovalStatus status) {
        return grnService.updateApprovalStatus(id, status);
    }

    // ── 🟢 নতুন যুক্ত করা হলো: PATCH /api/grn/1/cancel ──
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<GoodsReceivedNoteResponseDto> cancelGrn(@PathVariable Long id) {
        return ResponseEntity.ok(grnService.cancelGrn(id));
    }
}
