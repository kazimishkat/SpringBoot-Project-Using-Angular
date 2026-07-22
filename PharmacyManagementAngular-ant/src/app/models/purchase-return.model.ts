import { BaseEntity } from './baseEntity.model';

// ==========================================
// Enums Matching Backend Specification
// ==========================================
export enum ReturnReason {
  EXPIRED = 'EXPIRED',
  DAMAGED = 'DAMAGED',
  WRONG_ITEM_DELIVERED = 'WRONG_ITEM_DELIVERED',
  CUSTOMER_DISSATISFACTION = 'CUSTOMER_DISSATISFACTION',
  OVERSTOCK = 'OVERSTOCK',
  QUALITY_ISSUE = 'QUALITY_ISSUE',
  OTHER = 'OTHER'
}

export enum ApprovalStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  CANCELLED = 'CANCELLED'
}

// ==========================================
// Purchase Return Nested Items Interfaces
// ==========================================
export interface PurchaseReturnItemRequest {
  batchId: number;
  quantity: number;
  reason?: ReturnReason;
  creditAmount?: number;
  medicineId?: number; // Visual mapping helper
}

export interface PurchaseReturnItemResponse {
  id: number;
  batchId: number;
  batchNumber: string;
  medicineBrandName: string;
  quantity: number;
  reason: ReturnReason;
  creditAmount: number;
}

// ==========================================
// Purchase Return Request Model (Maps to PurchaseReturnRequestDto)
// ==========================================
export interface PurchaseReturnRequest extends BaseEntity {
  returnNumber: string;
  supplierId: number;
  branchId: number;
  returnDate: Date | string;
  approvalStatus?: ApprovalStatus;
  items: PurchaseReturnItemRequest[];
}

// ==========================================
// Purchase Return Response Model (Maps to PurchaseReturnResponseDto)
// ==========================================
export interface PurchaseReturnResponse extends BaseEntity {
  id: number;
  returnNumber: string;
  supplierId: number;
  supplierName: string;
  branchId: number;
  branchName: string;
  returnDate: Date | string;
  createdBy: string;
  approvalStatus?: ApprovalStatus;
  items: PurchaseReturnItemResponse[];
}