package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.SalesReturnItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SalesReturnItemRepository extends JpaRepository<SalesReturnItem, Long> {
    List<SalesReturnItem> findBySalesReturnId(Long salesReturnId);
    List<SalesReturnItem> findByBatchId(Long batchId);
}
