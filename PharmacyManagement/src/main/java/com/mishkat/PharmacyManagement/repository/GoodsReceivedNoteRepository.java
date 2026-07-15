package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.GoodsReceivedNote;
import com.mishkat.PharmacyManagement.enums.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GoodsReceivedNoteRepository extends JpaRepository<GoodsReceivedNote, Long> {
    Optional<GoodsReceivedNote> findByGrnNumber(String grnNumber);
    List<GoodsReceivedNote> findByApprovalStatus(ApprovalStatus approvalStatus);
    List<GoodsReceivedNote> findByPurchaseOrderId(Long purchaseOrderId);
}
