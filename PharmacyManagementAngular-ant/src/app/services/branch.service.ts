import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { BranchRequest, BranchResponse } from '../models/branch.model';

@Injectable({
  providedIn: 'root',
})
export class BranchService {
  
  // API endpoint for branches
  private apiUrl = environment.apiUrl + 'branches';

  constructor(private http: HttpClient) {}

  // Fetch all branches
  getAllBranches(): Observable<BranchResponse[]> {
    return this.http.get<BranchResponse[]>(this.apiUrl);
  }

  // Fetch only active branches
  getActiveBranches(): Observable<BranchResponse[]> {
    return this.http.get<BranchResponse[]>(`${this.apiUrl}/active`);
  }

  // Get single branch by primary ID
  getBranchById(id: number): Observable<BranchResponse> {
    return this.http.get<BranchResponse>(`${this.apiUrl}/${id}`);
  }

  // Get single branch by code identifier
  getBranchByCode(branchCode: string): Observable<BranchResponse> {
    return this.http.get<BranchResponse>(`${this.apiUrl}/code/${branchCode}`);
  }

  // Create a new branch entry
  createBranch(dto: BranchRequest): Observable<BranchResponse> {
    return this.http.post<BranchResponse>(this.apiUrl, dto);
  }

  // Update existing branch information
  updateBranch(id: number, dto: BranchRequest): Observable<BranchResponse> {
    return this.http.put<BranchResponse>(`${this.apiUrl}/${id}`, dto);
  }

  // Delete branch record completely by ID
  deleteBranch(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}