import { BaseEntity } from "./baseEntity.model";

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
}