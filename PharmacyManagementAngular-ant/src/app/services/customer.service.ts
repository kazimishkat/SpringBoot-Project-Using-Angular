import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { CustomerRequest, CustomerResponse } from '../models/customer.model';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {

  private apiUrl = environment.apiUrl + 'customers';

  constructor(private http: HttpClient) {}

  getAllCustomers(): Observable<CustomerResponse[]> {
    return this.http.get<CustomerResponse[]>(this.apiUrl);
  }

  getActiveCustomers(): Observable<CustomerResponse[]> {
    return this.http.get<CustomerResponse[]>(`${this.apiUrl}/active`);
  }

  getCustomerById(id: number): Observable<CustomerResponse> {
    return this.http.get<CustomerResponse>(`${this.apiUrl}/${id}`);
  }

  getCustomerByPhone(phone: string): Observable<CustomerResponse> {
    return this.http.get<CustomerResponse>(`${this.apiUrl}/phone/${phone}`);
  }

  getCustomerByEmail(email: string): Observable<CustomerResponse> {
    return this.http.get<CustomerResponse>(`${this.apiUrl}/email/${email}`);
  }

  /**
   * Creates a new customer (Walk-in or Online) with optional Multipart profile image.
   * Spring Boot expects @RequestPart("customer") and optional @RequestPart("image")
   */
  createCustomer(dto: CustomerRequest, imageFile?: File): Observable<CustomerResponse> {
    const formData = new FormData();
    formData.append('customer', new Blob([JSON.stringify(dto)], { type: 'application/json' }));

    if (imageFile) {
      formData.append('image', imageFile);
    }

    return this.http.post<CustomerResponse>(this.apiUrl, formData);
  }

  /**
   * Updates customer details with optional Multipart image payload.
   */
  updateCustomer(id: number, dto: CustomerRequest, imageFile?: File): Observable<CustomerResponse> {
    const formData = new FormData();
    formData.append('customer', new Blob([JSON.stringify(dto)], { type: 'application/json' }));

    if (imageFile) {
      formData.append('image', imageFile);
    }

    return this.http.put<CustomerResponse>(`${this.apiUrl}/${id}`, formData);
  }

  deleteCustomer(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}