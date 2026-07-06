package com.mishkat.PharmacyManagement.entity;

import com.mishkat.PharmacyManagement.enums.BranchType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "branches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Branch extends BaseEntity{

    @Column(name = "branch_code", nullable = false, unique = true, length = 20)
    private String branchCode;

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "branch_type", nullable = false, length = 20)
    private BranchType branchType;

    @Embedded
    private Address address;

    private String phone;

    private String email;

    @Column(name = "license_number", length = 100)
    private String licenseNumber;

    @Column(name = "manager_name", length = 100)
    private String managerName;
}
