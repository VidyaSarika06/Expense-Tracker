import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Category } from '../models/models';

@Injectable({ providedIn: 'root' })
export class CategoryService {

  private base = `${environment.apiUrl}/categories`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Category[]> {
    return this.http.get<Category[]>(this.base);
  }

  getById(id: number): Observable<Category> {
    return this.http.get<Category>(`${this.base}/${id}`);
  }

  create(payload: Category): Observable<Category> {
    return this.http.post<Category>(this.base, payload);
  }

  update(id: number, payload: Category): Observable<Category> {
    return this.http.put<Category>(`${this.base}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
