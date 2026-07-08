import { BaseEntity } from "./baseEntity.model";

export interface MedicineCategoryModel extends BaseEntity {
  name: string;
  description?: string;
}