import { BaseEntity } from './baseEntity.model';

// ==========================================
// Medicine Batch Request Model (Maps to RequestDto)
// ==========================================
export interface MedicineBatchRequest extends BaseEntity {
  medicineId: number;
  batchNumber: string;
  supplierId?: number;
  manufactureDate?: Date | string;
  expiryDate: Date | string;
  purchasePrice: number;
  sellingPrice: number;
}

// ==========================================
// Medicine Batch Response Model (Maps to ResponseDto)
// ==========================================
export interface MedicineBatchResponse extends BaseEntity {
  id: number;
  medicineId: number;
  brandName: string;
  genericName?: string; // Mapped for deep details validation
  medicineName?: string; // Mapped for fallback lookup structures
  batchNumber: string;
  supplierId: number;
  supplierName: string;
  manufactureDate: Date | string;
  expiryDate: Date | string;
  purchasePrice: number;
  sellingPrice: number;
  isActive: boolean;
  
  // Real-time stock status components requested for layout view
  quantityOnHand?: number;
  quantityReserved?: number;
  createdAt?: Date | string;
  createdBy?: string;
}