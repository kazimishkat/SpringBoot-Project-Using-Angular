package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.SalesInvoiceRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.SalesInvoiceResponseDto;
import com.mishkat.PharmacyManagement.enums.InvoiceStatus;

import java.math.BigDecimal;
import java.util.List;

public interface SalesInvoiceService {
    SalesInvoiceResponseDto createInvoice(SalesInvoiceRequestDto dto);

    List<SalesInvoiceResponseDto> getAllInvoices();

    SalesInvoiceResponseDto getInvoiceById(Long id);

    SalesInvoiceResponseDto getInvoiceByNumber(String invoiceNumber);

    List<SalesInvoiceResponseDto> getInvoicesByBranchId(Long branchId);

    List<SalesInvoiceResponseDto> getInvoicesByCustomerId(Long customerId);

    List<SalesInvoiceResponseDto> getInvoicesByStatus(InvoiceStatus status);

    List<SalesInvoiceResponseDto> getDueInvoices(BigDecimal minimumDue);

    SalesInvoiceResponseDto updateInvoice(Long id, SalesInvoiceRequestDto dto);

    SalesInvoiceResponseDto updateInvoiceStatus(Long id, InvoiceStatus status);

    void deleteInvoice(Long id);
}
