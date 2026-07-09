import { ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SupplierService } from '../../../../services/supplier.service';
import { SupplierRequest } from '../../../../models/supplier.model';

@Component({
  selector: 'app-add-supplier',
  imports: [FormsModule, CommonModule],
  templateUrl: './add-supplier.html',
  styleUrl: './add-supplier.css',
})
export class AddSupplier implements OnInit {

  @ViewChild('supplierForm') supplierForm!: NgForm;

  // =========================
  // FORM & SUBMISSION STATE
  // =========================
  supplier: SupplierRequest = {
    supplierCode: '',
    name: '',
    contactPerson: '',
    phone: '',
    email: '',
    address: {
      addressLine1: '',
      addressLine2: '',
      city: '',
      state: '',
      postalCode: '',
      country: ''
    },
    tradeLicenseNo: '',
    taxId: '',
    isActive: true
  };

  submitted = false;
  errorMessage = '';

  constructor(
    private supplierService: SupplierService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    // Initial initialization hooks if required
  }

  // =====================================================
  // SAVE OPERATION
  // =====================================================
  /**
   * Validate and submit supplier JSON profile to the backend API.
   */
  saveSupplier(): void {
    this.submitted = true;
    this.errorMessage = '';

    // Direct structural form validation check before processing request
    if (this.supplierForm.invalid) {
      return;
    }

    this.supplierService
      .createSupplier(this.supplier)
      .subscribe({
        next: (res) => {
          alert('Supplier Saved Successfully');
          this.resetForm();
        },
        error: (err) => {
          console.error(err);
          this.errorMessage = `Creation failed: ${err.error || err.message || 'Server Error'}`;
          this.cdr.markForCheck();
        }
      });
  }

  // =====================================================
  // FORM RESET ROUTINE
  // =====================================================
  /**
   * Resets the supplier request object properties and cleans Angular template validation states.
   */
  resetForm(): void {
    this.supplier = {
      supplierCode: '',
      name: '',
      contactPerson: '',
      phone: '',
      email: '',
      address: {
        addressLine1: '',
      addressLine2: '',
        city: '',
        state: '',
        postalCode: '',
        country: ''
      },
      tradeLicenseNo: '',
      taxId: '',
      isActive: true
    };

    this.submitted = false;
    this.errorMessage = '';

    // Completely clear Angular form state hooks and template boundary classes
    if (this.supplierForm) {
      this.supplierForm.resetForm();
    }

    this.cdr.markForCheck();
  }
}