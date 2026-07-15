import { BaseEntity } from './baseEntity.model';

// ==========================================
// Delivery Company Request Model (Maps to RequestDto)
// ==========================================
export interface DeliveryCompanyRequest extends BaseEntity {
  companyName: string;
  contactPerson?: string;
  phoneNumber: string;
  apiKey?: string;
}

// ==========================================
// Delivery Company Response Model (Maps to ResponseDto)
// ==========================================
export interface DeliveryCompanyResponse extends BaseEntity {
  id: number;
  companyName: string;
  contactPerson: string;
  phoneNumber: string;
  isActive: boolean;
}