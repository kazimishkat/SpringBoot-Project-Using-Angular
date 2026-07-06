package com.mishkat.PharmacyManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity // Child table detailing content items inside transit manifests
@Data // Auto-builds necessary access mechanisms via Lombok attributes
@Table(name = "stock_transfer_items") // Houses asset entries inside 'stock_transfer_items' data container
@AllArgsConstructor
@NoArgsConstructor
public class StockTransferItem extends  BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stock_transfer_id", nullable = false)
    private StockTransfer stockTransfer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "batch_id", nullable = false)
    private MedicineBatch batch;

    @Column(name = "sent_quantity", nullable = false)
    private Integer sentQuantity;

    @Column(name = "received_quantity")
    private Integer receivedQuantity;
}
