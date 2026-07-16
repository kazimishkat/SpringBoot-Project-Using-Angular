import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { MedicineBatchRequest, MedicineBatchResponse } from '../models/medicine-batch.model';

@Injectable({
  providedIn: 'root',
})
export class MedicineBatchService {
  
  // API base endpoint matching backend controller mapping
  private apiUrl = environment.apiUrl + 'medicine-batches';

  constructor(private http: HttpClient) {}

  // Fetch all recorded medicine batches across system layers
  getAllBatches(): Observable<MedicineBatchResponse[]> {
    return this.http.get<MedicineBatchResponse[]>(this.apiUrl);
  }

  // Find a specific isolated batch configuration log by database primary key ID
  getBatchById(id: number): Observable<MedicineBatchResponse> {
    return this.http.get<MedicineBatchResponse>(`${this.apiUrl}/${id}`);
  }

  // Retrieve batch arrays linked structurally to a specific medicine index row
  getBatchesByMedicine(medicineId: number): Observable<MedicineBatchResponse[]> {
    return this.http.get<MedicineBatchResponse[]>(`${this.apiUrl}/medicine/${medicineId}`);
  }

  // Find tracking rows matching an explicit batch number identifier string
  getBatchesByNumber(batchNumber: string): Observable<MedicineBatchResponse[]> {
    return this.http.get<MedicineBatchResponse[]>(`${this.apiUrl}/number/${batchNumber}`);
  }

  // Fetch expiring batches intercepting beforeDate parameter via HttpParams map
  getExpiringBatches(beforeDate: string): Observable<MedicineBatchResponse[]> {
    const params = new HttpParams().set('beforeDate', beforeDate);
    return this.http.get<MedicineBatchResponse[]>(`${this.apiUrl}/expiring`, { params });
  }

  // Update existing batch metadata tracking parameters using standard PUT operations
  updateBatch(id: number, dto: MedicineBatchRequest): Observable<MedicineBatchResponse> {
    return this.http.put<MedicineBatchResponse>(`${this.apiUrl}/${id}`, dto);
  }
}