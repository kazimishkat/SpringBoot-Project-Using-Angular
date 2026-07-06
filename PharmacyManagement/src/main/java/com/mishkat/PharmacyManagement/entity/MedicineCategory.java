package com.mishkat.PharmacyManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "medicine_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineCategory extends BaseEntity{
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
}
