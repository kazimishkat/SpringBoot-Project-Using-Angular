import { BaseEntity } from './baseEntity.model';

// ==========================================
// Sales Invoice System Enums
// ==========================================
export enum PaymentMethod {
  CASH = 'CASH',
  CARD = 'CARD',
  BKASH = 'BKASH',
  NAGAD = 'NAGAD',
  ROCKET = 'ROCKET',
  BANK_TRANSFER = 'BANK_TRANSFER'
}

export enum InvoiceStatus {
  DRAFT = 'DRAFT',
  COMPLETED = 'COMPLETED',
  PARTIALLY_PAID = 'PARTIALLY_PAID',
  PAID = 'PAID',
  CANCELLED = 'CANCELLED',
  REFUNDED = 'REFUNDED'
}

export enum DiscountType {
  PERCENTAGE = 'PERCENTAGE',
  FIXED_AMOUNT = 'FIXED_AMOUNT'
}

// ==========================================
// Sales Invoice Nested Items Interfaces
// ==========================================
export interface SalesInvoiceItemRequest {
  batchId: number;
  quantity: number;
  unitPrice: number;
  discountType?: DiscountType;
  discountValue?: number;
}

export interface SalesInvoiceItemResponse {
  id: number;
  batchId: number;
  batchNumber: string;
  medicineBrandName: string;
  quantity: number;
  unitPrice: number;
  discountType: DiscountType;
  discountValue: number;
  taxAmount?: number;
  totalAmount?: number;
}

// ==========================================
// Sales Invoice Request Model
// ==========================================
export interface SalesInvoiceRequest extends BaseEntity {
  invoiceNumber: string;
  branchId: number;
  customerId?: number;
  prescriptionId?: number;
  soldById: number;
  invoiceDate: Date | string;
  subTotal: number;
  discountAmount?: number;
  vatAmount?: number;
  totalAmount: number;
  paidAmount: number;
  dueAmount: number;
  status: InvoiceStatus;
  paymentMethod: PaymentMethod; // 👈 [NEW]: Payment Method added
  items: SalesInvoiceItemRequest[];
  onlineOrderId?: number;
}

// ==========================================
// Sales Invoice Response Model
// ==========================================
export interface SalesInvoiceResponse extends BaseEntity {
  id: number;
  invoiceNumber: string;
  branchId: number;
  branchName: string;
  customerId?: number;
  customerName?: string;
  prescriptionId?: number;
  soldByName: string;
  invoiceDate: Date | string;
  subTotal: number;
  discountAmount: number;
  vatAmount: number;
  totalAmount: number;
  paidAmount: number;
  dueAmount: number;
  status: InvoiceStatus;
  paymentMethod?: PaymentMethod; // 👈 [NEW]: Response field added
  items: SalesInvoiceItemResponse[];
  onlineOrderId?: number;
  onlineOrderNumber?: string;
}

// ==========================================
// Payment Response DTO Map
// ==========================================
export interface PaymentResponse {
  id: number;
  paymentNumber: string;
  invoiceId: number;
  paymentDate: Date | string;
  amount: number;
  paymentMethod: string;
  status: string;
  notes?: string;
}