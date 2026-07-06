package com.mishkat.PharmacyManagement.dto.mapper;

import com.mishkat.PharmacyManagement.dto.requestDTO.PaymentRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.PaymentResponseDto;
import com.mishkat.PharmacyManagement.entity.Payment;

public class PaymentMapper {
    // =========================================================================
    // 1. Entity থেকে Response DTO তে রূপান্তর (Client-কে ডাটা দেখানোর জন্য)
    // =========================================================================

    public PaymentResponseDto toDTO(Payment entity) {
        if (entity == null) return null;

        PaymentResponseDto dto = new PaymentResponseDto();

        dto.setId(entity.getId());
        dto.setPaymentDate(entity.getPaymentDate());
        dto.setAmount(entity.getAmount());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setPaymentStatus(entity.getPaymentStatus());
        dto.setCreatedAt(entity.getCreatedAt()); // BaseEntity থেকে টাইমস্ট্যাম্প নেওয়া হচ্ছে

        // [গুরুত্বপূর্ণ]: এন্টিটির transactionReference কে DTO এর transactionNumber-এ ম্যাপ করা হচ্ছে
        dto.setTransactionNumber(entity.getTransactionReference());

        // [নাল-চেক]: Lazy Loading-এর ক্ষেত্রে NullPointerException এড়ানোর জন্য Invoice চেক করা হচ্ছে
        if (entity.getInvoice() != null) {
            dto.setInvoiceId(entity.getInvoice().getId());
            dto.setInvoiceNumber(entity.getInvoice().getInvoiceNumber());
        }

        return dto;
    }

    // =========================================================================
    // 2. Request DTO থেকে Entity তে রূপান্তর (ডাটাবেজে ডাটা সেভ বা আপডেট করার জন্য)
    // =========================================================================

    public Payment toEntity(PaymentRequestDto dto) {
        if (dto == null) return null;

        Payment entity = new Payment();

        entity.setPaymentDate(dto.getPaymentDate());
        entity.setAmount(dto.getAmount());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setPaymentStatus(dto.getPaymentStatus());

        // [বিশেষ দ্রষ্টব্য]: DTO-তে থাকা invoiceId ব্যবহার করে সরাসরি এন্টিটিতে রিলেশন তৈরি করা যায় না।
        // আপনাকে PaymentService ক্লাসে SalesInvoiceRepository.findById(dto.getInvoiceId()) কল করে
        // আসল SalesInvoice অবজেক্টটি ডাটাবেজ থেকে বের করে এনে এই entity.setInvoice(...) এর মাধ্যমে সেট করতে হবে।

        // [নোট]: Request DTO তে transactionReference/Number ফিল্ড নেই। এটি সাধারণত
        // পেমেন্ট গেটওয়ের রেসপন্স থেকে Service Layer-এ জেনারেট বা সেট করা হয়।

        return entity;
    }
}
