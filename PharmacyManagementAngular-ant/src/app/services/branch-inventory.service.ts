import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { BranchInventoryRequest, BranchInventoryResponse } from '../models/branch-inventory.model';

@Injectable({
  providedIn: 'root',
})
export class BranchInventoryService {
  
  // API endpoint matching backend mapping
  private apiUrl = environment.apiUrl + 'branch-inventories';

  constructor(private http: HttpClient) {}

  // Fetch all stock distribution matrix records
  getAll(): Observable<BranchInventoryResponse[]> {
    return this.http.get<BranchInventoryResponse[]>(this.apiUrl);
  }

  // Get single inventory tracking record by technical primary ID
  getById(id: number): Observable<BranchInventoryResponse> {
    return this.http.get<BranchInventoryResponse>(`${this.apiUrl}/${id}`);
  }

  // Fetch all stock rows belonging to a targeted branch assignment
  getByBranch(branchId: number): Observable<BranchInventoryResponse[]> {
    return this.http.get<BranchInventoryResponse[]>(`${this.apiUrl}/branch/${branchId}`);
  }

  // Find unique stock allocation by target combinations (branch + batch)
  getByBranchAndBatch(branchId: number, batchId: number): Observable<BranchInventoryResponse> {
    return this.http.get<BranchInventoryResponse>(`${this.apiUrl}/branch/${branchId}/batch/${batchId}`);
  }

  // Calculate cumulative item pieces across specific medicine lines inside a branch
  getTotalQuantityByBranchAndMedicine(branchId: number, medicineId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/branch/${branchId}/medicine/${medicineId}/total`);
  }

  // Setup new stock allocation balance matrix records
  createInventory(dto: BranchInventoryRequest): Observable<BranchInventoryResponse> {
    return this.http.post<BranchInventoryResponse>(this.apiUrl, dto);
  }

  // Adjust target inventory values (Updates quantityOnHand variables)
  updateInventory(id: number, dto: BranchInventoryRequest): Observable<BranchInventoryResponse> {
    return this.http.put<BranchInventoryResponse>(`${this.apiUrl}/${id}`, dto);
  }

  // Purge allocated row references permanently from central tables
  deleteInventory(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}