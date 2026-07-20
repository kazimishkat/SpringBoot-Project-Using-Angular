import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../../../../services/customer.service';
import { CustomerRequest, Gender } from '../../../../models/customer.model';

@Component({
  selector: 'app-customer-edit',
  imports: [FormsModule, CommonModule],
  templateUrl: './customer-edit.html',
  styleUrl: './customer-edit.css'
})
export class CustomerEdit implements OnInit {

  @ViewChild('customerForm') customerForm!: NgForm;

  customerId!: number;
  genders = Object.values(Gender);
  
  customerRequest!: CustomerRequest;
  selectedFile?: File;
  imagePreview: string | null = null;
  existingImage: string | null = null;

  submitted = false;
  errorMessage = '';

  constructor(
    private customerService: CustomerService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.customerId = Number(id);
        this.loadCustomerRecord();
      }
    });
  }

  loadCustomerRecord(): void {
    this.customerService.getCustomerById(this.customerId).subscribe({
      next: (res) => {
        this.customerRequest = {
          name: res.name,
          phone: res.phone,
          email: res.email || '',
          gender: res.gender,
          age: res.age,
          address: res.address || { addressLine1: '',
      addressLine2: '',
      city: '',
      state: '',
      postalCode: '',
      country: '' },
          createAccount: false,
          isActive: res.isActive
        };
        this.existingImage = res.image || null;
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Failed to load customer profile', err)
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

  updateCustomer(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.customerForm.invalid) {
      return;
    }

    this.customerService.updateCustomer(this.customerId, this.customerRequest, this.selectedFile).subscribe({
      next: () => {
        alert('Customer details updated successfully.');
        this.router.navigate(['/dashboard/customers']);
      },
      error: (err) => this.handleError('Update failed', err)
    });
  }

  cancel(): void {
    this.router.navigate(['/dashboard/customers']);
  }

  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}