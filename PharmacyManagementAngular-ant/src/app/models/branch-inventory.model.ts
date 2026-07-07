import { BaseEntity } from "./baseEntity.model";

export interface BranchInventoryModel extends BaseEntity {
  branchId: number;
  branchName?: string;         // Populated by your Response DTO mapping
  batchId: number;
  batchNumber?: string;       // Populated by your Response DTO mapping
  medicineBrandName?: string; // Populated by your Response DTO mapping
  quantityOnHand: number;
  quantityReserved: number;   // Tracked dynamically on the server side
}