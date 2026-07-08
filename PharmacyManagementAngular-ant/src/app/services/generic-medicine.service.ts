import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { GenericMedicineModel } from '../models/generic-medicine.model';

@Injectable({
  providedIn: 'root',
})
export class GenericMedicineService {

  private apiUrl = environment.apiUrl + 'generic-medicines';

  constructor(private http: HttpClient) { }

  // ==========================================
  // Generic Medicine CRUD Operations
  // ==========================================

  getAllGenericMedicines(): Observable<GenericMedicineModel[]> {
    return this.http.get<GenericMedicineModel[]>(this.apiUrl);
  }

  getGenericMedicineById(id: number): Observable<GenericMedicineModel> {
    return this.http.get<GenericMedicineModel>(`${this.apiUrl}/${id}`);
  }

  getByGenericName(genericName: string): Observable<GenericMedicineModel> {
    return this.http.get<GenericMedicineModel>(`${this.apiUrl}/name/${genericName}`);
  }

  createGenericMedicine(genericMedicine: GenericMedicineModel): Observable<GenericMedicineModel> {
    return this.http.post<GenericMedicineModel>(this.apiUrl, genericMedicine);
  }

  updateGenericMedicine(id: number, genericMedicine: GenericMedicineModel): Observable<GenericMedicineModel> {
    return this.http.put<GenericMedicineModel>(`${this.apiUrl}/${id}`, genericMedicine);
  }

  deleteGenericMedicine(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}