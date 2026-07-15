package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.GoodsReceivedNoteRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.GoodsReceivedNoteResponseDto;
import com.mishkat.PharmacyManagement.enums.ApprovalStatus;

import java.util.List;

public interface GoodsReceivedNoteService {
    GoodsReceivedNoteResponseDto receiveGoods(GoodsReceivedNoteRequestDto dto);

    List<GoodsReceivedNoteResponseDto> getAllGrns();

    GoodsReceivedNoteResponseDto getGrnById(Long id);

    GoodsReceivedNoteResponseDto getGrnByNumber(String grnNumber);

    List<GoodsReceivedNoteResponseDto> getGrnsByStatus(ApprovalStatus status);

    List<GoodsReceivedNoteResponseDto> getGrnByPurchaseOrder(Long purchaseOrderId);

    GoodsReceivedNoteResponseDto updateGrn(Long id, GoodsReceivedNoteRequestDto dto);

    // ── 🟢 deleteGrn-এর পরিবর্তে cancelGrn যুক্ত করা হলো ──
    GoodsReceivedNoteResponseDto cancelGrn(Long id);

    GoodsReceivedNoteResponseDto updateApprovalStatus(Long id, ApprovalStatus status);

    GoodsReceivedNoteResponseDto printGrn(Long id);
}
