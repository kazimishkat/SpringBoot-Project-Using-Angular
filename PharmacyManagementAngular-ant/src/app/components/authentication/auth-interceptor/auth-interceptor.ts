import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { StorageService } from '../../../services/storage.service';

/**
 * Global HTTP Interceptor that appends the JWT bearer authorization string
 * into outgoing application network requests
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const storage = inject(StorageService);
  const token = storage.getToken();

  // Dynamically structuralize configuration values to meet Spring's JwtAuthFilter expectations
  if (token) {
    req = req.clone({
      setHeaders: { 
        Authorization: `Bearer ${token}` 
      }
    });
  }

  return next(req);
};
