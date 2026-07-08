import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { MedicineModel } from '../models/medicine.model';

@Injectable({
  providedIn: 'root',
})
export class MedicineService {

  private apiUrl = environment.apiUrl + 'medicines';

  constructor(private http: HttpClient) { }

  // ==========================================
  // Medicine CRUD & Dynamic Search Operations
  // ==========================================

  /**
   * Fetch all medicine items across inventories.
   * GET /api/v1/medicines
   */
  getAllMedicines(): Observable<MedicineModel[]> {
    return this.http.get<MedicineModel[]>(this.apiUrl);
  }

  /**
   * Retrieve a core medicine dataset row matching its unique primary database identifier.
   * GET /api/v1/medicines/{id}
   */
  getMedicineById(id: number): Observable<MedicineModel> {
    return this.http.get<MedicineModel>(`${this.apiUrl}/${id}`);
  }

  /**
   * Fetch a medicine structure profile using its tracking SKU string code.
   * GET /api/v1/medicines/code/{medicineCode}
   */
  getMedicineByCode(medicineCode: string): Observable<MedicineModel> {
    return this.http.get<MedicineModel>(`${this.apiUrl}/code/${medicineCode}`);
  }

  /**
   * Search and filter medicines by custom brand name keywords.
   * GET /api/v1/medicines/search?brandName={brandName}
   */
  searchMedicinesByBrandName(brandName: string): Observable<MedicineModel[]> {
    return this.http.get<MedicineModel[]>(`${this.apiUrl}/search`, {
      params: { brandName }
    });
  }

  /**
   * Fetch active database medicines falling beneath specified safe reorder margins.
   * GET /api/v1/medicines/low-stock
   */
  getLowStockMedicines(): Observable<MedicineModel[]> {
    return this.http.get<MedicineModel[]>(`${this.apiUrl}/low-stock`);
  }

  /**
   * Pack request fields and binary multi-part graphics payloads into a multipart tracking request form context.
   * POST /api/v1/medicines
   */
  createMedicine(medicine: MedicineModel, image?: File): Observable<MedicineModel> {
    const formData = new FormData();
    formData.append('medicine', new Blob([JSON.stringify(medicine)], { type: 'application/json' }));
    if (image) {
      formData.append('image', image);
    }
    return this.http.post<MedicineModel>(this.apiUrl, formData);
  }

  /**
   * Mutate properties dynamically for an existing record mapping including multipart files updates.
   * PUT /api/v1/medicines/{id}
   */
  updateMedicine(id: number, medicine: MedicineModel, image?: File): Observable<MedicineModel> {
    const formData = new FormData();
    formData.append('medicine', new Blob([JSON.stringify(medicine)], { type: 'application/json' }));
    if (image) {
      formData.append('image', image);
    }
    return this.http.put<MedicineModel>(`${this.apiUrl}/${id}`, formData);
  }

  /**
   * Remove structural core profile information matching its primary row id.
   * DELETE /api/v1/medicines/{id}
   */
  deleteMedicine(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}