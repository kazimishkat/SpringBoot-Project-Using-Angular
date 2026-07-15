import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { GoodsReceivedNoteRequest, GoodsReceivedNoteResponse, ApprovalStatus } from '../models/goods-received-note.model';

@Injectable({
  providedIn: 'root',
})
export class GoodsReceivedNoteService {
  
  // API base endpoint matching backend REST controller mapping configuration
  private apiUrl = environment.apiUrl + 'grn';

  constructor(private http: HttpClient) {}

  /**
   * Dispatches new GRN dataset payloads to trigger transaction operations.
   * If workflow initial status is APPROVED, it automatically executes downstream
   * batch creations and logs stock movement records inside the database.
   */
  receiveGoods(dto: GoodsReceivedNoteRequest): Observable<GoodsReceivedNoteResponse> {
    return this.http.post<GoodsReceivedNoteResponse>(this.apiUrl, dto);
  }

  /**
   * Fetch complete system historical goods received notes catalog listings matrix.
   */
  getAllGrns(): Observable<GoodsReceivedNoteResponse[]> {
    return this.http.get<GoodsReceivedNoteResponse[]>(this.apiUrl);
  }

  /**
   * Find a specific isolated GRN profile configuration using its database technical primary ID.
   */
  getGrnById(id: number): Observable<GoodsReceivedNoteResponse> {
    return this.http.get<GoodsReceivedNoteResponse>(`${this.apiUrl}/${id}`);
  }

  /**
   * Find a single unique GRN tracking document row matching specific business string number tokens.
   */
  getGrnByNumber(grnNumber: string): Observable<GoodsReceivedNoteResponse> {
    return this.http.get<GoodsReceivedNoteResponse>(`${this.apiUrl}/number/${grnNumber}`);
  }

  /**
   * Retrieve dynamic audit trail allocations filtered by isolated operational workflow approval states.
   */
  getGrnsByStatus(status: ApprovalStatus): Observable<GoodsReceivedNoteResponse[]> {
    return this.http.get<GoodsReceivedNoteResponse[]>(`${this.apiUrl}/status/${status}`);
  }

  /**
   * Fetch matching warehouse stocking GRN documents tied structurally to parental purchase order streams.
   */
  getGrnByPurchaseOrder(purchaseOrderId: number): Observable<GoodsReceivedNoteResponse[]> {
    return this.http.get<GoodsReceivedNoteResponse[]>(`${this.apiUrl}/purchase-order/${purchaseOrderId}`);
  }

  /**
   * Update fields parameters under an existing active non-cancelled GRN sequence layout.
   */
  updateGrn(id: number, dto: GoodsReceivedNoteRequest): Observable<GoodsReceivedNoteResponse> {
    return this.http.put<GoodsReceivedNoteResponse>(`${this.apiUrl}/${id}`, dto);
  }

  /**
   * Patch lifecycle workflow approval states. Shifting status tokens to APPROVED triggers
   * target medicine batch generations and records positive inventory movement lines dynamically.
   */
  updateApprovalStatus(id: number, status: ApprovalStatus): Observable<GoodsReceivedNoteResponse> {
    const params = new HttpParams().set('status', status);
    return this.http.patch<GoodsReceivedNoteResponse>(`${this.apiUrl}/${id}/status`, {}, { params });
  }

  /**
   * Triggers explicit high-level cancellation sequences. If the targeted GRN was previously APPROVED,
   * it securely logs a reverse stock ledger entry (PURCHASE_RETURN) to drop allocated units counts.
   */
  cancelGrn(id: number): Observable<GoodsReceivedNoteResponse> {
    return this.http.patch<GoodsReceivedNoteResponse>(`${this.apiUrl}/${id}/cancel`, {});
  }

  /**
   * Extract dedicated data structure layouts targeting print utility engines interface blocks.
   */
  printGrn(id: number): Observable<GoodsReceivedNoteResponse> {
    return this.http.get<GoodsReceivedNoteResponse>(`${this.apiUrl}/${id}/print`);
  }
}