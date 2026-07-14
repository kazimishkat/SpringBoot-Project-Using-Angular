// ==========================================
// Enums

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
// Medicine Request Model (Maps to RequestDto)
// ==========================================
export interface MedicineModelRequest extends BaseEntity {
  medicineCode: string;
  brandName: string;
  genericMedicineId: number;
  manufacturer?: string;
  dosageForm?: DosageForm;
  strength?: string;
  unitOfMeasure?: UnitOfMeasure;
  unitsPerPack?: number;
  drugSchedule?: DrugSchedule;
  storageCondition?: StorageCondition;
  reorderLevel?: number;
  reorderQuantity?: number;
  defaultPurchasePrice?: number;
  defaultSellingPrice?: number;
  vatPercentage?:number;
  description?: string;
  image?: string;
}

// ==========================================
// Medicine Response Model (Maps to ResponseDto)
// ==========================================
export interface MedicineModelResponse extends BaseEntity {
  id: number; 
  medicineCode: string;
  brandName: string;
  genericMedicineId: number;
  genericName: string;
  manufacturer: string;
  dosageForm: DosageForm;
  strength: string;
  unitOfMeasure: UnitOfMeasure;
  unitsPerPack: number;
  drugSchedule: DrugSchedule;
  storageCondition: StorageCondition;
  reorderLevel: number;
  reorderQuantity: number;
  defaultPurchasePrice: number;
  defaultSellingPrice: number;
  vatPercentage:number;
  description: string;
  isActive: boolean;
  image: string;
}