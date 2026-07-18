import { BaseEntity } from './baseEntity.model';

// ==========================================
// Enums matching backend capabilities
// ==========================================
export enum AdjustmentReason {
  PHYSICAL_COUNT_DISCREPANCY = 'PHYSICAL_COUNT_DISCREPANCY',
  EXPIRED_STOCK = 'EXPIRED_STOCK',
  DAMAGED_STOCK = 'DAMAGED_STOCK',
  THEFT_OR_LOSS = 'THEFT_OR_LOSS',
  DATA_ENTRY_ERROR = 'DATA_ENTRY_ERROR'
}

export enum AdjustmentStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  CANCELLED = 'CANCELLED'
}

// ==========================================
// Stock Adjustment Nested Items Models
// ==========================================
export interface StockAdjustmentItemRequest {
  batchId: number;
  quantityBefore: number;
  quantityAfter: number;
  reason: AdjustmentReason;
  remarks?: string;
}

export interface StockAdjustmentItemResponse {
  id: number;
  batchId: number;
  batchNumber: string;
  medicineBrandName: string;
  quantityBefore: number;
  quantityAfter: number;
  reason: AdjustmentReason;
  remarks: string;
}

// ==========================================
// Stock Adjustment Request Model (Maps to RequestDto)
// ==========================================
export interface StockAdjustmentRequest extends BaseEntity {
  adjustmentNumber: string;
  branchId: number;
  adjustmentDate: Date | string;
  approvedById?: number;
  status?: AdjustmentStatus; // Track workflow contexts locally
  items: StockAdjustmentItemRequest[];
}

// ==========================================
// Stock Adjustment Response Model (Maps to ResponseDto)
// ==========================================
export interface StockAdjustmentResponse extends BaseEntity {
  id: number;
  adjustmentNumber: string;
  branchId: number;
  branchName: string;
  adjustmentDate: Date | string;
  approvedBy: string;
  status?: AdjustmentStatus; // Maps localized system workflow flags
  items: StockAdjustmentItemResponse[];
}