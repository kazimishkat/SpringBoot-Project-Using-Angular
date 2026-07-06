package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.GenericMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GenericMedicineRepository extends JpaRepository<GenericMedicine, Long> {
    Optional<GenericMedicine> findByGenericNameIgnoreCase(String genericName);

}
