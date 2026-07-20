import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CustomerService } from '../../../../services/customer.service';
import { CustomerResponse } from '../../../../models/customer.model';

@Component({
  selector: 'app-customer-list',
  imports: [FormsModule, CommonModule],
  templateUrl: './customer-list.html',
  styleUrl: './customer-list.css'
})
export class CustomerListComponent implements OnInit {

  customers: CustomerResponse[] = [];
  masterLogs: CustomerResponse[] = [];

  searchQuery = '';
  selectedType: 'ALL' | 'WALK_IN' | 'ONLINE' = 'ALL';
  errorMessage = '';

  constructor(
    private customerService: CustomerService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.errorMessage = '';
    this.customerService.getAllCustomers().subscribe({
      next: (data) => {
        this.masterLogs = data || [];
        this.applyFilter();
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Failed to fetch customer directory', err)
    });
  }

  searchCustomer(): void {
    this.applyFilter();
  }

  filterByType(): void {
    this.applyFilter();
  }

  applyFilter(): void {
    let result = [...this.masterLogs];

    if (this.searchQuery.trim()) {
      const q = this.searchQuery.trim().toLowerCase();
      result = result.filter(c => 
        c.name.toLowerCase().includes(q) ||
        c.phone.toLowerCase().includes(q) ||
        (c.email && c.email.toLowerCase().includes(q))
      );
    }

    if (this.selectedType === 'WALK_IN') {
      result = result.filter(c => !c.userId);
    } else if (this.selectedType === 'ONLINE') {
      result = result.filter(c => !!c.userId);
    }

    this.customers = result;
    this.cdr.markForCheck();
  }

  addCustomer(): void {
    this.router.navigate(['/dashboard/customers/add']);
  }

  editCustomer(id: number): void {
    this.router.navigate(['/dashboard/customers/edit', id]);
  }

  viewDetails(id: number): void {
    this.router.navigate(['/dashboard/customers', id]);
  }

  deleteCustomer(id: number): void {
    if (confirm('Are you sure you want to delete this customer record?')) {
      this.customerService.deleteCustomer(id).subscribe({
        next: () => {
          alert('Customer record removed successfully.');
          this.loadCustomers();
        },
        error: (err) => this.handleError('Failed to delete customer', err)
      });
    }
  }

  refresh(): void {
    this.searchQuery = '';
    this.selectedType = 'ALL';
    this.loadCustomers();
  }

  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}