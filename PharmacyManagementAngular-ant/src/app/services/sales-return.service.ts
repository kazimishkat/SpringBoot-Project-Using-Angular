import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { SalesReturnRequest, SalesReturnResponse, ApprovalStatus } from '../models/sales-return.model';

@Injectable({
  providedIn: 'root',
})
export class SalesReturnService {

  private apiUrl = environment.apiUrl + 'sales-returns';

  constructor(private http: HttpClient) {}

  getAllReturns(): Observable<SalesReturnResponse[]> {
    return this.http.get<SalesReturnResponse[]>(this.apiUrl);
  }

  getReturnById(id: number): Observable<SalesReturnResponse> {
    return this.http.get<SalesReturnResponse>(`${this.apiUrl}/${id}`);
  }

  getReturnByNumber(returnNumber: string): Observable<SalesReturnResponse> {
    return this.http.get<SalesReturnResponse>(`${this.apiUrl}/number/${returnNumber}`);
  }

  getReturnsByInvoiceId(invoiceId: number): Observable<SalesReturnResponse[]> {
    return this.http.get<SalesReturnResponse[]>(`${this.apiUrl}/invoice/${invoiceId}`);
  }

  createReturn(request: SalesReturnRequest): Observable<SalesReturnResponse> {
    return this.http.post<SalesReturnResponse>(this.apiUrl, request);
  }

  approveReturn(id: number): Observable<SalesReturnResponse> {
    return this.http.patch<SalesReturnResponse>(`${this.apiUrl}/${id}/approve`, {});
  }

  rejectReturn(id: number): Observable<SalesReturnResponse> {
    return this.http.patch<SalesReturnResponse>(`${this.apiUrl}/${id}/reject`, {});
  }

  searchReturns(query: string): Observable<SalesReturnResponse[]> {
    const params = new HttpParams().set('query', query);
    return this.http.get<SalesReturnResponse[]>(`${this.apiUrl}/search`, { params });
  }

  filterReturns(invoiceId?: number, status?: ApprovalStatus, startDate?: string, endDate?: string): Observable<SalesReturnResponse[]> {
    let params = new HttpParams();
    if (invoiceId) params = params.set('invoiceId', invoiceId.toString());
    if (status) params = params.set('status', status);
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);

    return this.http.get<SalesReturnResponse[]>(`${this.apiUrl}/filter`, { params });
  }

  updateReturn(id: number, request: SalesReturnRequest): Observable<SalesReturnResponse> {
    return this.http.put<SalesReturnResponse>(`${this.apiUrl}/${id}`, request);
  }

  deleteReturn(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}