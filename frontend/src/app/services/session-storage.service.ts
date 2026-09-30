import { Injectable } from '@angular/core';

const TOKEN_KEY = 'auth_token';
const ROLE_KEY  = 'auth_role';
const EMAIL_KEY = 'auth_email';

/**
 * SessionStorageService — single source of truth for session data.
 * All auth state is stored in sessionStorage (cleared on tab close).
 */
@Injectable({ providedIn: 'root' })
export class SessionStorageService {

  setToken(token: string): void {
    sessionStorage.setItem(TOKEN_KEY, token);
  }

  getToken(): string | null {
    return sessionStorage.getItem(TOKEN_KEY);
  }

  setRole(role: string): void {
    sessionStorage.setItem(ROLE_KEY, role);
  }

  getRole(): string | null {
    return sessionStorage.getItem(ROLE_KEY);
  }

  setUserEmail(email: string): void {
    sessionStorage.setItem(EMAIL_KEY, email);
  }

  getUserEmail(): string | null {
    return sessionStorage.getItem(EMAIL_KEY);
  }

  clear(): void {
    sessionStorage.removeItem(TOKEN_KEY);
    sessionStorage.removeItem(ROLE_KEY);
    sessionStorage.removeItem(EMAIL_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
