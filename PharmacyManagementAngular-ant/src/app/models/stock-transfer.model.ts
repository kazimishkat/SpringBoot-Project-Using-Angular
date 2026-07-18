import { BaseEntity } from './baseEntity.model';

// ==========================================
// Enums matching backend workflow properties
// ==========================================
export enum TransferStatus {
  PENDING = 'PENDING',
  DISPATCHED = 'DISPATCHED',
  RECEIVED = 'RECEIVED',
  CANCELLED = 'CANCELLED'
}

// ==========================================
// Stock Transfer Nested Items Models
// ==========================================
export interface StockTransferItemRequest {
  batchId: number;
  sentQuantity: number;
  medicineId?: number; // UI integration helper
}

export interface StockTransferItemResponse {
  id: number;
  batchId: number;
  batchNumber: string;
  medicineBrandName: string;
  sentQuantity: number;
  receivedQuantity?: number;
}

// ==========================================
// Stock Transfer Request Model (Maps to RequestDto)
// ==========================================
export interface StockTransferRequest extends BaseEntity {
  transferNumber: string;
  requisitionId?: number;
  fromBranchId: number;
  toBranchId: number;
  transferDate: Date | string;
  status: TransferStatus;
  dispatchedById?: number;
  items: StockTransferItemRequest[];
}

// ==========================================
// Stock Transfer Response Model (Maps to ResponseDto)
// ==========================================
export interface StockTransferResponse extends BaseEntity {
  id: number;
  transferNumber: string;
  requisitionId: number;
  requisitionNumber: string;
  fromBranchId: number;
  fromBranchName: string;
  toBranchId: number;
  toBranchName: string;
  transferDate: Date | string;
  status: TransferStatus;
  dispatchedBy: string;
  items: StockTransferItemResponse[];
}