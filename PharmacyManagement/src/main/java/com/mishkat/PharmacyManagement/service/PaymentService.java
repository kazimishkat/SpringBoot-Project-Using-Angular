package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.PaymentRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PaymentResponseDto;

import java.util.List;

public interface PaymentService {
    PaymentResponseDto createPayment(PaymentRequestDto dto);

    List<PaymentResponseDto> getAllPayments();

    PaymentResponseDto getPaymentById(Long id);

    List<PaymentResponseDto> getPaymentsByInvoiceId(Long invoiceId);

    void deletePayment(Long id);
}
