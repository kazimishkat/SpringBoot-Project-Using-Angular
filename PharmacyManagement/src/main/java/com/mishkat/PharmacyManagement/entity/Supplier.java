package com.mishkat.PharmacyManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity // Marks this as a JPA persistence table entity
@Data // Automates encapsulation patterns via Lombok
@Table(name = "suppliers") // Configures database destination table
@AllArgsConstructor
@NoArgsConstructor
public class Supplier extends BaseEntity{

    @Column(name = "supplier_code", nullable = false, unique = true, length = 20)
    private String supplierCode;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    private String phone;

    private String email;

    @Embedded
    private Address address;

    @Column(name = "trade_license_no", length = 100)
    private String tradeLicenseNo;

    @Column(name = "tax_id", length = 100)
    private String taxId;
}
