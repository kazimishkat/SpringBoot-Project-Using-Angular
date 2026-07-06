package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.DeliveryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment, Long> {

    // ১. থার্ড-পার্টি ডেলিভারি কোম্পানি থেকে পাওয়া ট্র্যাকিং নাম্বার দিয়ে অ্যাসাইনমেন্টটি খুঁজে বের করার জন্য
    // (কুরিয়ার কোম্পানির Webhook যখন ডেলিভারি স্ট্যাটাস আপডেট করবে তখন এটি মোস্ট ইম্পর্ট্যান্ট)
    Optional<DeliveryAssignment> findByTrackingNumber(String trackingNumber);

    // ২. একটি নির্দিষ্ট অনলাইন অর্ডারের ডেলিভারি ও রাইডার ডিটেইলস দেখার জন্য
    Optional<DeliveryAssignment> findByOnlineOrderId(Long onlineOrderId);

    // ৩. একটি নির্দিষ্ট ডেলিভারি কোম্পানির আন্ডারে থাকা সব পার্সেলের ট্র্যাকিং লিস্ট দেখার জন্য
    List<DeliveryAssignment> findByDeliveryCompanyId(Long deliveryCompanyId);

    // ৪. লাইভ স্ট্যাটাস অনুযায়ী পার্সেল ফিল্টার করার জন্য (যেমন: কতগুলো পার্সেল এখন IN_TRANSIT বা PICKED_UP অবস্থায় আছে)
    List<DeliveryAssignment> findByDeliveryStatus(String deliveryStatus);
}
