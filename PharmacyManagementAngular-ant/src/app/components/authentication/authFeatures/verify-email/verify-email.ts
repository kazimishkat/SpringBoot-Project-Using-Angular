import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../../services/auth.service';

@Component({
  selector: 'app-verify-email',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './verify-email.html',
  styleUrl: './verify-email.css'
})
export class VerifyEmailComponent implements OnInit, OnDestroy {
  verifyForm: FormGroup;
  email = '';
  loading = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;
  countdown = 60;
  resendDisabled = true;
  private timer: any;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {
    this.verifyForm = this.fb.group({
      otp: ['', [Validators.required, Validators.pattern(/^\S+$/)]] // Accepts any valid OTP/Token code string without spaces
    });
  }

  ngOnInit(): void {
    this.email = this.route.snapshot.queryParamMap.get('email') ?? '';
    this.startTimer();
  }

  startTimer(): void {
    this.countdown = 60;
    this.resendDisabled = true;
    if (this.timer) {
      clearInterval(this.timer);
    }
    this.timer = setInterval(() => {
      this.countdown--;
      if (this.countdown <= 0) {
        this.resendDisabled = false;
        clearInterval(this.timer);
      }
    }, 1000);
  }

  verify(): void {
    if (this.verifyForm.invalid) {
      this.verifyForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = null;
    this.successMessage = null;

    const otpCode = this.verifyForm.value.otp;

    this.authService.verifyEmail(otpCode).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'OTP code verified successfully! Navigating to Password Reset...';
        setTimeout(() => {
          this.router.navigate(['/reset-password'], { queryParams: { token: otpCode } });
        }, 1500);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.status === 400 || err.status === 409
          ? 'Invalid or expired OTP token. Please enter the correct verification code.'
          : 'An error occurred during verification. Please request a new OTP code.';
      }
    });
  }

  resendOTP(): void {
    if (!this.email) {
      this.errorMessage = 'Missing email reference for resending OTP.';
      return;
    }

    this.loading = true;
    this.errorMessage = null;
    this.successMessage = null;

    this.authService.forgotPassword({ email: this.email }).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = `A fresh OTP code has been sent to ${this.email}`;
        this.startTimer();
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'Failed to dispatch a new OTP verification token. Try again later.';
      }
    });
  }

  ngOnDestroy(): void {
    if (this.timer) {
      clearInterval(this.timer);
    }
  }
}
