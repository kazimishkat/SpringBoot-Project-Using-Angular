import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";
import { Injectable } from "@angular/core";
import { MedicineBatchModel } from "../models/medicine-batch.model";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root',
})
export class MedicineBatchService {

  private apiUrl = environment.apiUrl + 'medicine-batches';

  constructor(private http: HttpClient) { }

  // ==========================================
  // Medicine Batch CRUD Operations
  // ==========================================

  getAllBatches(): Observable<MedicineBatchModel[]> {
    return this.http.get<MedicineBatchModel[]>(this.apiUrl);
  }

  getBatchById(id: number): Observable<MedicineBatchModel> {
    return this.http.get<MedicineBatchModel>(`${this.apiUrl}/${id}`);
  }

  getBatchByNumber(batchNumber: string): Observable<MedicineBatchModel> {
    return this.http.get<MedicineBatchModel>(`${this.apiUrl}/batch-number/${batchNumber}`);
  }

  createBatch(batch: MedicineBatchModel): Observable<MedicineBatchModel> {
    return this.http.post<MedicineBatchModel>(this.apiUrl, batch);
  }

  updateBatch(id: number, batch: MedicineBatchModel): Observable<MedicineBatchModel> {
    return this.http.put<MedicineBatchModel>(`${this.apiUrl}/${id}`, batch);
  }

  deleteBatch(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}