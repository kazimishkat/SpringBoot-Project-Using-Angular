import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { StockMovementRequest, StockMovementResponse, StockMovementType } from '../models/stock-movement.model';

@Injectable({
  providedIn: 'root',
})
export class StockMovementService {
  
  // API endpoint matching backend mapping architecture
  private apiUrl = environment.apiUrl + 'stock-movements';

  constructor(private http: HttpClient) {}

  // Fetch all historical ledger asset movement records
  getAllStockMovements(): Observable<StockMovementResponse[]> {
    return this.http.get<StockMovementResponse[]>(this.apiUrl);
  }

  // Find single ledger allocation parameter matching its database ID
  getStockMovementById(id: number): Observable<StockMovementResponse> {
    return this.http.get<StockMovementResponse>(`${this.apiUrl}/${id}`);
  }

  // Fetch audit trail allocations belonging to a specific branch context
  getMovementsByBranchId(branchId: number): Observable<StockMovementResponse[]> {
    return this.http.get<StockMovementResponse[]>(`${this.apiUrl}/branch/${branchId}`);
  }

  // Retrieve batch transaction entries targeting individual medical products
  getMovementsByBatchId(batchId: number): Observable<StockMovementResponse[]> {
    return this.http.get<StockMovementResponse[]>(`${this.apiUrl}/batch/${batchId}`);
  }

  // Fetch operational tracks filtered by isolated branches and behavior parameters
  getMovementsByBranchAndType(branchId: number, movementType: StockMovementType): Observable<StockMovementResponse[]> {
    return this.http.get<StockMovementResponse[]>(`${this.apiUrl}/branch/${branchId}/type/${movementType}`);
  }

  // Track operational rows matching reference systems using parameters map interception
  getMovementsByReference(type: string, id: number): Observable<StockMovementResponse[]> {
    const params = new HttpParams().set('type', type).set('id', id.toString());
    return this.http.get<StockMovementResponse[]>(`${this.apiUrl}/reference`, { params });
  }

  // Create or commit a fresh batch stock transaction asset record
  createStockMovement(dto: StockMovementRequest): Observable<StockMovementResponse> {
    return this.http.post<StockMovementResponse>(this.apiUrl, dto);
  }
}