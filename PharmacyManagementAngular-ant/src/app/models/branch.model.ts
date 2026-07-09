
import { Address } from './address.model';
import { BaseEntity } from './baseEntity.model';

// Enum matching your Java BranchType enum
export enum BranchType {
  MAIN = 'MAIN',
  SUB = 'SUB'
}

export interface BranchRequest extends BaseEntity {
  branchCode: string;
  name: string;
  branchType: BranchType;
  address: Address;
  phone?: string;
  email?: string;
  licenseNumber?: string;
  managerName?: string;
}

// ==========================================
// Branch Response Model (Maps to ResponseDto)
// ==========================================
export interface BranchResponse extends BaseEntity {
  id: number;
  branchCode: string;
  name: string;
  branchType: BranchType;
  address: Address;
  phone: string;
  email: string;
  licenseNumber: string;
  managerName: string;
  isActive: boolean;
  createdAt: Date | string;
  updatedAt: Date | string;
}