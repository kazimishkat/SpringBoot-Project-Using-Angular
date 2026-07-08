import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { MedicineCategoryModel } from '../models/medicine-category.model';

@Injectable({
  providedIn: 'root',
})
export class MedicineCategoryService {

  private apiUrl = environment.apiUrl + 'medicine-categories';

  constructor(private http: HttpClient) { }

  // ==========================================
  // Medicine Category CRUD Operations
  // ==========================================

  getAllCategories(): Observable<MedicineCategoryModel[]> {
    return this.http.get<MedicineCategoryModel[]>(this.apiUrl);
  }

  getCategoryById(id: number): Observable<MedicineCategoryModel> {
    return this.http.get<MedicineCategoryModel>(`${this.apiUrl}/${id}`);
  }

  getCategoryByName(name: string): Observable<MedicineCategoryModel> {
    return this.http.get<MedicineCategoryModel>(`${this.apiUrl}/name/${name}`);
  }

  createCategory(category: MedicineCategoryModel): Observable<MedicineCategoryModel> {
    return this.http.post<MedicineCategoryModel>(this.apiUrl, category);
  }

  updateCategory(id: number, category: MedicineCategoryModel): Observable<MedicineCategoryModel> {
    return this.http.put<MedicineCategoryModel>(`${this.apiUrl}/${id}`, category);
  }

  deleteCategory(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}