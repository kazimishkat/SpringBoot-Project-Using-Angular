package com.mishkat.PharmacyManagement.repository;

import com.mishkat.PharmacyManagement.entity.GoodsReceivedNoteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GoodsReceivedNoteItemRepository extends JpaRepository<GoodsReceivedNoteItem, Long> {
    List<GoodsReceivedNoteItem> findByGrnId(Long grnId);
}
