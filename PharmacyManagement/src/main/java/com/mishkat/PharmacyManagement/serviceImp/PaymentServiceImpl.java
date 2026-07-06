package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.PaymentMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.PaymentRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PaymentResponseDto;
import com.mishkat.PharmacyManagement.entity.Payment;
import com.mishkat.PharmacyManagement.entity.SalesInvoice;
import com.mishkat.PharmacyManagement.repository.PaymentRepository;
import com.mishkat.PharmacyManagement.repository.SalesInvoiceRepository;
import com.mishkat.PharmacyManagement.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final SalesInvoiceRepository salesInvoiceRepository;

    // ম্যাপারের মেথডগুলো স্ট্যাটিক না হওয়ায় ইনস্ট্যান্স তৈরি করা হলো
    private final PaymentMapper mapper = new PaymentMapper();

    @Override
    @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto dto) {
        Payment payment = mapper.toEntity(dto);

        // SalesInvoice অবজেক্ট ডেটাবেস থেকে খুঁজে সেট করা
        SalesInvoice invoice = salesInvoiceRepository.findById(dto.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Sales Invoice not found with id: " + dto.getInvoiceId()));
        payment.setInvoice(invoice);

        // Transaction Reference অটো-জেনারেট করা হচ্ছে (যেহেতু DTO তে নেই)
        payment.setTransactionReference("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        Payment savedPayment = paymentRepository.save(payment);
        return mapper.toDTO(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        return mapper.toDTO(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getPaymentsByInvoiceId(Long invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        paymentRepository.delete(payment);
    }
}
