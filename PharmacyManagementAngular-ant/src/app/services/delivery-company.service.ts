import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { DeliveryCompanyRequest, DeliveryCompanyResponse } from '../models/delivery-company.model';

@Injectable({
  providedIn: 'root',
})
export class DeliveryCompanyService {
  
  // API endpoint matching backend controller mapping
  private apiUrl = environment.apiUrl + 'delivery-companies';

  constructor(private http: HttpClient) {}

  // Register a new delivery partner profile into the registry
  registerCompany(dto: DeliveryCompanyRequest): Observable<DeliveryCompanyResponse> {
    return this.http.post<DeliveryCompanyResponse>(this.apiUrl, dto);
  }

  // Fetch only active delivery companies for order dispatch drops dropdowns
  getActiveCompanies(): Observable<DeliveryCompanyResponse[]> {
    return this.http.get<DeliveryCompanyResponse[]>(`${this.apiUrl}/active`);
  }

  // Find a specific courier setup registry by its primary key ID
  getCompanyById(id: number): Observable<DeliveryCompanyResponse> {
    return this.http.get<DeliveryCompanyResponse>(`${this.apiUrl}/${id}`);
  }

  // Update operational status tracking values via targeted patch operations
  toggleCompanyStatus(id: number, isActive: boolean): Observable<DeliveryCompanyResponse> {
    const params = new HttpParams().set('isActive', isActive.toString());
    return this.http.patch<DeliveryCompanyResponse>(`${this.apiUrl}/${id}/toggle-status`, {}, { params });
  }
}