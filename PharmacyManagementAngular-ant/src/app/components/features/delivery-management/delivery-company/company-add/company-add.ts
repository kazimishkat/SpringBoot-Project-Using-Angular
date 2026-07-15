import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { DeliveryCompanyRequest } from '../../../../../models/delivery-company.model';
import { DeliveryCompanyService } from '../../../../../services/delivery-company.service';


@Component({
  selector: 'app-company-add',
  imports: [FormsModule, CommonModule],
  templateUrl: './company-add.html',
  styleUrl: './company-add.css',
})
export class DeliveryCompanyAdd implements OnInit {

  @ViewChild('companyForm') companyForm!: NgForm;

  // =====================================================
  // FORM MUTATION & VALIDATION STATE
  // =====================================================
  company: DeliveryCompanyRequest = this.initFormStructure();
  submitted = false;
  errorMessage = '';

  constructor(
    private companyService: DeliveryCompanyService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void { }

  // Helper method to setup pristine model fields structural schema
  private initFormStructure(): DeliveryCompanyRequest {
    return {
      companyName: '',
      contactPerson: '',
      phoneNumber: '',
      apiKey: '',
      isActive: true
    };
  }

  // =====================================================
  // TRANSACTIONS SAVE ROUTINES
  // =====================================================
  /**
   * Validate bindings and dispatch new JSON payload to central delivery service streams.
   */
  saveCompany(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.companyForm.invalid) {
      return;
    }

    this.companyService
      .registerCompany(this.company)
      .subscribe({
        next: (res) => {
          alert('Delivery Company Registered Successfully');
          this.resetForm();
        },
        error: (err) => {
          console.error(err);
          this.errorMessage = `Registration transaction aborted: ${err.error || err.message || 'Server Exception'}`;
          this.cdr.markForCheck();
        }
      });
  }

  // =====================================================
  // FORM LIFE CYCLE CLEANER
  // =====================================================
  /**
   * Wipe tracking variables back to base blueprints and clear structural DOM classes.
   */
  resetForm(): void {
    this.company = this.initFormStructure();
    this.submitted = false;
    this.errorMessage = '';

    if (this.companyForm) {
      this.companyForm.resetForm();
    }
    this.cdr.markForCheck();
  }
}
