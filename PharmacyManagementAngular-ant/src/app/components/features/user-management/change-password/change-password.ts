import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule, Location } from '@angular/common';
import { UserService } from '../../../../services/user.service';
import { StorageService } from '../../../../services/storage.service';
import { ChangePasswordRequest } from '../../../../models/user.model';

@Component({
  selector: 'app-change-password',
  imports: [FormsModule, CommonModule],
  templateUrl: './change-password.html',
  styleUrl: './change-password.css'
})
export class ChangePassword implements OnInit {

  @ViewChild('pwdForm') pwdForm!: NgForm;

  activeSessionUserId!: number;
  pwdRequest!: ChangePasswordRequest;
  
  submitted = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private userService: UserService,
    private storageService: StorageService,
    private location: Location,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const user = this.storageService.getUser();
    if (user && user.userId) {
      this.activeSessionUserId = user.userId;
    } else {
      this.activeSessionUserId = 1; // Fallback ID
    }
    this.resetForm();
  }

  changePassword(): void {
    this.submitted = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.pwdRequest.newPassword !== this.pwdRequest.confirmPassword) {
      this.errorMessage = 'New password and confirmation password mismatch.';
      return;
    }

    if (this.pwdForm.invalid) {
      return;
    }

    this.userService.changePassword(this.activeSessionUserId, this.pwdRequest).subscribe({
      next: (res) => {
        if (res.success) {
          this.successMessage = res.message || 'Password changed successfully.';
          alert(this.successMessage);
          this.resetForm();
        } else {
          this.errorMessage = res.message || 'Password change request rejected.';
          this.cdr.markForCheck();
        }
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Password change failed: ${err.error?.message || err.error || err.message || 'Server Exception'}`;
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