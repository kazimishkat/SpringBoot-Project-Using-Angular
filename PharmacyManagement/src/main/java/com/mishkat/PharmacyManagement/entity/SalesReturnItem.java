package com.mishkat.PharmacyManagement.entity;

import com.mishkat.PharmacyManagement.enums.ReturnReason;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "sales_return_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesReturnItem extends  BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sales_return_id", nullable = false)
    private SalesReturn salesReturn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "batch_id", nullable = false)
    private MedicineBatch batch;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private ReturnReason reason;

    @Column(name = "refund_amount", precision = 12, scale = 2)
    private BigDecimal refundAmount;
}
