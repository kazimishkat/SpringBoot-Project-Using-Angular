import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../../services/auth.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './forgot-password.html',
  styleUrl: './forgot-password.css'
})
export class ForgotPasswordComponent {
  forgotForm: FormGroup;
  loading = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.forgotForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  submit(): void {
    if (this.forgotForm.invalid) {
      this.forgotForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.successMessage = null;
    this.errorMessage = null;

    const emailVal = this.forgotForm.value.email;

    this.authService.forgotPassword({ email: emailVal }).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = `One-Time Password (OTP) has been dispatched to ${emailVal}. Redirecting to verification...`;
        setTimeout(() => {
          this.router.navigate(['/verify-email'], { queryParams: { email: emailVal } });
        }, 2000);
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'No registered pharmacy user profile is associated with this email.';
      }
    });
  }
}
