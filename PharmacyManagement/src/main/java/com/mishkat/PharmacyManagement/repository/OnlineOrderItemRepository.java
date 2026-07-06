package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.OnlineOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OnlineOrderItemRepository extends JpaRepository<OnlineOrderItem, Long> {

    // ১. একটি নির্দিষ্ট অনলাইন অর্ডারের ভেতরে থাকা সমস্ত মেডিসিন আইটেমের লিস্ট বের করার জন্য
    // (ফার্মাসিস্ট যখন ভেরিফিকেশনের জন্য অর্ডার স্ক্রিন ওপেন করবেন, তখন এটি লাগবে)
    List<OnlineOrderItem> findByOnlineOrderId(Long onlineOrderId);

    // ২. এনালাইটিক্স বা রিপোর্টের জন্য: কোনো একটি নির্দিষ্ট মেডিসিন অনলাইনে মোট কয়বার এবং কোন কোন অর্ডারে সেল হয়েছে তা দেখতে
    List<OnlineOrderItem> findByMedicineId(Long medicineId);
}
