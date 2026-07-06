package com.mishkat.PharmacyManagement.entity;

import com.mishkat.PharmacyManagement.enums.RequisitionPriority;
import com.mishkat.PharmacyManagement.enums.RequisitionStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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


@Entity
@Table(name = "requisitions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Requisition extends BaseEntity{
    @Column(name = "requisition_number", nullable = false, unique = true, length = 30)
    private String requisitionNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "requisition_date", nullable = false)
    private LocalDate requisitionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RequisitionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RequisitionPriority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by")
    private User requestedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @OneToMany(mappedBy = "requisition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequisitionItem> items = new ArrayList<>();
}
