import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../../services/user.service';
import { BranchService } from '../../../../services/branch.service';
import { UserRequest, UserRole } from '../../../../models/user.model';

@Component({
  selector: 'app-user-edit',
  imports: [FormsModule, CommonModule],
  templateUrl: './user-edit.html',
  styleUrl: './user-edit.css'
})
export class UserEdit implements OnInit {

  @ViewChild('userForm') userForm!: NgForm;

  userId!: number;
  branches: any[] = [];
  roles = Object.values(UserRole);

  userRequest!: UserRequest;
  selectedFile?: File;
  imagePreview: string | null = null;
  existingImage: string | null = null;

  submitted = false;
  errorMessage = '';

  constructor(
    private userService: UserService,
    private branchService: BranchService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadActiveBranchesLookups();
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.userId = Number(id);
        this.loadUserRecord();
      }
    });
  }

  loadActiveBranchesLookups(): void {
    this.branchService.getActiveBranches().subscribe(data => { 
      this.branches = data || []; 
      this.cdr.markForCheck(); 
    });
  }

  loadUserRecord(): void {
    this.userService.getUserById(this.userId).subscribe({
      next: (res) => {
        this.userRequest = {
          username: res.username,
          password: '', // Kept blank unless modifications are explicitly passed
          fullName: res.fullName,
          email: res.email,
          phone: res.phone,
          role: res.role,
          branchId: res.branchId || undefined,
          isActive: res.enabled
        };
        this.existingImage = res.image || null;
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Failed to parse user record metadata', err)
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

  updateUser(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.userForm.invalid) {
      return;
    }

    this.userService.updateUser(this.userId, this.userRequest, this.selectedFile).subscribe({
      next: () => {
        alert('User details updated successfully.');
        this.router.navigate(['/dashboard/users']);
      },
      error: (err) => this.handleError('Update rejected by server', err)
    });
  }

  cancel(): void {
    this.router.navigate(['/dashboard/users']);
  }

  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}