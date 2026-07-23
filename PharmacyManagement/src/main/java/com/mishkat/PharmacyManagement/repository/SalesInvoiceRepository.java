package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.SalesInvoice;
import com.mishkat.PharmacyManagement.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesInvoiceRepository extends JpaRepository<SalesInvoice, Long> {
    Optional<SalesInvoice> findByInvoiceNumber(String invoiceNumber);
    List<SalesInvoice> findByBranchId(Long branchId);
    List<SalesInvoice> findByCustomerId(Long customerId);
    List<SalesInvoice> findByStatus(InvoiceStatus status);
    List<SalesInvoice> findByDueAmountGreaterThan(java.math.BigDecimal dueAmount);
    Optional<SalesInvoice> findByOnlineOrderId(Long onlineOrderId);

    // 🔍 Search & Filtering Queries
    @Query("SELECT i FROM SalesInvoice i WHERE LOWER(i.invoiceNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(i.customer.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<SalesInvoice> searchInvoices(@Param("query") String query);

    @Query("SELECT i FROM SalesInvoice i WHERE " +
            "(:customerId IS NULL OR i.customer.id = :customerId) AND " +
            "(:status IS NULL OR i.status = :status) AND " +
            "(:startDate IS NULL OR i.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR i.createdAt <= :endDate)")
    List<SalesInvoice> filterInvoices(@Param("customerId") Long customerId,
                                      @Param("status") InvoiceStatus status,
                                      @Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);
}
