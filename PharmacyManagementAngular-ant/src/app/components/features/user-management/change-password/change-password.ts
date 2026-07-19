import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule, Location } from '@angular/common';
import { UserService } from '../../../../services/user.service';
import { ChangePasswordRequest } from '../../../../models/user.model';

@Component({
  selector: 'app-change-password',
  imports: [FormsModule, CommonModule],
  templateUrl: './change-password.html',
  styleUrl: './change-password.css'
})
export class ChangePassword implements OnInit {

  @ViewChild('pwdForm') pwdForm!: NgForm;

  activeSessionUserId = 1; // Simulated session index locator mapping
  pwdRequest!: ChangePasswordRequest;
  
  submitted = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private userService: UserService,
    private location: Location,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.resetForm();
  }

  changePassword(): void {
    this.submitted = true;
    this.errorMessage = '';
    this.successMessage = '';

    // Frontend pre-validation assertion checklist rule
    if (this.pwdRequest.newPassword !== this.pwdRequest.confirmPassword) {
      this.errorMessage = 'New password and confirmation password tokens mismatch.';
      return;
    }

    if (this.pwdForm.invalid) {
      return;
    }

    this.userService.changePassword(this.activeSessionUserId, this.pwdRequest).subscribe({
      next: (res) => {
        // Evaluate dynamic status parameters mapped from Spring controller body return rules
        if (res.success) {
          this.successMessage = res.message || 'Credential arrays modified successfully inside vault.';
          alert(this.successMessage);
          this.resetForm();
        } else {
          this.errorMessage = res.message || 'Validation rejected by crypt engine policies.';
          this.cdr.markForCheck();
        }
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Vault patch process failed: ${err.error?.message || err.error || err.message || 'Server Fault'}`;
        this.cdr.markForCheck();
      }
    });
  }

  resetForm(): void {
    this.pwdRequest = {
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    };
    this.submitted = false;
    if (this.pwdForm) {
      this.pwdForm.resetForm();
    }
    this.cdr.markForCheck();
  }

  goBack(): void {
    this.location.back();
  }
}