import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserService } from '../../../../services/user.service';
import { StorageService } from '../../../../services/storage.service';
import { UserResponse, UserRequest } from '../../../../models/user.model';

@Component({
  selector: 'app-profile',
  imports: [FormsModule, CommonModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css'
})
export class Profile implements OnInit {

  @ViewChild('profileForm') profileForm!: NgForm;

  activeSessionUserId!: number;
  profileDetails: UserResponse | null = null;
  
  editRequest!: UserRequest;
  selectedFile?: File;
  imagePreview: string | null = null;

  isEditMode = false;
  submitted = false;
  errorMessage = '';

  constructor(
    private userService: UserService,
    private storageService: StorageService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const user = this.storageService.getUser();
    if (user && user.userId) {
      this.activeSessionUserId = user.userId;
    } else {
      this.activeSessionUserId = 1;
    }
    this.loadProfileDetails();
  }

  loadProfileDetails(): void {
    this.errorMessage = '';
    this.isEditMode = false;
    this.selectedFile = undefined;
    this.imagePreview = null;

    this.userService.getProfile(this.activeSessionUserId).subscribe({
      next: (data) => {
        this.profileDetails = data;
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Session identity synchronization failed', err)
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

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];

      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result as string;
        this.cdr.markForCheck();
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  updateProfile(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.profileForm.invalid) {
      return;
    }

    this.userService.updateProfile(this.activeSessionUserId, this.editRequest, this.selectedFile).subscribe({
      next: () => {
        alert('Personal profile updated successfully.');
        this.loadProfileDetails();
      },
      error: (err) => this.interceptError('Profile update failed', err)
    });
  }

  cancelEdit(): void {
    this.isEditMode = false;
    this.submitted = false;
    this.errorMessage = '';
    this.selectedFile = undefined;
    this.imagePreview = null;
    this.cdr.markForCheck();
  }

  private interceptError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}