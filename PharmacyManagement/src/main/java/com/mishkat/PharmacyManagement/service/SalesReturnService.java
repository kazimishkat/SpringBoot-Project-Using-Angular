package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.SalesReturnRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.SalesReturnResponseDto;
import com.mishkat.PharmacyManagement.enums.ApprovalStatus;

import java.time.LocalDate;
import java.util.List;

public interface SalesReturnService {
    SalesReturnResponseDto createSalesReturn(SalesReturnRequestDto dto);
    List<SalesReturnResponseDto> getAllSalesReturns();
    SalesReturnResponseDto getSalesReturnById(Long id);
    SalesReturnResponseDto getSalesReturnByNumber(String returnNumber);
    List<SalesReturnResponseDto> getSalesReturnsByInvoiceId(Long invoiceId);
    SalesReturnResponseDto updateSalesReturn(Long id, SalesReturnRequestDto dto);
    void deleteSalesReturn(Long id);

    // 🌟 [NEW] Approval Workflow & Search/Filter
    SalesReturnResponseDto approveReturn(Long id);
    SalesReturnResponseDto rejectReturn(Long id);
    List<SalesReturnResponseDto> searchReturns(String query);
    List<SalesReturnResponseDto> filterReturns(Long invoiceId, ApprovalStatus status, LocalDate startDate, LocalDate endDate);
}
