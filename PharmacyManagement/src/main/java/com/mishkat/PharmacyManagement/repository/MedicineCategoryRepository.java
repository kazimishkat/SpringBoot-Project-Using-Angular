package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.MedicineCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MedicineCategoryRepository extends JpaRepository<MedicineCategory, Long> {
    Optional<MedicineCategory> findByNameIgnoreCase(String name);
}
