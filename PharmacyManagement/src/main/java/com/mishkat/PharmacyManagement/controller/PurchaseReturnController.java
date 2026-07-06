package com.mishkat.PharmacyManagement.controller;

import com.mishkat.PharmacyManagement.dto.requestDTO.PurchaseReturnRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PurchaseReturnResponseDto;
import com.mishkat.PharmacyManagement.service.PurchaseReturnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-returns")
@RequiredArgsConstructor
public class PurchaseReturnController {
    private final PurchaseReturnService returnService;

    // POST /api/purchase-returns
    @PostMapping
    public ResponseEntity<PurchaseReturnResponseDto> create(
            @Valid @RequestBody PurchaseReturnRequestDto dto) {
        return new ResponseEntity<>(returnService.createPurchaseReturn(dto), HttpStatus.CREATED);
    }

    // GET /api/purchase-returns
    @GetMapping
    public ResponseEntity<List<PurchaseReturnResponseDto>> getAll() {
        List<PurchaseReturnResponseDto> list = returnService.getAllPurchaseReturns();
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // GET /api/purchase-returns/1
    @GetMapping("/{id}")
    public PurchaseReturnResponseDto getById(@PathVariable Long id) {
        return returnService.getPurchaseReturnById(id);
    }

    // GET /api/purchase-returns/number/RET-001
    @GetMapping("/number/{returnNumber}")
    public PurchaseReturnResponseDto getByReturnNumber(@PathVariable String returnNumber) {
        return returnService.getPurchaseReturnByNumber(returnNumber);
    }

    // GET /api/purchase-returns/supplier/5
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<PurchaseReturnResponseDto>> getBySupplierId(@PathVariable Long supplierId) {
        List<PurchaseReturnResponseDto> list = returnService.getReturnsBySupplierId(supplierId);
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    // PUT /api/purchase-returns/1
    @PutMapping("/{id}")
    public PurchaseReturnResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseReturnRequestDto dto) {
        return returnService.updatePurchaseReturn(id, dto);
    }

    // DELETE /api/purchase-returns/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        returnService.deletePurchaseReturn(id);
        return ResponseEntity.ok("Purchase Return deleted successfully");
    }
}
