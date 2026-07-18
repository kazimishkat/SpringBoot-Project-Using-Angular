import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { StockTransferRequest, StockTransferResponse, TransferStatus, StockTransferItemRequest } from '../models/stock-transfer.model';

@Injectable({
  providedIn: 'root',
})
export class StockTransferService {
  
  private apiUrl = environment.apiUrl + 'stock-transfers';

  constructor(private http: HttpClient) {}

  getAllTransfers(): Observable<StockTransferResponse[]> {
    return this.http.get<StockTransferResponse[]>(this.apiUrl);
  }

  getTransferById(id: number): Observable<StockTransferResponse> {
    return this.http.get<StockTransferResponse>(`${this.apiUrl}/${id}`);
  }

  getByTransferNumber(transferNumber: string): Observable<StockTransferResponse> {
    return this.http.get<StockTransferResponse>(`${this.apiUrl}/number/${transferNumber}`);
  }

  createTransfer(dto: StockTransferRequest): Observable<StockTransferResponse> {
    return this.http.post<StockTransferResponse>(this.apiUrl, dto);
  }

  // Maps backend PATCH /{id}/status?status=...&userId=... workflow
  updateTransferStatus(id: number, status: TransferStatus, userId?: number): Observable<StockTransferResponse> {
    let params = new HttpParams().set('status', status);
    if (userId) {
      params = params.set('userId', userId.toString());
    }
    return this.http.patch<StockTransferResponse>(`${this.apiUrl}/${id}/status`, {}, { params });
  }

  // Legacy layout requirements interface maps for components operations
  approveTransfer(id: number, userId: number): Observable<StockTransferResponse> {
    return this.updateTransferStatus(id, TransferStatus.DISPATCHED, userId);
  }

  cancelTransfer(id: number): Observable<StockTransferResponse> {
    return this.updateTransferStatus(id, TransferStatus.CANCELLED);
  }

  // Maps backend PUT /{id}/receive?receivedById=... workflow
  receiveStockItems(id: number, receivedById: number, itemUpdates: StockTransferItemRequest[]): Observable<StockTransferResponse> {
    const params = new HttpParams().set('receivedById', receivedById.toString());
    return this.http.put<StockTransferResponse>(`${this.apiUrl}/${id}/receive`, itemUpdates, { params });
  }

  getByStatus(status: TransferStatus): Observable<StockTransferResponse[]> {
    return this.http.get<StockTransferResponse[]>(`${this.apiUrl}/status/${status}`);
  }

  getBySourceBranch(branchId: number): Observable<StockTransferResponse[]> {
    return this.http.get<StockTransferResponse[]>(`${this.apiUrl}/from-branch/${branchId}`);
  }

  getByDestinationBranch(branchId: number): Observable<StockTransferResponse[]> {
    return this.http.get<StockTransferResponse[]>(`${this.apiUrl}/to-branch/${branchId}`);
  }

  getByDate(dateStr: string): Observable<StockTransferResponse[]> {
    const params = new HttpParams().set('date', dateStr);
    return this.http.get<StockTransferResponse[]>(`${this.apiUrl}/date`, { params });
  }

  getByDateRange(startDate: string, endDate: string): Observable<StockTransferResponse[]> {
    const params = new HttpParams().set('startDate', startDate).set('endDate', endDate);
    return this.http.get<StockTransferResponse[]>(`${this.apiUrl}/range`, { params });
  }
}