import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { GenericMedicineRequest, GenericMedicineResponse } from '../models/generic-medicine.model';

@Injectable({
  providedIn: 'root',
})
export class GenericMedicineService {
  
  // API endpoint for generic medicines
  private apiUrl = environment.apiUrl + 'generic-medicines';

  constructor(private http: HttpClient) {}

  // Fetch all generic medicines
  getAll(): Observable<GenericMedicineResponse[]> {
    return this.http.get<GenericMedicineResponse[]>(this.apiUrl);
  }

  // Get generic medicine by technical database ID
  getById(id: number): Observable<GenericMedicineResponse> {
    return this.http.get<GenericMedicineResponse>(`${this.apiUrl}/${id}`);
  }

  // Fetch generic medicine data by name lookup
  getByName(genericName: string): Observable<GenericMedicineResponse> {
    return this.http.get<GenericMedicineResponse>(`${this.apiUrl}/name/${genericName}`);
  }

  // Create a new generic medicine registry
  create(dto: GenericMedicineRequest): Observable<GenericMedicineResponse> {
    return this.http.post<GenericMedicineResponse>(this.apiUrl, dto);
  }

  // Update existing generic records
  update(id: number, dto: GenericMedicineRequest): Observable<GenericMedicineResponse> {
    return this.http.put<GenericMedicineResponse>(`${this.apiUrl}/${id}`, dto);
  }

  // Remove generic record entirely by ID
  delete(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}