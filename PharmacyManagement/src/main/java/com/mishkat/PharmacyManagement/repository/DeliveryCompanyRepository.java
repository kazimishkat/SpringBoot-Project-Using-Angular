package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.DeliveryCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryCompanyRepository extends JpaRepository<DeliveryCompany, Long> {

    // ১. শুধুমাত্র সচল (Active) ডেলিভারি কোম্পানিগুলোর লিস্ট বের করার জন্য
    // (ব্রাঞ্চ ম্যানেজার যখন রাইডার কল করার জন্য ড্রপডাউন ওপেন করবেন তখন এটি লাগবে)
    List<DeliveryCompany> findByIsActiveTrue();

    // ২. কোম্পানির নাম দিয়ে নির্দিষ্ট কুরিয়ার প্রোফাইল খুঁজে বের করার জন্য
    Optional<DeliveryCompany> findByCompanyNameIgnoreCase(String companyName);
}

