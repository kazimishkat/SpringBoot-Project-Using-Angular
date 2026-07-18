import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { StockMovementRequest, StockMovementResponse, StockMovementType } from '../models/stock-movement.model';

@Injectable({
  providedIn: 'root',
})
export class StockMovementService {
  
  private apiUrl = environment.apiUrl + 'stock-movements';

  constructor(private http: HttpClient) {}

  // Fetch all system historical ledger assets records
  getAllMovements(): Observable<StockMovementResponse[]> {
    return this.http.get<StockMovementResponse[]>(this.apiUrl);
  }

  // Find individual ledger identity parameter matching its primary key
  getMovementById(id: number): Observable<StockMovementResponse> {
    return this.http.get<StockMovementResponse>(`${this.apiUrl}/${id}`);
  }

  // Fetch audit trails records matching specific branch contexts
  getByBranch(branchId: number): Observable<StockMovementResponse[]> {
    return this.http.get<StockMovementResponse[]>(`${this.apiUrl}/branch/${branchId}`);
  }

  // Retrieve ledger entries targeting individual medicine product batches
  getByBatch(batchId: number): Observable<StockMovementResponse[]> {
    return this.http.get<StockMovementResponse[]>(`${this.apiUrl}/batch/${batchId}`);
  }

  // Client-side mapping fallback helper filtering by core medicine references
  getByMedicine(medicineId: number): Observable<StockMovementResponse[]> {
    return this.http.get<StockMovementResponse[]>(`${this.apiUrl}?medicineId=${medicineId}`);
  }

  // Fetch operational logs matching distinct branch parameters and movement classes
  getByMovementType(branchId: number, movementType: StockMovementType): Observable<StockMovementResponse[]> {
    return this.http.get<StockMovementResponse[]>(`${this.apiUrl}/branch/${branchId}/type/${movementType}`);
  }

  // Track transaction references parameters utilizing dynamic parameter intercepts maps
  getByReference(type: string, id: number): Observable<StockMovementResponse[]> {
    const params = new HttpParams().set('type', type).set('id', id.toString());
    return this.http.get<StockMovementResponse[]>(`${this.apiUrl}/reference`, { params });
  }

  // Local date interception tracking allocation queries parameters
  getByDate(dateStr: string): Observable<StockMovementResponse[]> {
    const params = new HttpParams().set('date', dateStr);
    return this.http.get<StockMovementResponse[]>(`${this.apiUrl}/date`, { params });
  }

  // Date range interval ledger tracking calculations logs
  getByDateRange(startDate: string, endDate: string): Observable<StockMovementResponse[]> {
    const params = new HttpParams().set('startDate', startDate).set('endDate', endDate);
    return this.http.get<StockMovementResponse[]>(`${this.apiUrl}/range`, { params });
  }

  // Internal module routing endpoint used only for background engine injections simulation logs
  createMovement(dto: StockMovementRequest): Observable<StockMovementResponse> {
    return this.http.post<StockMovementResponse>(this.apiUrl, dto);
  }
}