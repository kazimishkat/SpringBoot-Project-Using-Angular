package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.SalesReturn;
import com.mishkat.PharmacyManagement.enums.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesReturnRepository extends JpaRepository<SalesReturn, Long> {
    Optional<SalesReturn> findByReturnNumber(String returnNumber);
    List<SalesReturn> findByInvoiceId(Long invoiceId);

    // 🔍 Search & Filtering Queries
    @Query("SELECT r FROM SalesReturn r WHERE LOWER(r.returnNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(r.invoice.invoiceNumber) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<SalesReturn> searchReturns(@Param("query") String query);

    @Query("SELECT r FROM SalesReturn r WHERE " +
            "(:invoiceId IS NULL OR r.invoice.id = :invoiceId) AND " +
            "(:status IS NULL OR r.status = :status) AND " +
            "(:startDate IS NULL OR r.returnDate >= :startDate) AND " +
            "(:endDate IS NULL OR r.returnDate <= :endDate)")
    List<SalesReturn> filterReturns(@Param("invoiceId") Long invoiceId,
                                    @Param("status") ApprovalStatus status,
                                    @Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate);
}
