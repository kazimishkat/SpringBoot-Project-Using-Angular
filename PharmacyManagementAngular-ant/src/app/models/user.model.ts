import { BaseEntity } from './baseEntity.model';

// ==========================================
// User System Enums
// ==========================================
export enum UserRole {
  SUPER_ADMIN = 'SUPER_ADMIN',
  CUSTOMER = 'CUSTOMER',
  CENTRAL_ADMIN = 'CENTRAL_ADMIN',
  BRANCH_MANAGER = 'BRANCH_MANAGER',
  PHARMACIST = 'PHARMACIST',
  SALESMAN = 'SALESMAN',
  ACCOUNTANT = 'ACCOUNTANT',
  STOCK_KEEPER = 'STOCK_KEEPER'
}

// ==========================================
// User Request Model (Maps to UserRequestDto)
// ==========================================
export interface UserRequest extends BaseEntity {
  username: string;
  password?: string; // Optional during specific profiles updates patches
  fullName: string;
  email: string;
  phone?: string;
  role: UserRole;
  branchId?: number;
}

// ==========================================
// User Response Model (Maps to UserResponseDto)
// ==========================================
export interface UserResponse extends BaseEntity {
  id: number;
  username: string;
  fullName: string;
  email: string;
  phone: string;
  role: UserRole;
  branchId: number;
  branchName: string;
  enabled: boolean;
  isActive: boolean;
  createdAt: Date | string;
}

// ==========================================
// Password Management Sub-Interfaces
// ==========================================
export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
  confirmPassword: string;
}

export interface ChangePasswordResponse {
  success: boolean;
  message: string;
  changedAt: Date | string;
}