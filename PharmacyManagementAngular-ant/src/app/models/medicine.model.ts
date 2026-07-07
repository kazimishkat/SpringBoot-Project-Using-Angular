

// ==========================================
// Domain Enums matching Spring Boot Enums

import { BaseEntity } from "./baseEntity.model";

// ==========================================
export enum DosageForm {
  TABLET = 'TABLET',
  CAPSULE = 'CAPSULE',
  SYRUP = 'SYRUP',
  SUSPENSION = 'SUSPENSION',
  INJECTION = 'INJECTION',
  OINTMENT = 'OINTMENT',
  CREAM = 'CREAM',
  GEL = 'GEL',
  LOTION = 'LOTION',
  DROPS = 'DROPS',
  INHALER = 'INHALER',
  SUPPOSITORY = 'SUPPOSITORY',
  POWDER = 'POWDER',
  PATCH = 'PATCH',
  SPRAY = 'SPRAY',
  SACHET = 'SACHET'
}

export enum DrugSchedule {
  OTC = 'OTC',
  PRESCRIPTION_REQUIRED = 'PRESCRIPTION_REQUIRED',
  CONTROLLED_SUBSTANCE = 'CONTROLLED_SUBSTANCE',
  NARCOTIC = 'NARCOTIC'
}

export enum UnitOfMeasure {
  PIECE = 'PIECE',
  STRIP = 'STRIP',
  BOX = 'BOX',
  BOTTLE = 'BOTTLE',
  VIAL = 'VIAL',
  TUBE = 'TUBE',
  SACHET = 'SACHET',
  AMPOULE = 'AMPOULE',
  JAR = 'JAR'
}

export enum StorageCondition {
  ROOM_TEMPERATURE = 'ROOM_TEMPERATURE',
  REFRIGERATED = 'REFRIGERATED',
  FROZEN = 'FROZEN',
  COLD_CHAIN = 'COLD_CHAIN',
  PROTECT_FROM_LIGHT = 'PROTECT_FROM_LIGHT',
  PROTECT_FROM_MOISTURE = 'PROTECT_FROM_MOISTURE'
}

// ==========================================
// Main Medicine Model
// ==========================================
export interface MedicineModel extends BaseEntity {
  medicineCode: string;
  brandName: string;
  genericMedicineId: number;   // Maps to Long in DTO
  genericName?: string;         // Present in your MedicineResponseDto
  manufacturer?: string;
  dosageForm?: DosageForm | string;
  strength?: string;
  unitOfMeasure?: UnitOfMeasure | string;
  unitsPerPack?: number;
  drugSchedule?: DrugSchedule | string;
  storageCondition?: StorageCondition | string;
  reorderLevel?: number;
  reorderQuantity?: number;
  defaultPurchasePrice?: number; // Java BigDecimal maps to number in TypeScript
  defaultSellingPrice?: number;  // Java BigDecimal maps to number in TypeScript
  vatPercentage?: number;        // From your core Entity definitions
  description?: string;
  image?: string;
}