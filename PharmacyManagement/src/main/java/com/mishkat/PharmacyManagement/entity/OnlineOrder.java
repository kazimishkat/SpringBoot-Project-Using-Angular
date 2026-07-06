package com.mishkat.PharmacyManagement.entity;

import com.mishkat.PharmacyManagement.enums.OnlineOrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "online_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlineOrder extends BaseEntity{
    @Column(name = "order_number", nullable = false, unique = true, length = 30)
    private String orderNumber; // e.g., ORD-2026-0001

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch; // কাস্টমার যে নিকটস্থ ব্রাঞ্চ সিলেক্ট করেছে

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id")
    private Prescription prescription; // ঐচ্ছিক: কাস্টমার প্রেসক্রিপশন আপলোড করলে লিংক হবে

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OnlineOrderStatus status = OnlineOrderStatus.PENDING_VERIFICATION;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate = LocalDateTime.now();

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "payment_status", nullable = false, length = 20)
    private String paymentStatus; // e.g., PAID, PENDING, FAILED (Gateway থেকে আসবে)

    @Column(name = "payment_transaction_id", length = 100)
    private String paymentTransactionId;

    @Column(name = "delivery_address", nullable = false, columnDefinition = "TEXT")
    private String deliveryAddress;

    @OneToMany(mappedBy = "onlineOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OnlineOrderItem> items = new ArrayList<>();

    @OneToOne(mappedBy = "onlineOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DeliveryAssignment deliveryAssignment;
}
