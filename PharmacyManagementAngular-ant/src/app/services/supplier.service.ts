import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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