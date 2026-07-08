import { BaseEntity } from "./baseEntity.model";


export interface GenericMedicineModel extends BaseEntity {
  genericName: string;
  categoryId?: number;     // Maps to categoryId from Request/Response DTO
  categoryName?: string;   // Flattened property explicitly returned in your Response DTO
  description?: string;
  indication?: string;
  sideEffects?: string;
  contraindications?: string;
}