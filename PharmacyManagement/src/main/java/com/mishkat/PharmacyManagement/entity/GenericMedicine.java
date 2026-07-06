package com.mishkat.PharmacyManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "generic_medicines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericMedicine extends BaseEntity{
    @Column(name = "generic_name", nullable = false, unique = true, length = 150)
    private String genericName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private MedicineCategory category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String indication;

    @Column(name = "side_effects", columnDefinition = "TEXT")
    private String sideEffects;

    @Column(name = "contraindications", columnDefinition = "TEXT")
    private String contraindications;

    @OneToMany(mappedBy = "genericMedicine")
    private Set<Medicine> medicines = new HashSet<>();

}
