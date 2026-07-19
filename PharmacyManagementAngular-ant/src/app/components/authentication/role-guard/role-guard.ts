import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { StorageService } from '../../../services/storage.service';

/**
 * Validates active session context against an allowable subset array of UserRoles
 * @param allowedRoles Explicit list matching backend structural UserRole mappings
 */
export const roleGuard = (allowedRoles: string[]): CanActivateFn => {
  return () => {
    const storage = inject(StorageService);
    const router = inject(Router);
    const role = storage.getRole();

    if (role && allowedRoles.includes(role)) {
      return true;
    }

    // Prevents breaking UI loop flows by routing to a root dashboard anchor instead of a blank 403
    router.navigate(['/dashboard']);
    return false;
  };
};
