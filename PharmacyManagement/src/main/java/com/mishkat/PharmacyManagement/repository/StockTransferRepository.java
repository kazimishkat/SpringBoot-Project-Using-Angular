package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.StockTransfer;
import com.mishkat.PharmacyManagement.enums.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {
    Optional<StockTransfer> findByTransferNumber(String transferNumber);
    List<StockTransfer> findByFromBranchId(Long fromBranchId);
    List<StockTransfer> findByToBranchId(Long toBranchId);
    List<StockTransfer> findByStatus(TransferStatus status);
    List<StockTransfer> findByRequisitionId(Long requisitionId);
}
