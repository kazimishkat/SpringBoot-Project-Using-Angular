package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.PurchaseReturnRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PurchaseReturnResponseDto;

import java.util.List;

public interface PurchaseReturnService {
    PurchaseReturnResponseDto createPurchaseReturn(PurchaseReturnRequestDto dto);

    List<PurchaseReturnResponseDto> getAllPurchaseReturns();

    PurchaseReturnResponseDto getPurchaseReturnById(Long id);

    PurchaseReturnResponseDto getPurchaseReturnByNumber(String returnNumber);

    List<PurchaseReturnResponseDto> getReturnsBySupplierId(Long supplierId);

    PurchaseReturnResponseDto updatePurchaseReturn(Long id, PurchaseReturnRequestDto dto);

    void deletePurchaseReturn(Long id);
    PurchaseReturnResponseDto approvePurchaseReturn(Long id);
}
