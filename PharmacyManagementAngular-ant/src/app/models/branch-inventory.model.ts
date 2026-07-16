import { BaseEntity } from './baseEntity.model';

// ==========================================
// Branch Inventory Request Model (Maps to RequestDto)
// ==========================================
export interface BranchInventoryRequest extends BaseEntity {
  branchId: number;
  batchId: number;
  quantityOnHand: number;
}

// ==========================================
// Branch Inventory Response Model (Maps to ResponseDto)
// ==========================================
export interface BranchInventoryResponse extends BaseEntity {
  id: number;
  branchId: number;
  branchName: string;
  batchId: number;
  batchNumber: string;
  medicineBrandName: string;
  quantityOnHand: number;
  quantityReserved: number;
  
  // Real-time stock status fields requested for detail view mappings
  genericName?: string;
  categoryName?: string;
  supplierName?: string;
  reorderLevel?: number;
  expiryDate?: Date | string;
  purchasePrice?: number;
  sellingPrice?: number;
  lastUpdated?: Date | string;
}