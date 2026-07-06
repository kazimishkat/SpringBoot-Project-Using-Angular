package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.PurchaseReturnItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PurchaseReturnItemRepository extends JpaRepository<PurchaseReturnItem, Long> {
    List<PurchaseReturnItem> findByPurchaseReturnId(Long purchaseReturnId);
}
