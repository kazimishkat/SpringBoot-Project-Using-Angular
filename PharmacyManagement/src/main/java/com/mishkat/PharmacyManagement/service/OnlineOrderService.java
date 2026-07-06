package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.OnlineOrderRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.OnlineOrderResponseDto;
import com.mishkat.PharmacyManagement.enums.OnlineOrderStatus;

import java.util.List;

public interface OnlineOrderService {
    // ১. কাস্টমার যখন অ্যাপ থেকে নতুন অর্ডার প্লেস করবে (পেমেন্ট গেটওয়ে ট্রানজেকশন আইডি সহ)
    OnlineOrderResponseDto placeOrder(OnlineOrderRequestDto dto, Long customerId, String transactionId);

    // ২. কাস্টমার অ্যাপে তাঁর নিজস্ব আলাদা "অর্ডার হিস্ট্রি" দেখার জন্য
    List<OnlineOrderResponseDto> getOrderHistoryByCustomerId(Long customerId);

    // ৩. ইউনিক অর্ডার নম্বর দিয়ে লাইভ ট্র্যাকিং চেক করার জন্য
    OnlineOrderResponseDto getOrderByNumber(String orderNumber);

    // ৪. ফার্মাসিস্ট তাঁর ব্রাঞ্চের সমস্ত নতুন পেন্ডিং অর্ডার ভেরিফাই করার জন্য ড্যাশবোর্ডে টানবেন
    List<OnlineOrderResponseDto> getPendingOrdersByBranch(Long branchId);

    // ৫. ফার্মাসিস্ট বা ম্যানেজার যখন অর্ডারের বর্তমান অবস্থা পরিবর্তন করবেন (e.g., CONFIRMED, READY_FOR_PICKUP)
    OnlineOrderResponseDto updateOrderStatus(Long orderId, OnlineOrderStatus status);
}
