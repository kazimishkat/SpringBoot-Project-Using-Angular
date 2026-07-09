import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { MedicineBatchRequest, MedicineBatchResponse } from '../models/medicine-batch.model';

@Injectable({
  providedIn: 'root',
})
export class MedicineBatchService {
  
  // API endpoint for medicine batches
  private apiUrl = environment.apiUrl + 'medicine-batches';

  constructor(private http: HttpClient) {}

  // Fetch all medicine batches
  getAllBatches(): Observable<MedicineBatchResponse[]> {
    return this.http.get<MedicineBatchResponse[]>(this.apiUrl);
  }

  // Get batch by database ID
  getBatchById(id: number): Observable<MedicineBatchResponse> {
    return this.http.get<MedicineBatchResponse>(`${`${this.apiUrl}/${id}`}`);
  }

  // Fetch batches belonging to a specific medicine ID
  getBatchesByMedicine(medicineId: number): Observable<MedicineBatchResponse[]> {
    return this.http.get<MedicineBatchResponse[]>(`${this.apiUrl}/medicine/${medicineId}`);
  }

  // Fetch batches matching a specific batch number identifier
  getBatchesByNumber(batchNumber: string): Observable<MedicineBatchResponse[]> {
    return this.http.get<MedicineBatchResponse[]>(`${this.apiUrl}/number/${batchNumber}`);
  }

  // Fetch batches expiring before a targeted ISO date string
  getExpiringBatches(beforeDate: string): Observable<MedicineBatchResponse[]> {
    const params = new HttpParams().set('beforeDate', beforeDate);
    return this.http.get<MedicineBatchResponse[]>(`${this.apiUrl}/expiring`, { params });
  }

  // Create a new medicine batch tracking record
  createBatch(dto: MedicineBatchRequest): Observable<MedicineBatchResponse> {
    return this.http.post<MedicineBatchResponse>(this.apiUrl, dto);
  }

  // Update existing batch configuration metrics
  updateBatch(id: number, dto: MedicineBatchRequest): Observable<MedicineBatchResponse> {
    return this.http.put<MedicineBatchResponse>(`${this.apiUrl}/${id}`, dto);
  }

  // Remove batch instance documentation entirely
  deleteBatch(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}