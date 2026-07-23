package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByPhone(String phone);
    Optional<Customer> findByEmail(String email);
    List<Customer> findByIsActiveTrue();

    // ── নতুন অনলাইন রেজিস্ট্রেশন ও লগইনের জন্য প্রয়োজনীয় মেথড ──
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    // 🔍 [NEW]: ইনভয়েস থেকে কাস্টমারের নাম বা ফোন নম্বর দিয়ে খোঁজার জন্য JPQL Query
    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR c.phone LIKE CONCAT('%', :query, '%')")
    List<Customer> searchCustomers(@Param("query") String query);
}
