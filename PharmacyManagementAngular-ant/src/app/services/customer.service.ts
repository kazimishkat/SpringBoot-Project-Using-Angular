import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
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

  // 🌟 [NEW]: ইনভয়েস থেকে নাম বা ফোন নম্বর দিয়ে কাস্টমার খোঁজার মেথড
  searchCustomers(query: string): Observable<CustomerResponse[]> {
    const params = new HttpParams().set('query', query);
    return this.http.get<CustomerResponse[]>(`${this.apiUrl}/search`, { params });
  }

  createCustomer(dto: CustomerRequest, imageFile?: File): Observable<CustomerResponse> {
    const formData = new FormData();
    formData.append('customer', new Blob([JSON.stringify(dto)], { type: 'application/json' }));

    if (imageFile) {
      formData.append('image', imageFile);
    }

    return this.http.post<CustomerResponse>(this.apiUrl, formData);
  }

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