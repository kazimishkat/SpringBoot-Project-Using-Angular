package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.SalesInvoiceRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PaymentResponseDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.SalesInvoiceResponseDto;
import com.mishkat.PharmacyManagement.enums.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    // 🌟 [NEW] Added for Angular Support
    SalesInvoiceResponseDto cancelInvoice(Long id);
    List<SalesInvoiceResponseDto> searchInvoices(String query);
    List<SalesInvoiceResponseDto> filterInvoices(Long customerId, InvoiceStatus status, LocalDateTime startDate, LocalDateTime endDate);
    List<PaymentResponseDto> getInvoicePayments(Long invoiceId);
    byte[] printInvoicePdf(Long id);
}
