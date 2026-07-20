import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CustomerService } from '../../../../services/customer.service';
import { CustomerResponse, CustomerRequest } from '../../../../models/customer.model';

@Component({
  selector: 'app-customer-details',
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './customer-details.html',
  styleUrl: './customer-details.css'
})
export class CustomerDetails implements OnInit {

  customerId!: number;
  customerDetails: CustomerResponse | null = null;
  errorMessage = '';

  // 🟢 Scenario 3: Convert Walk-in Customer to Online Account Modal State
  showAccountModal = false;
  accountData = {
    username: '',
    email: '',
    password: '',
    confirmPassword: ''
  };
  accountModalError = '';
  isAccountSubmitting = false;

  constructor(
    private customerService: CustomerService,
    private route: ActivatedRoute,
    private location: Location,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.customerId = Number(id);
        this.loadCustomerDetails();
      }
    });
  }

  loadCustomerDetails(): void {
    this.errorMessage = '';
    this.customerService.getCustomerById(this.customerId).subscribe({
      next: (data) => {
        this.customerDetails = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Failed to load customer details: ${err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  openAccountModal(): void {
    if (!this.customerDetails) return;
    this.accountData = {
      username: '',
      email: this.customerDetails.email || '',
      password: '',
      confirmPassword: ''
    };
    this.accountModalError = '';
    this.showAccountModal = true;
    this.cdr.markForCheck();
  }

  closeAccountModal(): void {
    this.showAccountModal = false;
    this.cdr.markForCheck();
  }

  createAccountForExistingCustomer(): void {
    this.accountModalError = '';

    if (!this.accountData.username || !this.accountData.email || !this.accountData.password) {
      this.accountModalError = 'All account fields are required.';
      return;
    }

    if (this.accountData.password !== this.accountData.confirmPassword) {
      this.accountModalError = 'Passwords do not match.';
      return;
    }

    if (!this.customerDetails) return;

    this.isAccountSubmitting = true;

    // Build update DTO with createAccount = true
    const updateDto: CustomerRequest = {
      name: this.customerDetails.name,
      phone: this.customerDetails.phone,
      email: this.accountData.email,
      gender: this.customerDetails.gender,
      age: this.customerDetails.age,
      address: this.customerDetails.address,
      createAccount: true,
      username: this.accountData.username,
      password: this.accountData.password
    };

    this.customerService.updateCustomer(this.customerId, updateDto).subscribe({
      next: () => {
        alert('Account created and linked successfully! Verification email dispatched.');
        this.isAccountSubmitting = false;
        this.showAccountModal = false;
        this.loadCustomerDetails();
      },
      error: (err) => {
        console.error(err);
        this.accountModalError = err.error || err.message || 'Failed to convert to Online Account.';
        this.isAccountSubmitting = false;
        this.cdr.markForCheck();
      }
    });
  }

  goBack(): void {
    this.location.back();
  }
}