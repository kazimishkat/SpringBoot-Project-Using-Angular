import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { BranchInventoryRequest, BranchInventoryResponse } from '../models/branch-inventory.model';

@Injectable({
  providedIn: 'root',
})
export class BranchInventoryService {
  
  // API base endpoint matching backend controller mapping
  private apiUrl = environment.apiUrl + 'branch-inventories'; // adjust path to match your spring boot app routing if needed

  constructor(private http: HttpClient) {}

  // Fetch all recorded inventory configurations across branches
  getAllInventory(): Observable<BranchInventoryResponse[]> {
    return this.http.get<BranchInventoryResponse[]>(this.apiUrl);
  }

  // Find detailed inventory data row using its database primary key ID
  getInventoryById(id: number): Observable<BranchInventoryResponse> {
    return this.http.get<BranchInventoryResponse>(`${this.apiUrl}/${id}`);
  }

  // Retrieve matching inventory sheets mapped to a specific branch primary ID
  getInventoryByBranch(branchId: number): Observable<BranchInventoryResponse[]> {
    return this.http.get<BranchInventoryResponse[]>(`${this.apiUrl}/branch/${branchId}`);
  }

  // Retrieve inventory balances mapped to specific pharmaceutical items
  getInventoryByMedicine(medicineId: number): Observable<BranchInventoryResponse[]> {
    return this.http.get<BranchInventoryResponse[]>(`${this.apiUrl}/medicine/${medicineId}`);
  }

  // Fetch low stock items with optional warning threshold parameters
  getLowStock(threshold?: number): Observable<BranchInventoryResponse[]> {
    let params = new HttpParams();
    if (threshold !== undefined) {
      params = params.set('threshold', threshold.toString());
    }
    return this.http.get<BranchInventoryResponse[]>(`${this.apiUrl}/low-stock`, { params });
  }

  // Fetch products out of stock (available quantity matches zero)
  getOutOfStock(): Observable<BranchInventoryResponse[]> {
    return this.http.get<BranchInventoryResponse[]>(`${this.apiUrl}/out-of-stock`);
  }

  // Fetch expiring batches matching ISO date string limits parameterization
  getExpiringInventory(beforeDate?: string): Observable<BranchInventoryResponse[]> {
    let params = new HttpParams();
    if (beforeDate) {
      params = params.set('beforeDate', beforeDate);
    }
    return this.http.get<BranchInventoryResponse[]>(`${this.apiUrl}/expiring`, { params });
  }
}