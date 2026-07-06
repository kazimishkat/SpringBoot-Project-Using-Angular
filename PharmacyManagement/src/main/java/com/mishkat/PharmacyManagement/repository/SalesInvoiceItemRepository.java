package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.SalesInvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SalesInvoiceItemRepository extends JpaRepository<SalesInvoiceItem, Long> {
    List<SalesInvoiceItem> findByInvoiceId(Long invoiceId);
    List<SalesInvoiceItem> findByBatchId(Long batchId);
}
