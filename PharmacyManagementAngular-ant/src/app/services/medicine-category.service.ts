import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { MedicineCategoryRequest, MedicineCategoryResponse } from '../models/medicine-category.model';

@Injectable({
  providedIn: 'root',
})
export class MedicineCategoryService {
  
  // API endpoint for medicine categories
  private apiUrl = environment.apiUrl + 'medicine-categories';

  constructor(private http: HttpClient) {}

  // Fetch all medicine categories
  getAllCategories(): Observable<MedicineCategoryResponse[]> {
    return this.http.get<MedicineCategoryResponse[]>(this.apiUrl);
  }

  // Get category by technical database ID
  getCategoryById(id: number): Observable<MedicineCategoryResponse> {
    return this.http.get<MedicineCategoryResponse>(`${this.apiUrl}/${id}`);
  }

  // Fetch category data by name lookup
  getCategoryByName(name: string): Observable<MedicineCategoryResponse> {
    return this.http.get<MedicineCategoryResponse>(`${this.apiUrl}/name/${name}`);
  }

  // Create a new medicine category
  createCategory(dto: MedicineCategoryRequest): Observable<MedicineCategoryResponse> {
    return this.http.post<MedicineCategoryResponse>(this.apiUrl, dto);
  }

  // Update existing category records
  updateCategory(id: number, dto: MedicineCategoryRequest): Observable<MedicineCategoryResponse> {
    return this.http.put<MedicineCategoryResponse>(`${this.apiUrl}/${id}`, dto);
  }

  // Remove category record entirely by ID
  deleteCategory(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}