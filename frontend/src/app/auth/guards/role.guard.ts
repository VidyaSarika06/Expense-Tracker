import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { SessionStorageService } from '../../services/session-storage.service';

/** Allows access only to ROLE_INDIVIDUAL users */
export const individualGuard: CanActivateFn = () => {
  const session = inject(SessionStorageService);
  const router  = inject(Router);

  if (session.getRole() === 'ROLE_INDIVIDUAL') {
    return true;
  }
  return router.createUrlTree(['/dashboard']);
};
