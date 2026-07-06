package com.mishkat.PharmacyManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "delivery_companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCompany extends BaseEntity{
    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName; // e.g., Pathao, Paperfly, Steadfast

    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "api_key", length = 255)
    private String apiKey; // ডেলিভারি কোম্পানির API ইন্টিগ্রেশনের জন্য টোকেন

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
