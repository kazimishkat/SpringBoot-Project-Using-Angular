package com.mishkat.PharmacyManagement.entity;

import com.mishkat.PharmacyManagement.enums.StockMovementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

@Entity // Establishes a read-only immutable core audit log ledger tracking all inventory changes
@Data // Standard Lombok utility integration tool configuration
@Table(name = "stock_movements") // Maps schema definition into central inventory ledger table 'stock_movements'
@AllArgsConstructor
@NoArgsConstructor
public class StockMovement extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "batch_id", nullable = false)
    private MedicineBatch batch;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 30)
    private StockMovementType movementType;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "movement_date", nullable = false)
    private LocalDateTime movementDate;

    private String remarks;
}
