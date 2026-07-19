import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { UserService } from '../../../../services/user.service';
import { UserResponse, UserRole } from '../../../../models/user.model';

@Component({
  selector: 'app-user-list',
  imports: [FormsModule, CommonModule],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css'
})
export class UserList implements OnInit {

  users: UserResponse[] = [];
  masterLogs: UserResponse[] = [];
  roles = Object.values(UserRole);

  searchQuery = '';
  selectedRole: UserRole | '' = '';
  errorMessage = '';

  constructor(
    private userService: UserService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.errorMessage = '';
    this.userService.getUsers().subscribe({
      next: (data) => {
        this.masterLogs = data || [];
        this.users = [...this.masterLogs];
        this.cdr.markForCheck(); // Recalculate component tree graphics
      },
      error: (err) => this.handleError('Failed to fetch personnel directory logs', err)
    });
  }

  searchUser(): void {
    this.applyLocalFiltersPipeline();
  }

  filterByRole(): void {
    this.applyLocalFiltersPipeline();
  }

  toggleStatus(id: number, currentStatus: boolean): void {
    const nextStatus = !currentStatus;
    this.userService.toggleUserStatus(id, nextStatus).subscribe({
      next: () => {
        alert(`Account state shifted successfully.`);
        this.loadUsers();
      },
      error: (err) => this.handleError('Status alteration rejected', err)
    });
  }

  addUser(): void {
    this.router.navigate(['/users/add']);
  }

  editUser(id: number): void {
    this.router.navigate(['/users/edit', id]);
  }

  viewDetails(id: number): void {
    this.router.navigate(['/users', id]);
  }

  deleteUser(id: number): void {
    if (confirm('Permanently wipe this user account credential ledger line?')) {
      this.userService.deleteUser(id).subscribe({
        next: () => {
          alert('User records deleted from central schema.');
          this.loadUsers();
        },
        error: (err) => this.handleError('Deletion procedure aborted by security rules', err)
      });
    }
  }

  refresh(): void {
    this.searchQuery = '';
    this.selectedRole = '';
    this.loadUsers();
  }

  private applyLocalFiltersPipeline(): void {
    let result = [...this.masterLogs];

    if (this.searchQuery.trim()) {
      const q = this.searchQuery.trim().toLowerCase();
      result = result.filter(x => 
        x.fullName.toLowerCase().includes(q) || 
        x.username.toLowerCase().includes(q) || 
        x.email.toLowerCase().includes(q)
      );
    }

    if (this.selectedRole) {
      result = result.filter(x => x.role === this.selectedRole);
    }

    this.users = result;
    this.cdr.markForCheck();
  }

  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}
