package com.mishkat.PharmacyManagement.entity;

import com.mishkat.PharmacyManagement.enums.TransferStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity // Sets database framework for managing internal point-to-point shipments
@Data // Lombok tool automation application settings
@Table(name = "stock_transfers") // Binds parent configurations directly inside 'stock_transfers' table
@AllArgsConstructor
@NoArgsConstructor
public class StockTransfer extends  BaseEntity{
    @Column(name = "transfer_number", nullable = false, unique = true, length = 30)
    private String transferNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requisition_id")
    private Requisition requisition;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_branch_id", nullable = false)
    private Branch fromBranch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_branch_id", nullable = false)
    private Branch toBranch;

    @Column(name = "transfer_date", nullable = false)
    private LocalDate transferDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TransferStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispatched_by")
    private User dispatchedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_by")
    private User receivedBy;

    @OneToMany(mappedBy = "stockTransfer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockTransferItem> items = new ArrayList<>();
}
