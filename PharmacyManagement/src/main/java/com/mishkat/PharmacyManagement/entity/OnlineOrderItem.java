package com.mishkat.PharmacyManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "online_order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlineOrderItem extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "online_order_id", nullable = false)
    private OnlineOrder onlineOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine; // 👈 কুরিয়ার বা কাউন্টারের মতো ব্যাচ নয়, সরাসরি মেডিসিন অবজেক্ট

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "price_per_unit", nullable = false)
    private Double pricePerUnit;
}
