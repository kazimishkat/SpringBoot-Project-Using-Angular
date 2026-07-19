import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../../services/auth.service';

type VerifyState = 'loading' | 'success' | 'error' | 'no-token';

@Component({
  selector: 'app-verify-email',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './verify-email.html',
  styleUrl: './verify-email.css'
})
export class VerifyEmailComponent implements OnInit, OnDestroy {
  state: VerifyState = 'loading';
  errorMessage = '';
  countdown = 5;
  private timer: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');

    if (!token) {
      this.state = 'no-token';
      return;
    }

    this.authService.verifyEmail(token).subscribe({
      next: () => {
        this.state = 'success';
        this.startCountdown();
      },
      error: (err) => {
        this.state = 'error';
        // Intercepts structural 400/409 conflicts directly tied to database validation tasks
        this.errorMessage = err.status === 400 || err.status === 409
          ? (err.error || 'This digital credential verification string is unreadable or has already expired.')
          : 'Something went wrong while verifying. Please request a new authentication profile setup transmission.';
      }
    });
  }

  private startCountdown(): void {
    this.timer = setInterval(() => {
      this.countdown--;
      if (this.countdown <= 0) {
        this.clearTimerAndRedirect();
      }
    }, 1000);
  }

  goToLogin(): void {
    this.clearTimerAndRedirect();
  }

  private clearTimerAndRedirect(): void {
    if (this.timer) {
      clearInterval(this.timer);
    }
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    if (this.timer) {
      clearInterval(this.timer);
    }
  }
}
