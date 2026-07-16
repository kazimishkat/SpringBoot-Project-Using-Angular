package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.BranchInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BranchInventoryRepository extends JpaRepository<BranchInventory, Long> {
    Optional<BranchInventory> findByBranchIdAndBatchId(Long branchId, Long batchId);
    List<BranchInventory> findByBranchId(Long branchId);

    // Medicine ID এর ওপর ভিত্তি করে ইনভেন্টরি কুয়েরি
    @Query("SELECT bi FROM BranchInventory bi WHERE bi.batch.medicine.id = :medicineId")
    List<BranchInventory> findByMedicineId(@Param("medicineId") Long medicineId);

    // Low Stock ফিল্টার করার কুয়েরি
    List<BranchInventory> findByQuantityOnHandLessThanEqual(Integer threshold);

    // Expiring Inventory ফিল্টার করার কাস্টম কুয়েরি (MedicineBatch এর expiryDate এর ওপর ভিত্তি করে)
    @Query("SELECT bi FROM BranchInventory bi WHERE bi.batch.expiryDate <= :date")
    List<BranchInventory> findByBatchExpiryDateBefore(@Param("date") LocalDate date);
}
