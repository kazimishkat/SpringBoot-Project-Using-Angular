import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../../services/auth.service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './reset-password.html',
  styleUrl: './reset-password.css'
})
export class ResetPasswordComponent implements OnInit {
  resetForm: FormGroup;
  token = '';
  showPassword = false;
  loading = false;
  success = false;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.resetForm = this.fb.group({
      newPassword: ['', [Validators.required, Validators.minLength(4)]],
      confirmPassword: ['', [Validators.required]]
    }, {
      validators: this.passwordMatchValidator
    });
  }

  ngOnInit(): void {
    // Extract token value parameters matching standard backend link/OTP payloads
    this.token = this.route.snapshot.queryParamMap.get('token') ?? '';
    if (!this.token) {
      this.errorMessage = 'Crucial verification token is missing. Please request a new OTP code.';
    }
  }

  passwordMatchValidator(g: FormGroup) {
    const parent = g.parent as FormGroup;
    const newPassword = g.get('newPassword')?.value;
    const confirmPassword = g.get('confirmPassword')?.value;
    return newPassword === confirmPassword ? null : { mismatch: true };
  }

  get mismatch(): boolean {
    const newPassword = this.resetForm.get('newPassword')?.value;
    const confirmPassword = this.resetForm.get('confirmPassword')?.value;
    return !!confirmPassword && newPassword !== confirmPassword;
  }

  getPasswordStrength(): { score: number; label: string; color: string; class: string } {
    const pwd = this.resetForm.get('newPassword')?.value || '';
    if (!pwd) {
      return { score: 0, label: '', color: '', class: '' };
    }
    let score = 0;
    if (pwd.length >= 4) score++;
    if (pwd.length >= 6) score++;
    if (pwd.length >= 8 && /[A-Z]/.test(pwd) && /[0-9]/.test(pwd) && /[^A-Za-z0-9]/.test(pwd)) score++;
    
    if (score === 1) {
      return { score: 33, label: 'Weak', color: '#ef4444', class: 'bg-danger' };
    } else if (score === 2) {
      return { score: 66, label: 'Medium', color: '#f59e0b', class: 'bg-warning' };
    } else if (score === 3) {
      return { score: 100, label: 'Strong', color: '#10b981', class: 'bg-success' };
    }
    return { score: 15, label: 'Weak', color: '#ef4444', class: 'bg-danger' };
  }

  submit(): void {
    if (this.resetForm.invalid || !this.token) {
      this.resetForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = null;

    const newPasswordVal = this.resetForm.get('newPassword')?.value;

    this.authService.resetPassword({ token: this.token, newPassword: newPasswordVal }).subscribe({
      next: () => {
        this.loading = false;
        this.success = true;
        // Allows user context to visually read the completion alert before redirect executes
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.status === 400 
          ? 'The password modification window has expired. Please re-trigger the forgot password script.' 
          : 'Failed to process updates. Please try again later.';
      }
    });
  }
}