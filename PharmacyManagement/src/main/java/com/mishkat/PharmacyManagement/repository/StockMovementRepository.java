package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.StockMovement;
import com.mishkat.PharmacyManagement.enums.StockMovementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findByBranchId(Long branchId);
    List<StockMovement> findByBatchId(Long batchId);
    List<StockMovement> findByBranchIdAndMovementType(Long branchId, StockMovementType movementType);
    List<StockMovement> findByReferenceTypeAndReferenceId(String referenceType, Long referenceId);
}
