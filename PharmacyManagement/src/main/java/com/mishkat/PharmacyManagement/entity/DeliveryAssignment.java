package com.mishkat.PharmacyManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAssignment extends BaseEntity{
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "online_order_id", nullable = false)
    private OnlineOrder onlineOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_company_id", nullable = false)
    private DeliveryCompany deliveryCompany; // কোন কোম্পানি ডেলিভারি করছে (যেমন: Pathao)

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber; // থার্ড-পার্টি কোম্পানি থেকে পাওয়া কনসাইনমেন্ট/ট্র্যাকিং আইডি

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt = LocalDateTime.now();

    @Column(name = "delivery_status", length = 30)
    private String deliveryStatus; // e.g., REQUESTED, PICKED_UP, IN_TRANSIT, DELIVERED

    @Column(columnDefinition = "TEXT")
    private String remarks;
}
