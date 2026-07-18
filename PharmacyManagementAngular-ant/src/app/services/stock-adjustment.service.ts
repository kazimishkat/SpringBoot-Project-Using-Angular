import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { StockAdjustmentRequest, StockAdjustmentResponse, AdjustmentReason, AdjustmentStatus } from '../models/stock-adjustment.model';

@Injectable({
  providedIn: 'root',
})
export class StockAdjustmentService {
  
  private apiUrl = environment.apiUrl + 'stock-adjustments';

  constructor(private http: HttpClient) {}

  getAllAdjustments(): Observable<StockAdjustmentResponse[]> {
    return this.http.get<StockAdjustmentResponse[]>(this.apiUrl);
  }

  getAdjustmentById(id: number): Observable<StockAdjustmentResponse> {
    return this.http.get<StockAdjustmentResponse>(`${this.apiUrl}/${id}`);
  }

  getAdjustmentByNumber(adjustmentNumber: string): Observable<StockAdjustmentResponse> {
    return this.http.get<StockAdjustmentResponse>(`${this.apiUrl}/number/${adjustmentNumber}`);
  }

  createAdjustment(dto: StockAdjustmentRequest): Observable<StockAdjustmentResponse> {
    return this.http.post<StockAdjustmentResponse>(this.apiUrl, dto);
  }

  updateAdjustment(id: number, dto: StockAdjustmentRequest): Observable<StockAdjustmentResponse> {
    return this.http.put<StockAdjustmentResponse>(`${this.apiUrl}/${id}`, dto);
  }

  approveAdjustment(id: number, approvedById: number): Observable<StockAdjustmentResponse> {
    const params = new HttpParams().set('approvedById', approvedById.toString());
    return this.http.patch<StockAdjustmentResponse>(`${this.apiUrl}/${id}/approve`, {}, { params });
  }

  cancelAdjustment(id: number): Observable<StockAdjustmentResponse> {
    return this.http.patch<StockAdjustmentResponse>(`${this.apiUrl}/${id}/cancel`, {});
  }

  deleteStockAdjustment(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }

  getByStatus(status: AdjustmentStatus): Observable<StockAdjustmentResponse[]> {
    return this.http.get<StockAdjustmentResponse[]>(`${this.apiUrl}/status/${status}`);
  }

  getByReason(reason: AdjustmentReason): Observable<StockAdjustmentResponse[]> {
    return this.http.get<StockAdjustmentResponse[]>(`${this.apiUrl}/reason/${reason}`);
  }

  getByBranch(branchId: number): Observable<StockAdjustmentResponse[]> {
    return this.http.get<StockAdjustmentResponse[]>(`${this.apiUrl}/branch/${branchId}`);
  }

  getByDate(dateStr: string): Observable<StockAdjustmentResponse[]> {
    const params = new HttpParams().set('date', dateStr);
    return this.http.get<StockAdjustmentResponse[]>(`${this.apiUrl}/date`, { params });
  }

  getByDateRange(startDate: string, endDate: string): Observable<StockAdjustmentResponse[]> {
    const params = new HttpParams().set('startDate', startDate).set('endDate', endDate);
    return this.http.get<StockAdjustmentResponse[]>(`${this.apiUrl}/range`, { params });
  }
}