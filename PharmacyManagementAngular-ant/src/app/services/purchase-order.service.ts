import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { PurchaseOrderRequest, PurchaseOrderResponse, PurchaseOrderStatus } from '../models/purchase-order.model';

@Injectable({
  providedIn: 'root',
})
export class PurchaseOrderService {
  
  // API endpoint matching backend controller mapping
  private apiUrl = environment.apiUrl + 'purchase-orders';

  constructor(private http: HttpClient) {}

  // Create a brand new purchase order matrix reference entry
  createPurchaseOrder(dto: PurchaseOrderRequest): Observable<PurchaseOrderResponse> {
    return this.http.post<PurchaseOrderResponse>(this.apiUrl, dto);
  }

  // Fetch all historical purchase orders catalog lists
  getAllPurchaseOrders(): Observable<PurchaseOrderResponse[]> {
    return this.http.get<PurchaseOrderResponse[]>(this.apiUrl);
  }

  // Fetch tracked purchase orders filtered by unique lifecycle status state
  getPurchaseOrdersByStatus(status: PurchaseOrderStatus): Observable<PurchaseOrderResponse[]> {
    return this.http.get<PurchaseOrderResponse[]>(`${this.apiUrl}/status/${status}`);
  }

  // Find a specific purchase order parameters card by its database ID
  getPurchaseOrderById(id: number): Observable<PurchaseOrderResponse> {
    return this.http.get<PurchaseOrderResponse>(`${this.apiUrl}/${id}`);
  }

  // Find a unique purchase order row matching specific string number identities
  getPurchaseOrderByPoNumber(poNumber: string): Observable<PurchaseOrderResponse> {
    return this.http.get<PurchaseOrderResponse>(`${this.apiUrl}/number/${poNumber}`);
  }

  // Update structural properties or items list under an existing purchase order
  updatePurchaseOrder(id: number, dto: PurchaseOrderRequest): Observable<PurchaseOrderResponse> {
    return this.http.put<PurchaseOrderResponse>(`${this.apiUrl}/${id}`, dto);
  }

  // Patch lifecycle state tokens utilizing query param maps interception
  updatePurchaseOrderStatus(id: number, status: PurchaseOrderStatus): Observable<PurchaseOrderResponse> {
    const params = new HttpParams().set('status', status);
    return this.http.patch<PurchaseOrderResponse>(`${this.apiUrl}/${id}/status`, {}, { params });
  }

  // Approve a pending purchase order tracking profile state via specialized patch routes
  approvePurchaseOrder(id: number): Observable<PurchaseOrderResponse> {
    return this.http.patch<PurchaseOrderResponse>(`${this.apiUrl}/${id}/approve`, {});
  }

  // Reject a pending purchase order tracking profile state via specialized patch routes
  rejectPurchaseOrder(id: number): Observable<PurchaseOrderResponse> {
    return this.http.patch<PurchaseOrderResponse>(`${this.apiUrl}/${id}/reject`, {});
  }

  // Remove a purchase order instance reference completely from backend tracking tables
  deletePurchaseOrder(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}