import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { SessionStorageService } from '../../services/session-storage.service';

/**
 * jwtInterceptor — functional interceptor (Angular 17 style).
 * Attaches Authorization: Bearer <token> to every request
 * EXCEPT login and register endpoints.
 */
export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const session = inject(SessionStorageService);

  // Skip token injection for public auth endpoints
  const isPublic = req.url.includes('/auth/login') || req.url.includes('/auth/register');

  if (!isPublic) {
    const token = session.getToken();
    if (token) {
      req = req.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
      });
    }
  }

  return next(req);
};
