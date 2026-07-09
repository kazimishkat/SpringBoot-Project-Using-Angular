import { BaseEntity } from "./baseEntity.model";

// Medicine Batch Request Model (Maps to RequestDto)
// ==========================================
export interface MedicineBatchRequest extends BaseEntity {
  medicineId: number;
  batchNumber: string;
  supplierId?: number;
  manufactureDate?: Date | string; // Maps to LocalDate
  expiryDate: Date | string;       // Maps to LocalDate
  purchasePrice: number;           // Maps to BigDecimal
  sellingPrice: number;            // Maps to BigDecimal
}

// ==========================================
// Medicine Batch Response Model (Maps to ResponseDto)
// ==========================================
export interface MedicineBatchResponse extends BaseEntity {
  id: number;
  medicineId: number;
  brandName: string;
  batchNumber: string;
  supplierId: number;
  supplierName: string;
  manufactureDate: Date | string;
  expiryDate: Date | string;
  purchasePrice: number;
  sellingPrice: number;
  isActive: boolean;
}