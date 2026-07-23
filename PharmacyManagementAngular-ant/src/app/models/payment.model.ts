import { BaseEntity } from './baseEntity.model';

// ==========================================
// Payment System Enums
// ==========================================
export enum PaymentMethod {
  CASH = 'CASH',
  CARD = 'CARD',
  MOBILE_BANKING = 'MOBILE_BANKING',
  BANK_TRANSFER = 'BANK_TRANSFER',
  CHEQUE = 'CHEQUE',
  OTHER = 'OTHER'
}

export enum PaymentStatus {
  PENDING = 'PENDING',
  COMPLETED = 'COMPLETED',
  FAILED = 'FAILED',
  REFUNDED = 'REFUNDED',
  CANCELLED = 'CANCELLED'
}

// ==========================================
// Payment Request Model (Maps to PaymentRequestDto)
// ==========================================
export interface PaymentRequest extends BaseEntity {
  invoiceId: number;
  paymentDate: Date | string;
  amount: number;
  paymentMethod: PaymentMethod;
  paymentStatus?: PaymentStatus;
}

// ==========================================
// Payment Response Model (Maps to PaymentResponseDto)
// ==========================================
export interface PaymentResponse extends BaseEntity {
  id: number;
  invoiceId: number;
  invoiceNumber: string;
  paymentDate: Date | string;
  amount: number;
  paymentMethod: PaymentMethod;
  paymentStatus: PaymentStatus;
  transactionNumber?: string;
  createdAt: Date | string;
}