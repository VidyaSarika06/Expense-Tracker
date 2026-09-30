import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { MonthlySummary, BudgetAlert } from '../models/models';

@Injectable({ providedIn: 'root' })
export class DashboardService {

  private base = `${environment.apiUrl}/dashboard`;

  constructor(private http: HttpClient) {}

  getMonthlySummary(month: string): Observable<MonthlySummary> {
    const params = new HttpParams().set('month', month);
    return this.http.get<MonthlySummary>(`${this.base}/monthly-summary`, { params });
  }

  getBudgetAlerts(month: string): Observable<BudgetAlert[]> {
    const params = new HttpParams().set('month', month);
    return this.http.get<BudgetAlert[]>(`${this.base}/budget-alerts`, { params });
  }
}
