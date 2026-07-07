
import { Address } from './address.model';
import { BaseEntity } from './baseEntity.model';

// Enum matching your Java BranchType enum
export enum BranchType {
  MAIN = 'MAIN',
  SUB = 'SUB'
}

export interface BranchModel extends BaseEntity {
  branchCode: string;
  name: string;
  branchType: BranchType | string; // Handled as string enum
  phone?: string;
  email?: string;
  address?: Address;               // Embedded object mapping
  licenseNumber?: string;
  managerName?: string;
}