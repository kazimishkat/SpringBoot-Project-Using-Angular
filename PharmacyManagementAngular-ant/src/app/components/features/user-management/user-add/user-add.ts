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
    this.branchService.getActiveBranches().subscribe(data => { this.branches = data || []; this.cdr.markForCheck(); });
  }

  saveUser(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.userForm.invalid) {
      return;
    }

    this.userService.createUser(this.userRequest).subscribe({
      next: () => {
        alert('Fresh account parameters committed and securely hashed.');
        this.router.navigate(['/users']);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Creation rejected: ${err.error || err.message || 'Server Validation Failure'}`;
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
    this.submitted = false;
    this.errorMessage = '';
    if (this.userForm) {
      this.userForm.resetForm({ role: UserRole.SALESMAN });
    }
    this.cdr.markForCheck();
  }

  cancel(): void {
    this.router.navigate(['/users']);
  }
}