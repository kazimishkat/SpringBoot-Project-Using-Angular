import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute, RouterLink, RouterModule } from '@angular/router';
import { UserService } from '../../../../services/user.service';
import { UserResponse } from '../../../../models/user.model';

@Component({
  selector: 'app-user-details',
  imports: [CommonModule, RouterModule],
  templateUrl: './user-details.html',
  styleUrl: './user-details.css'
})
export class UserDetails implements OnInit {

  userId!: number;
  userDetails: UserResponse | null = null;
  errorMessage = '';

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private location: Location,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.userId = Number(id);
        this.loadUserDetails();
      }
    });
  }

  loadUserDetails(): void {
    this.errorMessage = '';
    this.userService.getUserById(this.userId).subscribe({
      next: (data) => {
        this.userDetails = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Failed to process individual parameter sheets cards: ${err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  goBack(): void {
    this.location.back();
  }
}
