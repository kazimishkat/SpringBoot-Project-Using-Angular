package com.mishkat.PharmacyManagement.entity;
import com.mishkat.PharmacyManagement.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales_returns")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesReturn extends BaseEntity{

    @Column(name = "return_number", nullable = false, unique = true, length = 30)
    private String returnNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private SalesInvoice invoice;

    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    // 🌟 [NEW]: অ্যাপ্রুভাল ওয়ার্কফ্লো হ্যান্ডেল করার জন্য স্ট্যাটাস ফিল্ড
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private ApprovalStatus status = ApprovalStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by")
    private User processedBy;

    @OneToMany(mappedBy = "salesReturn", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SalesReturnItem> items = new ArrayList<>();
}
