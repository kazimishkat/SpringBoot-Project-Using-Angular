package com.mishkat.PharmacyManagement.entity;

import com.mishkat.PharmacyManagement.enums.DosageForm;
import com.mishkat.PharmacyManagement.enums.DrugSchedule;
import com.mishkat.PharmacyManagement.enums.StorageCondition;
import com.mishkat.PharmacyManagement.enums.UnitOfMeasure;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;


@Entity
@Table(name = "medicines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicine extends BaseEntity{
    @Column(name = "medicine_code", nullable = false, unique = true, length = 30)
    private String medicineCode;

    @Column(name = "brand_name", nullable = false, length = 150)
    private String brandName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "generic_medicine_id", nullable = false)
    private GenericMedicine genericMedicine;

    @Column(length = 150)
    private String manufacturer;

    @Enumerated(EnumType.STRING)
    @Column(name = "dosage_form", length = 30)
    private DosageForm dosageForm;

    @Column(length = 50)
    private String strength;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_of_measure", length = 20)
    private UnitOfMeasure unitOfMeasure;

    @Column(name = "units_per_pack")
    private Integer unitsPerPack;

    @Enumerated(EnumType.STRING)
    @Column(name = "drug_schedule", length = 30)
    private DrugSchedule drugSchedule;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage_condition", length = 30)
    private StorageCondition storageCondition;

    @Column(name = "reorder_level")
    private Integer reorderLevel;

    @Column(name = "reorder_quantity")
    private Integer reorderQuantity;

    @Column(name = "default_purchase_price", precision = 12, scale = 2)
    private BigDecimal defaultPurchasePrice;

    @Column(name = "default_selling_price", precision = 12, scale = 2)
    private BigDecimal defaultSellingPrice;

    @Column(name = "vat_percentage", precision = 5, scale = 2)
    private BigDecimal vatPercentage;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String image;

}
