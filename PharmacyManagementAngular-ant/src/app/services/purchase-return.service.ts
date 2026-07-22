import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { PurchaseReturnRequest, PurchaseReturnResponse, ApprovalStatus } from '../models/purchase-return.model';

@Injectable({
  providedIn: 'root',
})
export class PurchaseReturnService {

  private apiUrl = environment.apiUrl + 'purchase-returns';

  constructor(private http: HttpClient) {}

  getAllPurchaseReturns(): Observable<PurchaseReturnResponse[]> {
    return this.http.get<PurchaseReturnResponse[]>(this.apiUrl);
  }

  getPurchaseReturnById(id: number): Observable<PurchaseReturnResponse> {
    return this.http.get<PurchaseReturnResponse>(`${this.apiUrl}/${id}`);
  }

  getPurchaseReturnByNumber(returnNumber: string): Observable<PurchaseReturnResponse> {
    return this.http.get<PurchaseReturnResponse>(`${this.apiUrl}/number/${returnNumber}`);
  }

  getPurchaseReturnsBySupplier(supplierId: number): Observable<PurchaseReturnResponse[]> {
    return this.http.get<PurchaseReturnResponse[]>(`${this.apiUrl}/supplier/${supplierId}`);
  }

  getPurchaseReturnsByStatus(status: ApprovalStatus): Observable<PurchaseReturnResponse[]> {
    const params = new HttpParams().set('status', status);
    return this.http.get<PurchaseReturnResponse[]>(`${this.apiUrl}/status`, { params });
  }

  createPurchaseReturn(dto: PurchaseReturnRequest): Observable<PurchaseReturnResponse> {
    return this.http.post<PurchaseReturnResponse>(this.apiUrl, dto);
  }

  updatePurchaseReturn(id: number, dto: PurchaseReturnRequest): Observable<PurchaseReturnResponse> {
    return this.http.put<PurchaseReturnResponse>(`${this.apiUrl}/${id}`, dto);
  }

  approvePurchaseReturn(id: number): Observable<PurchaseReturnResponse> {
    const params = new HttpParams().set('status', ApprovalStatus.APPROVED);
    return this.http.patch<PurchaseReturnResponse>(`${this.apiUrl}/${id}/approve`, {}, { params });
  }

  cancelPurchaseReturn(id: number): Observable<PurchaseReturnResponse> {
    return this.http.delete<PurchaseReturnResponse>(`${this.apiUrl}/${id}`);
  }

  printPurchaseReturn(id: number): Observable<PurchaseReturnResponse> {
    return this.http.get<PurchaseReturnResponse>(`${this.apiUrl}/${id}/print`);
  }
}