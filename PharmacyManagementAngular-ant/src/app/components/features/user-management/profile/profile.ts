import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserService } from '../../../../services/user.service';
import { UserResponse, UserRequest } from '../../../../models/user.model';

@Component({
  selector: 'app-profile',
  imports: [FormsModule, CommonModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css'
})
export class Profile implements OnInit {

  @ViewChild('profileForm') profileForm!: NgForm;

  activeSessionUserId = 1; // Simulated active localized credential identity index token
  profileDetails: UserResponse | null = null;
  
  editRequest!: UserRequest;
  isEditMode = false;
  submitted = false;
  errorMessage = '';

  constructor(
    private userService: UserService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadProfileDetails();
  }

  loadProfileDetails(): void {
    this.errorMessage = '';
    this.isEditMode = false;
    this.userService.getProfile(this.activeSessionUserId).subscribe({
      next: (data) => {
        this.profileDetails = data;
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Session identity card synchronization failed', err)
    });
  }

  enableEditMode(): void {
    if (!this.profileDetails) return;
    this.isEditMode = true;
    this.submitted = false;

    this.editRequest = {
      username: this.profileDetails.username,
      fullName: this.profileDetails.fullName,
      email: this.profileDetails.email,
      phone: this.profileDetails.phone,
      role: this.profileDetails.role,
      branchId: this.profileDetails.branchId || undefined,
      isActive: this.profileDetails.enabled
    };
    this.cdr.markForCheck();
  }

  updateProfile(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.profileForm.invalid) {
      return;
    }

    this.userService.updateProfile(this.activeSessionUserId, this.editRequest).subscribe({
      next: () => {
        alert('Personal dashboard details updated successfully.');
        this.loadProfileDetails();
      },
      error: (err) => this.interceptError('Profile payload mutations rejected', err)
    });
  }

  cancelEdit(): void {
    this.isEditMode = false;
    this.submitted = false;
    this.errorMessage = '';
    this.cdr.markForCheck();
  }

  private interceptError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}