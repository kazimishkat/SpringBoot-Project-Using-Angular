package com.mishkat.PharmacyManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "requisition_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequisitionItem extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requisition_id", nullable = false)
    private Requisition requisition;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Column(name = "requested_quantity", nullable = false)
    private Integer requestedQuantity;

    @Column(name = "approved_quantity")
    private Integer approvedQuantity;

    @Column(name = "fulfilled_quantity", nullable = false)
    private Integer fulfilledQuantity = 0;
}
