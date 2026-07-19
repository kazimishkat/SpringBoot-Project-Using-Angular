import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { StorageService } from '../../../services/storage.service';

/** 
 * Blocks unauthenticated access to protected dashboard systems 
 * Redirects directly back to fallback login screen
 */
export const authGuard: CanActivateFn = () => {
  const storage = inject(StorageService);
  const router = inject(Router);

  if (storage.isLoggedIn()) {
    return true;
  }

  router.navigate(['/login']);
  return false;
};
