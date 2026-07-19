import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../../services/auth.service';
import { LoginRequest } from '../../../../models/auth.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  // Binds directly to the modified structural DTO requirements
  dto: LoginRequest = { usernameOrEmail: '', password: '' };
  
  showPassword = false;
  loading = false;
  errorMessage: string | null = null;

  constructor(
    private authService: AuthService, 
    private router: Router
  ) {}

  login(): void {
    if (!this.dto.usernameOrEmail || !this.dto.password) {
      this.errorMessage = 'Please provide both identification credentials.';
      return;
    }

    this.loading = true;
    this.errorMessage = null;

    this.authService.login(this.dto).subscribe({
      next: () => {
        this.loading = false;
        // Directs user to the central dashboard router gatekeeper
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.loading = false;
        if (err.status === 401) {
          this.errorMessage = 'Invalid username/email or password.';
        } else if (err.status === 403) {
          this.errorMessage = 'Your pharmacy worker profile profile is inactive or disabled.';
        } else {
          this.errorMessage = 'A network synchronization problem occurred. Please retry.';
        }
      }
    });
  }
}
