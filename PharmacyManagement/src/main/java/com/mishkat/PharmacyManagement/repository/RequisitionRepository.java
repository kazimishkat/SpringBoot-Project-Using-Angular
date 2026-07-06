package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.Requisition;
import com.mishkat.PharmacyManagement.enums.RequisitionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequisitionRepository extends JpaRepository<Requisition, Long> {
    Optional<Requisition> findByRequisitionNumber(String requisitionNumber);
    List<Requisition> findByBranchId(Long branchId);
    List<Requisition> findByStatus(RequisitionStatus status);
}
