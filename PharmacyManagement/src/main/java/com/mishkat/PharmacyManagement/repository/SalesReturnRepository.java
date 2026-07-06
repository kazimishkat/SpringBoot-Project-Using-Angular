package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.SalesReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesReturnRepository extends JpaRepository<SalesReturn, Long> {
    Optional<SalesReturn> findByReturnNumber(String returnNumber);
    List<SalesReturn> findByInvoiceId(Long invoiceId);
}
