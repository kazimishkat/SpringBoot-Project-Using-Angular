import { BaseEntity } from "./baseEntity.model";


// ==========================================
// Generic Medicine Request Model (Maps to RequestDto)
// ==========================================
export interface GenericMedicineRequest extends BaseEntity {
  genericName: string;
  categoryId?: number;
  description?: string;
  indication?: string;
  sideEffects?: string;
  contraindications?: string;
}

// ==========================================
// Generic Medicine Response Model (Maps to ResponseDto)
// ==========================================
export interface GenericMedicineResponse extends BaseEntity {
  id: number;
  genericName: string;
  categoryId: number;
  categoryName: string;
  description: string;
  indication: string;
  sideEffects: string;
  contraindications: string;
  isActive: boolean;
}