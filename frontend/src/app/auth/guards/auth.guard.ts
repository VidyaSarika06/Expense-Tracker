import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { SessionStorageService } from '../../services/session-storage.service';

/** Redirects unauthenticated users to /login */
export const authGuard: CanActivateFn = () => {
  const session = inject(SessionStorageService);
  const router  = inject(Router);

  if (session.isLoggedIn()) {
    return true;
  }
  return router.createUrlTree(['/login']);
};
