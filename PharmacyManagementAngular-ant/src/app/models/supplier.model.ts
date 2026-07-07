import { BaseEntity } from './baseEntity.model';
import { Address } from './address.model';

export interface SupplierModel extends BaseEntity {
  supplierCode: string;
  name: string;
  contactPerson?: string; // Optional because nullable is true by default in JPA
  phone?: string;
  email?: string;
  address?: Address;      // Embedded object mapping
  tradeLicenseNo?: string;
  taxId?: string;
}