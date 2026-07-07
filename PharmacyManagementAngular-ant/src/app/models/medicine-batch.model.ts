import { BaseEntity } from "./baseEntity.model";

export interface MedicineBatchModel extends BaseEntity {
  medicineId: number;       // Maps to Long from Request DTO
  batchNumber: string;
  supplierId?: number;      // Optional Long from Request DTO
  manufactureDate?: string | Date; 
  expiryDate: string | Date;
  purchasePrice: number;    // Java BigDecimal maps to number
  sellingPrice: number;     // Java BigDecimal maps to number
  
  // Optional flattened display properties if your response DTO expands them later
  medicineName?: string;
  supplierName?: string;
}