import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../../services/auth.service';

@Component({
  selector: 'app-verify-email',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './verify-email.html',
  styleUrl: './verify-email.css'
})
export class VerifyEmailComponent implements OnInit {
  loading = true;
  successMessage: string | null = null;
  errorMessage: string | null = null;
  token: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // Read the 'token' parameter attached to the email link URL
    this.token = this.route.snapshot.queryParamMap.get('token');

    if (!this.token) {
      this.loading = false;
      this.errorMessage = 'Verification token is missing from the link. Please check your verification email.';
      return;
    }

    // Automatically invoke verification call upon component initialization
    this.executeEmailVerification(this.token);
  }

  executeEmailVerification(token: string): void {
    this.loading = true;
    this.errorMessage = null;
    this.successMessage = null;

    this.authService.verifyEmail(token).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Your email has been verified successfully! Redirecting to login...';
        
        // Redirect user to login page after successful activation
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2500);
      },
      error: (err) => {
        this.loading = false;
        console.error(err);
        this.errorMessage = err.status === 400 || err.status === 409
          ? 'Invalid, expired, or already processed verification token.'
          : 'An error occurred while activating your account. Please try again or contact support.';
      }
    });
  }
}