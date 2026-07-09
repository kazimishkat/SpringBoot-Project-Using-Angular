import { BaseEntity } from "./baseEntity.model";

export interface MedicineCategoryRequest extends BaseEntity {
  name: string;
  description?: string;
}

// ==========================================
// Medicine Category Response Model (Maps to ResponseDto)
// ==========================================
export interface MedicineCategoryResponse extends BaseEntity {
  id: number;
  name: string;
  description: string;
  isActive: boolean;
  createdAt: Date | string;
}