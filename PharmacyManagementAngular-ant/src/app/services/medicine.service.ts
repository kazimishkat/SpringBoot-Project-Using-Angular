import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { MedicineModelRequest, MedicineModelResponse } from '../models/medicine.model';

@Injectable({
  providedIn: 'root',
})
export class MedicineService {
  
  // API endpoint for medicines
  private apiUrl = environment.apiUrl + 'medicines';

  constructor(private http: HttpClient) {}

  // Fetch all medicines
  getAllMedicines(): Observable<MedicineModelResponse[]> {
    return this.http.get<MedicineModelResponse[]>(this.apiUrl);
  }

  // Get medicine by database ID
  getMedicineById(id: number): Observable<MedicineModelResponse> {
    return this.http.get<MedicineModelResponse>(`${this.apiUrl}/${id}`);
  }

  // Get medicine by its unique medicine code
  getMedicineByCode(medicineCode: string): Observable<MedicineModelResponse> {
    return this.http.get<MedicineModelResponse>(`${this.apiUrl}/code/${medicineCode}`);
  }

  // Search medicines by brand name using query parameter
  searchMedicinesByBrandName(brandName: string): Observable<MedicineModelResponse[]> {
    const params = new HttpParams().set('brandName', brandName);
    return this.http.get<MedicineModelResponse[]>(`${this.apiUrl}/search`, { params });
  }

  // Get a list of low stock medicines
  getLowStockMedicines(): Observable<MedicineModelResponse[]> {
    return this.http.get<MedicineModelResponse[]>(`${this.apiUrl}/low-stock`);
  }

  // Create new medicine using FormData for multipart request (JSON + File)
  createMedicine(medicine: MedicineModelRequest, image?: File): Observable<MedicineModelResponse> {
    const formData = new FormData();
    formData.append('medicine', new Blob([JSON.stringify(medicine)], { type: 'application/json' }));
    if (image) {
      formData.append('image', image);
    }
    return this.http.post<MedicineModelResponse>(this.apiUrl, formData);
  }

  // Update existing medicine using FormData for multipart request
  updateMedicine(id: number, medicine: MedicineModelRequest, image?: File): Observable<MedicineModelResponse> {
    const formData = new FormData();
    formData.append('medicine', new Blob([JSON.stringify(medicine)], { type: 'application/json' }));
    if (image) {
      formData.append('image', image);
    }
    return this.http.put<MedicineModelResponse>(
      `${this.apiUrl}/${id}`, formData);
  }

  // Delete medicine by database ID
  deleteMedicine(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, 
      { responseType: 'text' });
  }
}