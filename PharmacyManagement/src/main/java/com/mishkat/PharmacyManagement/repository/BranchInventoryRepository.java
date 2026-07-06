package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.BranchInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BranchInventoryRepository extends JpaRepository<BranchInventory, Long> {
    Optional<BranchInventory> findByBranchIdAndBatchId(Long branchId, Long batchId);
    List<BranchInventory> findByBranchId(Long branchId);

    // Get total stock of a specific medicine across all batches in a specific branch
    @Query("SELECT SUM(bi.quantityOnHand) FROM BranchInventory bi WHERE bi.branch.id = :branchId AND bi.batch.medicine.id = :medicineId")
    Integer getTotalQuantityByBranchAndMedicine(@Param("branchId") Long branchId, @Param("medicineId") Long medicineId);

    Long branchId(Long branchId);
}
