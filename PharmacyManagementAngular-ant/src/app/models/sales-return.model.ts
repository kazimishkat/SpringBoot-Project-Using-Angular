import { BaseEntity } from './baseEntity.model';

// ==========================================
// Sales Return System Enums
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
// Sales Return Item Interfaces
// ==========================================
export interface SalesReturnItemRequest {
  batchId: number;
  quantity: number;
  reason?: ReturnReason;
  refundAmount?: number;
}

export interface SalesReturnItemResponse {
  id: number;
  batchId: number;
  batchNumber: string;
  medicineBrandName: string;
  quantity: number;
  reason: ReturnReason;
  refundAmount: number;
}

// ==========================================
// Sales Return Request Model (Maps to SalesReturnRequestDto)
// ==========================================
export interface SalesReturnRequest extends BaseEntity {
  returnNumber: string;
  invoiceId: number;
  returnDate: Date | string;
  status?: ApprovalStatus;
  processedById?: number;
  items: SalesReturnItemRequest[];
}

// ==========================================
// Sales Return Response Model (Maps to SalesReturnResponseDto)
// ==========================================
export interface SalesReturnResponse extends BaseEntity {
  id: number;
  returnNumber: string;
  invoiceId: number;
  invoiceNumber: string;
  returnDate: Date | string;
  status: ApprovalStatus;
  processedById?: number;
  processedByName?: string;
  items: SalesReturnItemResponse[];
}