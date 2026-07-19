import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { ForgotPasswordRequest, LoginRequest, LoginResponse, ResetPasswordRequest } from '../models/auth.model';
import { StorageService } from './storage.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  // Points directly to your Spring boot /api/auth path mappings
  private apiUrl = `${environment.apiUrl}auth`;

  constructor(
    private http: HttpClient,
    private storage: StorageService,
    private router: Router
  ) {}

  // ── Login ────────────────────────────────────────────
  login(dto: LoginRequest): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${this.apiUrl}/login`, dto)
      .pipe(
        tap((res) => this.storage.saveSession(res))
      );
  }

  // ── Logout ───────────────────────────────────────────
  logout(): void {
    this.storage.clearSession();
    this.router.navigate(['/login']);
  }

  // ── Forgot Password ──────────────────────────────────
  forgotPassword(dto: ForgotPasswordRequest): Observable<string> {
    return this.http.post(
      `${this.apiUrl}/forgot-password`,
      dto,
      { responseType: 'text' }
    );
  }

  // ── Reset Password ───────────────────────────────────
  resetPassword(dto: ResetPasswordRequest): Observable<string> {
    return this.http.post(
      `${this.apiUrl}/reset-password`,
      dto,
      { responseType: 'text' }
    );
  }

  // ── Verify Email ─────────────────────────────────────
  verifyEmail(token: string): Observable<string> {
    return this.http.get(
      `${this.apiUrl}/verify-email`,
      { params: { token }, responseType: 'text' }
    );
  }

  // ── Authorization Helpers ─────────────────────────────
  isLoggedIn(): boolean {
    return this.storage.isLoggedIn();
  }

  getRole(): string | null {
    return this.storage.getRole();
  }

  getUser(): LoginResponse | null {
    return this.storage.getUser();
  }
}