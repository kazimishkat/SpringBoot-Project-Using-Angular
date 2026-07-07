import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { BranchModel } from '../models/branch.model';

@Injectable({
  providedIn: 'root',
})
export class BranchService {

  private apiUrl = environment.apiUrl + 'branches';

  constructor(private http: HttpClient) { }

  getAllBranches(): Observable<BranchModel[]> {
    return this.http.get<BranchModel[]>(this.apiUrl);
  }

  getActiveBranches(): Observable<BranchModel[]> {
    return this.http.get<BranchModel[]>(`${this.apiUrl}/active`);
  }

  getBranchById(id: number): Observable<BranchModel> {
    return this.http.get<BranchModel>(`${this.apiUrl}/${id}`);
  }

  getBranchByCode(branchCode: string): Observable<BranchModel> {
    return this.http.get<BranchModel>(`${this.apiUrl}/code/${branchCode}`);
  }

  createBranch(branch: BranchModel): Observable<BranchModel> {
    return this.http.post<BranchModel>(this.apiUrl, branch);
  }

  updateBranch(id: number, branch: BranchModel): Observable<BranchModel> {
    return this.http.put<BranchModel>(`${this.apiUrl}/${id}`, branch);
  }

  deleteBranch(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}