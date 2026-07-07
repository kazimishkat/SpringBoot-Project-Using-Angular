import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { SupplierModel } from '../models/supplier.model';

@Injectable({
  providedIn: 'root',
})
export class SupplierService {

  private apiUrl = environment.apiUrl + 'suppliers';

  constructor(private http: HttpClient) { }

  getAllSuppliers(): Observable<SupplierModel[]> {
    return this.http.get<SupplierModel[]>(this.apiUrl);
  }

 
  getSupplierById(id: number): Observable<SupplierModel> {
    return this.http.get<SupplierModel>(`${this.apiUrl}/${id}`);
  }

  getSupplierByCode(supplierCode: string): Observable<SupplierModel> {
    return this.http.get<SupplierModel>(`${this.apiUrl}/code/${supplierCode}`);
  }

  createSupplier(supplier: SupplierModel): Observable<SupplierModel> {
    return this.http.post<SupplierModel>(this.apiUrl, supplier);
  }

  updateSupplier(id: number, supplier: SupplierModel): Observable<SupplierModel> {
    return this.http.put<SupplierModel>(`${this.apiUrl}/${id}`, supplier);
  }

  deleteSupplier(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}