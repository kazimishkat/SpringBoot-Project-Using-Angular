

// ==========================================
// Enums

import { Address } from "./address.model";
import { BaseEntity } from "./baseEntity.model";

// ==========================================
export enum BranchType {
  BRANCH = 'BRANCH',
  CENTRAL_HUB = 'CENTRAL_HUB',
  MAIN= 'MAIN',
  SUB='SUB'
}

// ==========================================
// Branch Request Model (Maps to RequestDto)
// ==========================================
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
  address?: Address;
  phone: string;
  email: string;
  licenseNumber: string;
  managerName: string;
  isActive: boolean;
  createdAt: Date | string;
  updatedAt: Date | string;
}