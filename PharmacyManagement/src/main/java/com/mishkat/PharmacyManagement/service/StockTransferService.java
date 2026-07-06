package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.StockTransferItemRequestDto;
import com.mishkat.PharmacyManagement.dto.requestDTO.StockTransferRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.StockTransferResponseDto;
import com.mishkat.PharmacyManagement.enums.TransferStatus;

import java.util.List;

public interface StockTransferService {
    // ── Stock Transfer management ──────────────────────────────────
    StockTransferResponseDto createTransfer(StockTransferRequestDto dto);

    List<StockTransferResponseDto> getAll();

    StockTransferResponseDto getById(Long id);

    StockTransferResponseDto getByTransferNumber(String transferNumber);

    List<StockTransferResponseDto> getTransfersFromBranch(Long branchId);

    List<StockTransferResponseDto> getTransfersToBranch(Long branchId);

    List<StockTransferResponseDto> getTransfersByStatus(TransferStatus status);

    // ── Transit Workflow operations ─────────────────────────────────
    StockTransferResponseDto updateTransferStatus(Long id, TransferStatus status, Long userId);

    StockTransferResponseDto updateReceivedQuantities(Long id, List<StockTransferItemRequestDto> itemUpdates, Long receivedById);
}
