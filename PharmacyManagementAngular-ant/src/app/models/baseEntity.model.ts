export interface BaseEntity {
  id?: number; // Maps to Long
  createdAt?: Date | string; // Maps to LocalDateTime
  updatedAt?: Date | string; // Maps to LocalDateTime
  createdBy?: string;
  updatedBy?: string;
  isActive: boolean; // Maps to Boolean (defaults to true in Java)
}