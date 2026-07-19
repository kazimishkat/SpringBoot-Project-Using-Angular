import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { UserRequest, UserResponse, ChangePasswordRequest, ChangePasswordResponse } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  
  // API base route targeting spring boot controller paths
  private apiUrl = environment.apiUrl + 'users';

  constructor(private http: HttpClient) {}

  getUsers(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(this.apiUrl);
  }

  getUserById(id: number): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.apiUrl}/${id}`);
  }

  createUser(dto: UserRequest): Observable<UserResponse> {
    return this.http.post<UserResponse>(this.apiUrl, dto);
  }

  updateUser(id: number, dto: UserRequest): Observable<UserResponse> {
    return this.http.put<UserResponse>(`${this.apiUrl}/${id}`, dto);
  }

  deleteUser(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }

  toggleUserStatus(id: number, enabled: boolean): Observable<UserResponse> {
    const params = new HttpParams().set('enabled', enabled.toString());
    return this.http.patch<UserResponse>(`${this.apiUrl}/${id}/status`, {}, { params });
  }

  // ── Profile Mappings Fallbacks (Leverages target ID resolutions) ──
  getProfile(id: number): Observable<UserResponse> {
    return this.getUserById(id);
  }

  updateProfile(id: number, dto: UserRequest): Observable<UserResponse> {
    return this.updateUser(id, dto);
  }

  // Maps backend PATCH /api/users/{id}/change-password route exactly
  changePassword(id: number, dto: ChangePasswordRequest): Observable<ChangePasswordResponse> {
    return this.http.patch<ChangePasswordResponse>(`${this.apiUrl}/${id}/change-password`, dto);
  }
}