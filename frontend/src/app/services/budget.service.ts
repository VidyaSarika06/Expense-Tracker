import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Budget } from '../models/models';

@Injectable({ providedIn: 'root' })
export class BudgetService {

  private base = `${environment.apiUrl}/budgets`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Budget[]> {
    return this.http.get<Budget[]>(this.base);
  }

  getById(id: number): Observable<Budget> {
    return this.http.get<Budget>(`${this.base}/${id}`);
  }

  create(payload: Budget): Observable<Budget> {
    return this.http.post<Budget>(this.base, payload);
  }

  update(id: number, payload: Budget): Observable<Budget> {
    return this.http.put<Budget>(`${this.base}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
