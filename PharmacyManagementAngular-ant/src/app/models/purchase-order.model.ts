import { BaseEntity } from './baseEntity.model';

// ==========================================
// Enums
// ==========================================
export enum PurchaseOrderStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  RECEIVED = 'RECEIVED',
  CANCELLED = 'CANCELLED',
  CLOSED = 'CLOSED'
}

// ==========================================
// Purchase Order Nested Items Models
// ==========================================
export interface PurchaseOrderItemRequest {
  medicineId: number;
  orderedQuantity: number;
  unitPrice: number;
}

export interface PurchaseOrderItemResponse {
  id: number;
  medicineId: number;
  medicineCode: string;
  brandName: string;
  orderedQuantity: number;
  receivedQuantity: number;
  unitPrice: number;
}

// ==========================================
// Purchase Order Request Model (Maps to RequestDto)
// ==========================================
export interface PurchaseOrderRequest extends BaseEntity {
  poNumber: string;
  supplierId: number;
  branchId: number;
  orderDate: Date | string;
  expectedDeliveryDate?: Date | string;
  status: PurchaseOrderStatus;
  items: PurchaseOrderItemRequest[];
}

// ==========================================
// Purchase Order Response Model (Maps to ResponseDto)
// ==========================================
export interface PurchaseOrderResponse extends BaseEntity {
  id: number;
  poNumber: string;
  supplierId: number;
  supplierName: string;
  branchId: number;
  branchName: string;
  orderDate: Date | string;
  expectedDeliveryDate: Date | string;
  status: PurchaseOrderStatus;
  createdBy: string;
  createdAt: Date | string;
  items: PurchaseOrderItemResponse[];
}