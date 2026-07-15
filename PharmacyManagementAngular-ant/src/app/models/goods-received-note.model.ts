import { BaseEntity } from './baseEntity.model';

// ==========================================
// Enums
// ==========================================
export enum ApprovalStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  CANCELLED = 'CANCELLED'
}

// ==========================================
// GRN Nested Items Models
// ==========================================
export interface GoodsReceivedNoteItemRequest {
  medicineId: number;
  batchNumber: string;
  manufactureDate?: Date | string;
  expiryDate: Date | string;
  receivedQuantity: number;
  purchasePrice: number;
  sellingPrice: number;
}

export interface GoodsReceivedNoteItemResponse {
  id: number;
  medicineId: number;
  brandName: string;
  batchNumber: string;
  manufactureDate: Date | string;
  expiryDate: Date | string;
  receivedQuantity: number;
  purchasePrice: number;
  sellingPrice: number;
}

// ==========================================
// GRN Request Model (Maps to RequestDto)
// ==========================================
export interface GoodsReceivedNoteRequest extends BaseEntity {
  grnNumber: string;
  purchaseOrderId: number;
  receivedDate: Date | string;
  receivedById?: number;
  approvalStatus?: ApprovalStatus;
  items: GoodsReceivedNoteItemRequest[];
}

// ==========================================
// GRN Response Model (Maps to ResponseDto)
// ==========================================
export interface GoodsReceivedNoteResponse extends BaseEntity {
  id: number;
  grnNumber: string;
  purchaseOrderId: number;
  poNumber: string;
  receivedDate: Date | string;
  receivedById: number;
  receivedByName: string;
  approvalStatus: ApprovalStatus;
  createdAt: Date | string;
  items: GoodsReceivedNoteItemResponse[];
}