package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.SalesInvoice;
import com.mishkat.PharmacyManagement.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesInvoiceRepository extends JpaRepository<SalesInvoice, Long> {
    Optional<SalesInvoice> findByInvoiceNumber(String invoiceNumber);
    List<SalesInvoice> findByBranchId(Long branchId);
    List<SalesInvoice> findByCustomerId(Long customerId);
    List<SalesInvoice> findByStatus(InvoiceStatus status);

    // Find invoices where the customer still owes money
    List<SalesInvoice> findByDueAmountGreaterThan(java.math.BigDecimal dueAmount);
    // ── 🟢 নতুন যুক্ত করা হলো: অনলাইন অর্ডার আইডি দিয়ে ইনভয়েস ট্র্যাকিংয়ের জন্য ──
    Optional<SalesInvoice> findByOnlineOrderId(Long onlineOrderId);
}
