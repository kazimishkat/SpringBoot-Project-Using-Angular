package com.mishkat.PharmacyManagement.dto.requestDTO;

import com.mishkat.PharmacyManagement.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class MedicineRequestDto {
    @NotBlank(message = "Medicine code is required")
    @Size(max = 30)
    private String medicineCode;

    @NotBlank(message = "Brand name is required")
    @Size(max = 150)
    private String brandName;

    @NotNull(message = "Generic medicine identity is required")
    private Long genericMedicineId;

    @Size(max = 150)
    private String manufacturer;

    private DosageForm dosageForm;
    private String strength;
    private UnitOfMeasure unitOfMeasure;
    private Integer unitsPerPack;
    private DrugSchedule drugSchedule;
    private StorageCondition storageCondition;
    private Integer reorderLevel;
    private Integer reorderQuantity;

    @Positive(message = "Default purchase price must be positive")
    private BigDecimal defaultPurchasePrice;

    @Positive(message = "Default selling price must be positive")
    private BigDecimal defaultSellingPrice;
}
