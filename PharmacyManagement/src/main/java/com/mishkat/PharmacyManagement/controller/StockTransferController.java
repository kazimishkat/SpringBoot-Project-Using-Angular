package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.StockTransferItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.StockTransferRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.StockTransferResponseDto;
import com.mishkat.PharmacyManagement.enums.TransferStatus;
import com.mishkat.PharmacyManagement.service.StockTransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-transfers")
@RequiredArgsConstructor
public class StockTransferController {
    private final StockTransferService stockTransferService;

    // ── Stock Transfer management (Admin / Store Keeper / Branch Manager) ──

    // POST /api/stock-transfers
    @PostMapping
    public ResponseEntity<StockTransferResponseDto> createTransfer(@Valid @RequestBody StockTransferRequestDto dto) {
        return new ResponseEntity<>(stockTransferService.createTransfer(dto), HttpStatus.CREATED);
    }

    // GET /api/stock-transfers
    @GetMapping
    public ResponseEntity<List<StockTransferResponseDto>> getAll() {
        List<StockTransferResponseDto> list = stockTransferService.getAll();
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/stock-transfers/1
    @GetMapping("/{id}")
    public ResponseEntity<StockTransferResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(stockTransferService.getById(id));
    }

    // GET /api/stock-transfers/number/TRF-100293
    @GetMapping("/number/{transferNumber}")
    public ResponseEntity<StockTransferResponseDto> getByTransferNumber(@PathVariable String transferNumber) {
        return ResponseEntity.ok(stockTransferService.getByTransferNumber(transferNumber));
    }

    // GET /api/stock-transfers/from-branch/2
    @GetMapping("/from-branch/{branchId}")
    public ResponseEntity<List<StockTransferResponseDto>> getTransfersFromBranch(@PathVariable Long branchId) {
        List<StockTransferResponseDto> list = stockTransferService.getTransfersFromBranch(branchId);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/stock-transfers/to-branch/5
    @GetMapping("/to-branch/{branchId}")
    public ResponseEntity<List<StockTransferResponseDto>> getTransfersToBranch(@PathVariable Long branchId) {
        List<StockTransferResponseDto> list = stockTransferService.getTransfersToBranch(branchId);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/stock-transfers/status/PENDING
    @GetMapping("/status/{status}")
    public ResponseEntity<List<StockTransferResponseDto>> getTransfersByStatus(@PathVariable String status) {
        TransferStatus transferStatus;
        try {
            transferStatus = TransferStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        List<StockTransferResponseDto> list = stockTransferService.getTransfersByStatus(transferStatus);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // ── Transit Workflow operations (Store Keeper / Branch Manager Verification) ──

    // PATCH /api/stock-transfers/1/status?status=DISPATCHED&userId=3
    @PatchMapping("/{id}/status")
    public ResponseEntity<StockTransferResponseDto> updateTransferStatus(
            @PathVariable Long id,
            @RequestParam TransferStatus status,
            @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(stockTransferService.updateTransferStatus(id, status, userId));
    }

    // PUT /api/stock-transfers/1/receive?receivedById=4
    // Body: [{ "batchId": 12, "sentQuantity": 50 }, ...]
    @PutMapping("/{id}/receive")
    public ResponseEntity<StockTransferResponseDto> receiveStockItems(
            @PathVariable Long id,
            @RequestParam Long receivedById,
            @RequestBody List<StockTransferItemRequestDto> itemUpdates) {
        return ResponseEntity.ok(stockTransferService.updateReceivedQuantities(id, itemUpdates, receivedById));
    }
}
