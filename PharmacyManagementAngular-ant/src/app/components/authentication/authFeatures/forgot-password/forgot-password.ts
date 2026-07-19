import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../../services/auth.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './forgot-password.html',
  styleUrl: './forgot-password.css'
})
export class ForgotPasswordComponent {
  email = '';
  loading = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(private authService: AuthService) {}

  submit(): void {
    if (!this.email) {
      this.errorMessage = 'Please key in a valid notification email handle.';
      return;
    }

    this.loading = true;
    this.successMessage = null;
    this.errorMessage = null;

    this.authService.forgotPassword({ email: this.email }).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = `A password recovery system transmission was forwarded to ${this.email}. Check your inbox.`;
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'No active verification profile is associated with that email parameter.';
      }
    });
  }
}
