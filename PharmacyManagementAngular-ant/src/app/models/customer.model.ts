import { BaseEntity } from './baseEntity.model';
import { Address } from "./address.model";

// ==========================================
// Customer System Enums
// ==========================================
export enum Gender {
  MALE = 'MALE',
  FEMALE = 'FEMALE',
  OTHER = 'OTHER'
}



// ==========================================
// Customer Request Model (Maps to CustomerRequestDto)
// ==========================================
export interface CustomerRequest extends BaseEntity {
  name: string;
  phone: string;
  email?: string;
  gender?: Gender;
  age?: number;
  address?: Address;
  
  // 🟢 Online Account Registration Flags & Credentials
  createAccount?: boolean;
  username?: string;
  password?: string;
}

// ==========================================
// Customer Response Model (Maps to CustomerResponseDto)
// ==========================================
export interface CustomerResponse extends BaseEntity {
  id: number;
  name: string;
  phone: string;
  email: string;
  gender?: Gender;
  age?: number;
  address?: Address;
  loyaltyPoints: number;
  isActive: boolean;
  image?: string;

  // 🟢 Online Customer Account Mapping Fields
  userId?: number;
  username?: string;
  accountCreated?: boolean;
}