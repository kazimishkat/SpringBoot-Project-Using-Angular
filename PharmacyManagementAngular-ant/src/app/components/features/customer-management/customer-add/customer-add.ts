import { ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CustomerService } from '../../../../services/customer.service';
import { CustomerRequest, CustomerResponse, Gender } from '../../../../models/customer.model';

@Component({
  selector: 'app-customer-add',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './customer-add.html',
  styleUrl: './customer-add.css'
})
export class CustomerAdd implements OnInit {

  @ViewChild('customerForm') customerForm!: NgForm;

  // 🌟 CreateInvoice বা অন্য মোডাল কম্পোনেন্ট থেকে ডেটা পাওয়ার জন্য Inputs
  @Input() isDialogMode: boolean = false;
  @Input() initialPhone: string = '';

  // 🌟 নতুন কাস্টমার সেভ হলে বা মোডাল বন্ধ করলে Event Emit করার জন্য Outputs
  @Output() customerSaved = new EventEmitter<CustomerResponse>();
  @Output() dialogClosed = new EventEmitter<void>();

  genders = Object.values(Gender);
  customerRequest!: CustomerRequest;
  confirmPassword = '';
  
  selectedFile?: File;
  imagePreview: string | null = null;

  submitted = false;
  errorMessage = '';

  constructor(
    private customerService: CustomerService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.resetForm();
    
    // যদি ইনভয়েস সার্চ থেকে মোবাইল নম্বর পাঠানো হয়ে থাকে
    if (this.initialPhone) {
      this.customerRequest.phone = this.initialPhone;
    }
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

  onCreateAccountToggle(): void {
    if (!this.customerRequest.createAccount) {
      this.customerRequest.username = '';
      this.customerRequest.password = '';
      this.confirmPassword = '';
    }
    this.cdr.markForCheck();
  }

  saveCustomer(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.customerForm.invalid) {
      return;
    }

    // 🌟 [FIX]: ইমেইল ফাঁকা বা শুধু স্পেস থাকলে undefined বানিয়ে দেওয়া (ডাটাবেজে 500 duplicate entry '' আটকানোর জন্য)
    if (this.customerRequest.email && !this.customerRequest.email.trim()) {
      this.customerRequest.email = undefined;
    }

    if (this.customerRequest.createAccount) {
      if (!this.customerRequest.email) {
        this.errorMessage = 'Email is required when creating an online login account.';
        return;
      }
      if (this.customerRequest.password !== this.confirmPassword) {
        this.errorMessage = 'Passwords do not match.';
        return;
      }
    }

    this.customerService.createCustomer(this.customerRequest, this.selectedFile).subscribe({
      next: (savedCustomer: CustomerResponse) => {
        if (this.isDialogMode) {
          // 🌟 মোডাল মোডে কাস্টমার অবজেক্টটি Event Emit করা হচ্ছে
          this.customerSaved.emit(savedCustomer);
        } else {
          alert(this.customerRequest.createAccount 
            ? 'Online Customer account created! Verification email dispatched.' 
            : 'Walk-in Customer registered successfully.');
          this.router.navigate(['/dashboard/customers']);
        }
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Creation failed: ${err.error || err.message || 'Validation Failure'}`;
        this.cdr.markForCheck();
      }
    });
  }

  resetForm(): void {
    this.customerRequest = {
      name: '',
      phone: this.initialPhone || '',
      email: '',
      gender: undefined,
      age: undefined,
      address: { 
        addressLine1: '',
        addressLine2: '',
        city: '',
        state: '',
        postalCode: '',
        country: '' 
      },
      createAccount: false,
      username: '',
      password: '',
      isActive: true
    };
    this.confirmPassword = '';
    this.selectedFile = undefined;
    this.imagePreview = null;
    this.submitted = false;
    this.errorMessage = '';

    if (this.customerForm) {
      this.customerForm.resetForm(this.customerRequest);
    }
    this.cdr.markForCheck();
  }

  cancel(): void {
    if (this.isDialogMode) {
      // 🌟 মোডাল মোডে ক্লোজ ইভেন্ট ট্রিগার করা হচ্ছে
      this.dialogClosed.emit();
    } else {
      this.router.navigate(['/dashboard/customers']);
    }
  }
}