package com.mishkat.PharmacyManagement.entity;

import com.mishkat.PharmacyManagement.enums.AdjustmentReason;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "stock_adjustment_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockAdjustmentItem extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stock_adjustment_id", nullable = false)
    private StockAdjustment stockAdjustment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "batch_id", nullable = false)
    private MedicineBatch batch;

    @Column(name = "quantity_before")
    private Integer quantityBefore;

    @Column(name = "quantity_after")
    private Integer quantityAfter;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private AdjustmentReason reason;

    @Column(columnDefinition = "TEXT")
    private String remarks;
}
