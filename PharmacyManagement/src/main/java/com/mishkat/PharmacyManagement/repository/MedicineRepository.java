package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    Optional<Medicine> findByMedicineCode(String medicineCode);
    List<Medicine> findByBrandNameContainingIgnoreCase(String brandName);

    // Find medicines that need to be reordered
    @Query("SELECT m FROM Medicine m JOIN MedicineBatch mb JOIN BranchInventory bi ON bi.batch = mb WHERE m = mb.medicine GROUP BY m HAVING SUM(bi.quantityOnHand) <= m.reorderLevel")
    List<Medicine> findLowStockMedicines();
}
