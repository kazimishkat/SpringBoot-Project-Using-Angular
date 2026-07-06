package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.RequisitionRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.RequisitionResponseDto;
import com.mishkat.PharmacyManagement.enums.RequisitionStatus;

import java.util.List;

public interface RequisitionService {
    RequisitionResponseDto createRequisition(RequisitionRequestDto dto);

    List<RequisitionResponseDto> getAllRequisitions();

    RequisitionResponseDto getRequisitionById(Long id);

    RequisitionResponseDto getRequisitionByNumber(String requisitionNumber);

    List<RequisitionResponseDto> getRequisitionsByBranchId(Long branchId);

    List<RequisitionResponseDto> getRequisitionsByStatus(RequisitionStatus status);

    RequisitionResponseDto updateRequisition(Long id, RequisitionRequestDto dto);

    RequisitionResponseDto updateRequisitionStatus(Long id, RequisitionStatus status);

    void deleteRequisition(Long id);
}
