import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../../services/auth.service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './reset-password.html',
  styleUrl: './reset-password.css'
})
export class ResetPasswordComponent implements OnInit {
  token = '';
  newPassword = '';
  confirmPassword = '';
  
  showPassword = false;
  loading = false;
  success = false;
  errorMessage: string | null = null;

  constructor(
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Extract token value parameters matching standard backend link payloads
    this.token = this.route.snapshot.queryParamMap.get('token') ?? '';
    if (!this.token) {
      this.errorMessage = 'Crucial operational verification values are missing. Request a new token.';
    }
  }

  get mismatch(): boolean {
    return !!this.confirmPassword && this.newPassword !== this.confirmPassword;
  }

  submit(): void {
    if (this.mismatch || !this.token) return;
    
    if (this.newPassword.length < 4) {
      this.errorMessage = 'Security policy requires a length greater than or equal to 4 values.';
      return;
    }

    this.loading = true;
    this.errorMessage = null;

    this.authService.resetPassword({ token: this.token, newPassword: this.newPassword }).subscribe({
      next: () => {
        this.loading = false;
        this.success = true;
        // Allows user context to visually read the completion alert before redirect loops execute
        setTimeout(() => this.router.navigate(['/login']), 3000);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.status === 400 
          ? 'The password modification timeframe window expired. Re-trigger the forgot script.' 
          : 'Failed processing updates. Please try again.';
      }
    });
  }
}