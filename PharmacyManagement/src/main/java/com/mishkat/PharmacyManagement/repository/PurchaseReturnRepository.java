package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.PurchaseReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseReturnRepository extends JpaRepository<PurchaseReturn, Long> {
    Optional<PurchaseReturn> findByReturnNumber(String returnNumber);
    List<PurchaseReturn> findBySupplierId(Long supplierId);
    List<PurchaseReturn> findByBranchId(Long branchId);
}
