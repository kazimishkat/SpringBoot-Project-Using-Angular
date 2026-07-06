package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.StockAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockAdjustmentRepository extends JpaRepository<StockAdjustment, Long> {
    Optional<StockAdjustment> findByAdjustmentNumber(String adjustmentNumber);
    List<StockAdjustment> findByBranchId(Long branchId);
}
