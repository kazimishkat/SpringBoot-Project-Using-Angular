package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.MedicineBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicineBatchRepository extends JpaRepository<MedicineBatch, Long> {
    List<MedicineBatch> findByMedicineId(Long medicineId);
    List<MedicineBatch> findByBatchNumber(String batchNumber);

    // Find batches expiring before a specific date (e.g., next 3 months)
    List<MedicineBatch> findByExpiryDateBeforeAndIsActiveTrue(LocalDate date);
}
