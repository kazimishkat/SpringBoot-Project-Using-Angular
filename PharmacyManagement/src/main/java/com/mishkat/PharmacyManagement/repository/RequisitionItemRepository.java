package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.RequisitionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RequisitionItemRepository extends JpaRepository<RequisitionItem, Long> {
    List<RequisitionItem> findByRequisitionId(Long requisitionId);
    List<RequisitionItem> findByMedicineId(Long medicineId);
}
