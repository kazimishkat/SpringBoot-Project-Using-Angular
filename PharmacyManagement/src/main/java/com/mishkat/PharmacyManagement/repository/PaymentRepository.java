package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByInvoiceId(Long invoiceId);

    // 🔍 Search & Filtering Queries
    @Query("SELECT p FROM Payment p WHERE LOWER(p.transactionReference) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.invoice.invoiceNumber) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Payment> searchPayments(@Param("query") String query);

    @Query("SELECT p FROM Payment p WHERE " +
            "(:invoiceId IS NULL OR p.invoice.id = :invoiceId) AND " +
            "(:method IS NULL OR LOWER(p.paymentMethod) = LOWER(:method)) AND " +
            "(:startDate IS NULL OR p.paymentDate >= :startDate) AND " +
            "(:endDate IS NULL OR p.paymentDate <= :endDate)")
    List<Payment> filterPayments(@Param("invoiceId") Long invoiceId,
                                 @Param("method") String method,
                                 @Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate);
}
