import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BranchService } from '../../../../../services/branch.service';
import { BranchRequest, BranchType } from '../../../../../models/branch.model';


@Component({
  selector: 'app-add-branch',
  imports: [FormsModule, CommonModule],
  templateUrl: './add-branch.html',
  styleUrl: './add-branch.css',
})
export class AddBranch implements OnInit {

  @ViewChild('branchForm') branchForm!: NgForm;

  // =========================
  // ENUM DROPDOWN DATA
  // =========================
  branchTypes = Object.values(BranchType);

  // =========================
  // FORM & SUBMISSION STATE
  // =========================
  branch: BranchRequest = this.initForm();
  submitted = false;
  errorMessage = '';

  constructor(
    private branchService: BranchService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void { }

  // Initialize/Reset object model fields structure
  initForm(): BranchRequest {
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
  // SAVE OPERATION
  // =====================================================
  /**
   * Validate and post complete branch configuration properties.
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
          this.errorMessage = `Creation failed: ${err.error || err.message || 'Server Error'}`;
          this.cdr.markForCheck();
        }
      });
  }

  // =====================================================
  // FORM RESET ROUTINE
  // =====================================================
  /**
   * Clears transaction entity definitions and purges Angular form state flags.
   */
  resetForm(): void {
    this.branch = this.initForm();
    this.submitted = false;
    this.errorMessage = '';

    if (this.branchForm) {
      this.branchForm.resetForm();
    }
    this.cdr.markForCheck();
  }
}