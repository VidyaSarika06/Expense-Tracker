import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { LoginRequest, LoginResponse, RegisterRequest } from '../models/models';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private base = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) {}

  register(payload: RegisterRequest): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${this.base}/register`, payload);
  }

  login(payload: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.base}/login`, payload);
  }
}
