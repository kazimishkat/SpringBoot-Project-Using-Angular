package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.OnlineOrder;
import com.mishkat.PharmacyManagement.enums.OnlineOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OnlineOrderRepository extends JpaRepository<OnlineOrder, Long> {

    // ১. কাস্টমার যখন মোবাইল অ্যাপ বা প্রোফাইল থেকে তাঁর নিজস্ব "অর্ডার হিস্ট্রি" দেখতে চাইবেন
    List<OnlineOrder> findByCustomerIdOrderByOrderDateDesc(Long customerId);

    // ২. নির্দিষ্ট কাস্টমারের কোনো পার্টিকুলার স্ট্যাটাসের অর্ডার দেখতে (যেমন: শুধু PENDING বা DELIVERED অর্ডারগুলো)
    List<OnlineOrder> findByCustomerIdAndStatus(Long customerId, OnlineOrderStatus status);

    // ৩. ইউনিক অর্ডার নম্বর (e.g., ORD-2026-0001) দিয়ে কাস্টমার বা রাইডার যখন অর্ডার ট্র্যাক করবে
    Optional<OnlineOrder> findByOrderNumber(String orderNumber);

    // ৪. ব্রাঞ্চের ফার্মাসিস্ট যখন তাঁর নির্দিষ্ট ব্রাঞ্চের সব "পেন্ডিং অর্ডার" ভেরিফিকেশনের জন্য ড্যাশবোর্ডে দেখতে চাইবেন
    List<OnlineOrder> findByBranchIdAndStatus(Long branchId, OnlineOrderStatus status);

    // ৫. অ্যাডমিন বা ম্যানেজার যখন একটি নির্দিষ্ট ব্রাঞ্চের সব অনলাইন অর্ডার দেখতে চাইবেন
    List<OnlineOrder> findByBranchIdOrderByOrderDateDesc(Long branchId);

    // ৬. পেমেন্ট গেটওয়ে ভেরিফিকেশনের জন্য ট্রানজেকশন আইডি (Transaction ID) দিয়ে অর্ডার খোঁজা
    Optional<OnlineOrder> findByPaymentTransactionId(String paymentTransactionId);
}
