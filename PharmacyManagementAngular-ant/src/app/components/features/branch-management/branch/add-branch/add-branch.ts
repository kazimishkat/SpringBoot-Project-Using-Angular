import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BranchRequest, BranchType } from '../../../../../models/branch.model';
import { BranchService } from '../../../../../services/branch.service';


@Component({
  selector: 'app-add-branch',
  imports: [FormsModule, CommonModule],
  templateUrl: './add-branch.html',
  styleUrl: './add-branch.css',
})
export class AddBranch implements OnInit {

  @ViewChild('branchForm') branchForm!: NgForm;

  // =====================================================
  // ENUM DROPDOWNS & CONFIGURATION STATES
  // =====================================================
  branchTypes = Object.values(BranchType);

  // =====================================================
  // FORM MUTATION & VALIDATION STATE
  // =====================================================
  branch: BranchRequest = this.initFormStructure();
  submitted = false;
  errorMessage = '';

  constructor(
    private branchService: BranchService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void { }

  // Helper method to setup pristine model fields structural schema
  private initFormStructure(): BranchRequest {
    return {
      branchCode: '',
      name: '',
      branchType: undefined as any,
      address: {
        addressLine1: '',
      addressLine2: '',
        city: '',
        state: '',
        postalCode: '',
        country: ''
      },
      phone: '',
      email: '',
      licenseNumber: '',
      managerName: '',
      isActive: true
    };
  }

  // =====================================================
  // TRANSACTIONS SAVE ROUTINES
  // =====================================================
  /**
   * Validate bindings and dispatch new JSON payload to central service streams.
   */
  saveBranch(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.branchForm.invalid) {
      return;
    }

    this.branchService
      .createBranch(this.branch)
      .subscribe({
        next: (res) => {
          alert('Branch Saved Successfully');
          this.resetForm();
        },
        error: (err) => {
          console.error(err);
          this.errorMessage = `Creation transaction aborted: ${err.error || err.message || 'Server Exception'}`;
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
    this.branch = this.initFormStructure();
    this.submitted = false;
    this.errorMessage = '';

    if (this.branchForm) {
      this.branchForm.resetForm();
    }
    this.cdr.markForCheck();
  }
}