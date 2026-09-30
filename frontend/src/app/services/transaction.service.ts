import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Transaction, PageResponse } from '../models/models';

export interface TransactionFilter {
  startDate?: string;
  endDate?: string;
  categoryId?: number;
  type?: string;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDir?: string;
}

@Injectable({ providedIn: 'root' })
export class TransactionService {

  private base = `${environment.apiUrl}/transactions`;

  constructor(private http: HttpClient) {}

  getAll(filter: TransactionFilter = {}): Observable<PageResponse<Transaction>> {
    let params = new HttpParams();
    if (filter.startDate)              params = params.set('startDate',  filter.startDate);
    if (filter.endDate)                params = params.set('endDate',    filter.endDate);
    if (filter.categoryId)             params = params.set('categoryId', filter.categoryId.toString());
    // Only send type if it is a non-empty string (avoids sending type="" to backend)
    if (filter.type && filter.type !== '') params = params.set('type', filter.type);
    params = params.set('page',    (filter.page  ?? 0).toString());
    params = params.set('size',    (filter.size  ?? 10).toString());
    params = params.set('sortBy',  filter.sortBy  ?? 'date');
    params = params.set('sortDir', filter.sortDir ?? 'desc');
    return this.http.get<PageResponse<Transaction>>(this.base, { params });
  }

  getById(id: number): Observable<Transaction> {
    return this.http.get<Transaction>(`${this.base}/${id}`);
  }

  create(payload: Transaction): Observable<Transaction> {
    return this.http.post<Transaction>(this.base, payload);
  }

  update(id: number, payload: Transaction): Observable<Transaction> {
    return this.http.put<Transaction>(`${this.base}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
