package com.mishkat.PharmacyManagement.dto.responseDTO;

import com.mishkat.PharmacyManagement.enums.DosageForm;
import com.mishkat.PharmacyManagement.enums.DrugSchedule;
import com.mishkat.PharmacyManagement.enums.StorageCondition;
import com.mishkat.PharmacyManagement.enums.UnitOfMeasure;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MedicineResponseDto {
    private Long id;
    private String medicineCode;
    private String brandName;
    private Long genericMedicineId;
    private String genericName;
    private String manufacturer;
    private DosageForm dosageForm;
    private String strength;
    private UnitOfMeasure unitOfMeasure;
    private Integer unitsPerPack;
    private DrugSchedule drugSchedule;
    private StorageCondition storageCondition;
    private Integer reorderLevel;
    private Integer reorderQuantity;
    private BigDecimal defaultPurchasePrice;
    private BigDecimal defaultSellingPrice;
    private Boolean isActive;
    private String image;
}
