import { BaseEntity } from './baseEntity.model';

// ==========================================
// Enums
// ==========================================
export enum StockMovementType {
  PURCHASE_RECEIVED = 'PURCHASE_RECEIVED',
  TRANSFER_IN = 'TRANSFER_IN',
  TRANSFER_OUT = 'TRANSFER_OUT',
  SALE = 'SALE',
  SALE_RETURN = 'SALE_RETURN',
  PURCHASE_RETURN = 'PURCHASE_RETURN',
  ADJUSTMENT_INCREASE = 'ADJUSTMENT_INCREASE',
  ADJUSTMENT_DECREASE = 'ADJUSTMENT_DECREASE',
  EXPIRED_WRITE_OFF = 'EXPIRED_WRITE_OFF',
  DAMAGED_WRITE_OFF = 'DAMAGED_WRITE_OFF'
}

// ==========================================
// Stock Movement Request Model (Maps to RequestDto)
// ==========================================
export interface StockMovementRequest extends BaseEntity {
  branchId: number;
  batchId: number;
  movementType: StockMovementType;
  quantity: number;
  referenceType?: string;
  referenceId?: number;
}

// ==========================================
// Stock Movement Response Model (Maps to ResponseDto)
// ==========================================
export interface StockMovementResponse extends BaseEntity {
  id: number;
  branchId: number;
  branchName: string;
  batchId: number;
  batchNumber: string;
  medicineName: string;
  movementType: StockMovementType;
  quantity: number;
  referenceType: string;
  referenceId: number;
  createdAt: Date | string;
  createdBy: string;
}