import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { UserService } from '../../../../services/user.service';
import { BranchService } from '../../../../services/branch.service';
import { UserRequest, UserRole } from '../../../../models/user.model';

@Component({
  selector: 'app-user-add',
  imports: [FormsModule, CommonModule],
  templateUrl: './user-add.html',
  styleUrl: './user-add.css'
})
export class UserAdd implements OnInit {

  @ViewChild('userForm') userForm!: NgForm;

  branches: any[] = [];
  roles = Object.values(UserRole);

  userRequest!: UserRequest;
  selectedFile?: File;
  imagePreview: string | null = null;

  submitted = false;
  errorMessage = '';

  constructor(
    private userService: UserService,
    private branchService: BranchService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.resetForm();
    this.loadActiveBranchesLookups();
  }

  loadActiveBranchesLookups(): void {
    this.branchService.getActiveBranches().subscribe(data => { 
      this.branches = data || []; 
      this.cdr.markForCheck(); 
    });
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

  saveUser(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.userForm.invalid) {
      return;
    }

    // Pass both DTO and image file to UserService
    this.userService.createUser(this.userRequest, this.selectedFile).subscribe({
      next: () => {
        alert('User created successfully. Activation email dispatched.');
        this.router.navigate(['/dashboard/users']);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Creation failed: ${err.error || err.message || 'Server Validation Failure'}`;
        this.cdr.markForCheck();
      }
    });
  }

  resetForm(): void {
    this.userRequest = {
      username: '',
      password: '',
      fullName: '',
      email: '',
      phone: '',
      role: UserRole.SALESMAN,
      branchId: undefined,
      isActive: true
    };
    this.selectedFile = undefined;
    this.imagePreview = null;
    this.submitted = false;
    this.errorMessage = '';

    if (this.userForm) {
      this.userForm.resetForm({ role: UserRole.SALESMAN });
    }
    this.cdr.markForCheck();
  }

  cancel(): void {
    this.router.navigate(['/dashboard/users']);
  }
}