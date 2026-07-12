import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { SupplierRequest, SupplierResponse } from '../models/supplier.model';

@Injectable({
  providedIn: 'root',
})
export class SupplierService {
  
  // API endpoint matching backend mapping
  private apiUrl = environment.apiUrl + 'suppliers';

  constructor(private http: HttpClient) {}

  // Fetch all suppliers from database registry
  getAllSuppliers(): Observable<SupplierResponse[]> {
    return this.http.get<SupplierResponse[]>(this.apiUrl);
  }

  // Find a specific supplier by database ID
  getSupplierById(id: number): Observable<SupplierResponse> {
    return this.http.get<SupplierResponse>(`${this.apiUrl}/${id}`);
  }

  // Find supplier matching corporate supplier code string
  getSupplierByCode(supplierCode: string): Observable<SupplierResponse> {
    return this.http.get<SupplierResponse>(`${this.apiUrl}/code/${supplierCode}`);
  }

  // Search suppliers by name using query parameter
  searchSuppliersByName(name: string): Observable<SupplierResponse[]> {
    const params = new HttpParams().set('name', name);
    return this.http.get<SupplierResponse[]>(`${this.apiUrl}/search`, { params });
  }

  // Create a new supplier corporate registry profile
  createSupplier(dto: SupplierRequest): Observable<SupplierResponse> {
    return this.http.post<SupplierResponse>(this.apiUrl, dto);
  }

  // Update existing supplier records validation parameters
  updateSupplier(id: number, dto: SupplierRequest): Observable<SupplierResponse> {
    return this.http.put<SupplierResponse>(`${this.apiUrl}/${id}`, dto);
  }

  // Completely drop supplier corporate profile from tracking
  deleteSupplier(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}