import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { BranchInventoryModel } from "../models/branch-inventory.model";

@Injectable({
  providedIn: 'root',
})
export class BranchInventoryService {

  private apiUrl = environment.apiUrl + 'branch-inventories';

  constructor(private http: HttpClient) { }

  // ==========================================
  // Inventory CRUD & Distribution Metrics
  // ==========================================

  getAllInventories(): Observable<BranchInventoryModel[]> {
    return this.http.get<BranchInventoryModel[]>(this.apiUrl);
  }

  getInventoryById(id: number): Observable<BranchInventoryModel> {
    return this.http.get<BranchInventoryModel>(`${this.apiUrl}/${id}`);
  }

  getInventoriesByBranch(branchId: number): Observable<BranchInventoryModel[]> {
    return this.http.get<BranchInventoryModel[]>(`${this.apiUrl}/branch/${branchId}`);
  }

  getInventoryByBranchAndBatch(branchId: number, batchId: number): Observable<BranchInventoryModel> {
    return this.http.get<BranchInventoryModel>(`${this.apiUrl}/branch/${branchId}/batch/${batchId}`);
  }

  getTotalQuantityByBranchAndMedicine(branchId: number, medicineId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/branch/${branchId}/medicine/${medicineId}/total-quantity`);
  }

  createInventory(inventory: BranchInventoryModel): Observable<BranchInventoryModel> {
    return this.http.post<BranchInventoryModel>(this.apiUrl, inventory);
  }

  updateInventory(id: number, inventory: BranchInventoryModel): Observable<BranchInventoryModel> {
    return this.http.put<BranchInventoryModel>(`${this.apiUrl}/${id}`, inventory);
  }

  deleteInventory(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}