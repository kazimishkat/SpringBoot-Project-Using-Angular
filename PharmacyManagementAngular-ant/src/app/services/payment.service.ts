import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { PaymentRequest, PaymentResponse, PaymentMethod } from '../models/payment.model';

@Injectable({
  providedIn: 'root',
})
export class PaymentService {

  private apiUrl = environment.apiUrl + 'payments';

  constructor(private http: HttpClient) {}

  getAllPayments(): Observable<PaymentResponse[]> {
    return this.http.get<PaymentResponse[]>(this.apiUrl);
  }

  getPaymentById(id: number): Observable<PaymentResponse> {
    return this.http.get<PaymentResponse>(`${this.apiUrl}/${id}`);
  }

  getPaymentsByInvoice(invoiceId: number): Observable<PaymentResponse[]> {
    return this.http.get<PaymentResponse[]>(`${this.apiUrl}/invoice/${invoiceId}`);
  }

  createPayment(request: PaymentRequest): Observable<PaymentResponse> {
    return this.http.post<PaymentResponse>(this.apiUrl, request);
  }

  searchPayments(keyword: string): Observable<PaymentResponse[]> {
    const params = new HttpParams().set('query', keyword);
    return this.http.get<PaymentResponse[]>(`${this.apiUrl}/search`, { params });
  }

  filterPayments(fromDate?: string, toDate?: string, invoiceId?: number, method?: PaymentMethod | string): Observable<PaymentResponse[]> {
    let params = new HttpParams();
    if (invoiceId) params = params.set('invoiceId', invoiceId.toString());
    if (method) params = params.set('method', method);
    if (fromDate) params = params.set('startDate', fromDate);
    if (toDate) params = params.set('endDate', toDate);

    return this.http.get<PaymentResponse[]>(`${this.apiUrl}/filter`, { params });
  }

  deletePayment(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}