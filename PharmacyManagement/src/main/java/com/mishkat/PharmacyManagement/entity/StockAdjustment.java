package com.mishkat.PharmacyManagement.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Manual correction of stock at a branch — physical count, write-off of expired/damaged stock, etc. */
@Entity
@Table(name = "stock_adjustments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockAdjustment extends BaseEntity{
    @Column(name = "adjustment_number", nullable = false, unique = true, length = 30)
    private String adjustmentNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "adjustment_date", nullable = false)
    private LocalDate adjustmentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @OneToMany(mappedBy = "stockAdjustment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockAdjustmentItem> items = new ArrayList<>();
}
