package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.GoodsReceivedNoteRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.GoodsReceivedNoteResponseDto;
import com.mishkat.PharmacyManagement.enums.ApprovalStatus;

import java.util.List;

public interface GoodsReceivedNoteService {
    GoodsReceivedNoteResponseDto createGrn(GoodsReceivedNoteRequestDto dto);

    List<GoodsReceivedNoteResponseDto> getAllGrns();

    GoodsReceivedNoteResponseDto getGrnById(Long id);

    GoodsReceivedNoteResponseDto getGrnByNumber(String grnNumber);

    List<GoodsReceivedNoteResponseDto> getGrnsByStatus(ApprovalStatus status);

    GoodsReceivedNoteResponseDto updateGrn(Long id, GoodsReceivedNoteRequestDto dto);

    void deleteGrn(Long id);

    GoodsReceivedNoteResponseDto updateApprovalStatus(Long id, ApprovalStatus status);
}
