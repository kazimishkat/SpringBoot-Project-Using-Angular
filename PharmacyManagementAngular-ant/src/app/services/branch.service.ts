import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { BranchRequest, BranchResponse } from '../models/branch.model';

@Injectable({
  providedIn: 'root',
})
export class BranchService {
  
  // API endpoint matching backend controller routing
  private apiUrl = environment.apiUrl + 'branches';

  constructor(private http: HttpClient) {}

  // Fetch all branches from the database registry
  getAllBranches(): Observable<BranchResponse[]> {
    return this.http.get<BranchResponse[]>(this.apiUrl);
  }

  // Fetch only active branches
  getActiveBranches(): Observable<BranchResponse[]> {
    return this.http.get<BranchResponse[]>(`${this.apiUrl}/active`);
  }

  // Find a specific branch by its database primary ID
  getBranchById(id: number): Observable<BranchResponse> {
    return this.http.get<BranchResponse>(`${this.apiUrl}/${id}`);
  }

  // Find a single branch matching a unique string code identifier
  getBranchByCode(branchCode: string): Observable<BranchResponse> {
    return this.http.get<BranchResponse>(`${this.apiUrl}/code/${branchCode}`);
  }

  // Search branches by name utilizing query parameter mapping
  searchBranchesByName(name: string): Observable<BranchResponse[]> {
    const params = new HttpParams().set('name', name);
    return this.http.get<BranchResponse[]>(`${this.apiUrl}/search`, { params });
  }

  // Create a new branch profile instance layout
  createBranch(dto: BranchRequest): Observable<BranchResponse> {
    return this.http.post<BranchResponse>(this.apiUrl, dto);
  }

  // Update existing branch structural tracking configurations
  updateBranch(id: number, dto: BranchRequest): Observable<BranchResponse> {
    return this.http.put<BranchResponse>(`${this.apiUrl}/${id}`, dto);
  }

  // Completely wipe branch reference completely out of active databases
  deleteBranch(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}