import { Address } from './address.model';
import { BaseEntity } from './baseEntity.model';


// ==========================================
// Supplier Request Model (Maps to RequestDto)
// ==========================================
export interface SupplierRequest extends BaseEntity {
  supplierCode: string;
  name: string;
  contactPerson?: string;
  phone?: string;
  email?: string;
  address?: Address;
  tradeLicenseNo?: string;
  taxId?: string;
}

// ==========================================
// Supplier Response Model (Maps to ResponseDto)
// ==========================================
export interface SupplierResponse extends BaseEntity {
  id: number;
  supplierCode: string;
  name: string;
  contactPerson: string;
  phone: string;
  email: string;
  tradeLicenseNo: string;
  taxId: string;
  isActive: boolean;
  address: Address;
}