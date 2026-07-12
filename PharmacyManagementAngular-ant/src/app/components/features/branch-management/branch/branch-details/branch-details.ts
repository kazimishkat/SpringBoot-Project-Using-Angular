import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { BranchRequest, BranchResponse, BranchType } from '../../../../../models/branch.model';
import { BranchService } from '../../../../../services/branch.service';


@Component({
  selector: 'app-branch-details',
  imports: [CommonModule, FormsModule],
  templateUrl: './branch-details.html',
  styleUrl: './branch-details.css'
})
export class BranchDetails implements OnInit {

  @ViewChild('branchForm') branchForm!: NgForm;

  // =====================================================
  // COMBINED DASHBOARD RENDERING STATES
  // =====================================================
  /** Holds the primary master menu array listed inside index sections */
  branchList: BranchResponse[] = [];
  branchTypes = Object.values(BranchType);
  
  branchId!: number;
  branchDetails: BranchResponse | null = null;
  errorMessage = '';
  submitted = false;

  /** Controls layout status between flat sheet visualization vs editable forms */
  isEditMode = false;

  /** Mutation mapping object structure targets updating scripts */
  editData!: BranchRequest;

  constructor(
    private branchService: BranchService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    // 1. Initialise full catalog dashboard grid arrays
    this.loadAllBranchesCatalog();

    // 2. Continuous tracking stream for checking parameters shifts inside URLs
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.branchId = Number(idParam);
        this.fetchGranularDetailsPanel(this.branchId);
      }
    });
  }

  // =====================================================
  // MASTER CATALOG RECOVERY SEQUENCES
  // =====================================================
  loadAllBranchesCatalog(): void {
    this.branchService.getAllBranches().subscribe({
      next: (data) => {
        this.branchList = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Catalog processing transaction crashed', err)
    });
  }

  // =====================================================
  // SINGLE PROFILE DETAILS EXTRACTION
  // =====================================================
  fetchGranularDetailsPanel(id: number): void {
    this.errorMessage = '';
    this.isEditMode = false; // Collapse form nodes if user switches rows mid-operation
    
    this.branchService.getBranchById(id).subscribe({
      next: (data) => {
        this.branchDetails = data;
        this.branchId = id;
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Failed to capture isolated targets structural context', err)
    });
  }

  // =====================================================
  // TOGGLE & INLINE INJECTION HANDLERS
  // =====================================================
  enableEditMode(): void {
    if (!this.branchDetails) return;

    this.isEditMode = true;
    this.submitted = false;

    // Maps recorded details onto modifiable data models safely
    this.editData = {
      id: this.branchDetails.id,
      branchCode: this.branchDetails.branchCode,
      name: this.branchDetails.name,
      branchType: this.branchDetails.branchType,
      address: this.branchDetails.address ? { ...this.branchDetails.address } : { addressLine1: '',
      addressLine2: '', city: '', state: '', postalCode: '', country: '' },
      phone: this.branchDetails.phone,
      email: this.branchDetails.email,
      licenseNumber: this.branchDetails.licenseNumber,
      managerName: this.branchDetails.managerName,
      isActive: this.branchDetails.isActive
    };
    
    this.cdr.markForCheck();
  }

  cancelEdit(): void {
    this.isEditMode = false;
    this.submitted = false;
    this.errorMessage = '';

    if (this.branchForm) {
      this.branchForm.resetForm();
    }
    this.cdr.markForCheck();
  }

  // =====================================================
  // SAVE/PATCH TRANSACTION LOGICS
  // =====================================================
  updateBranch(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.branchForm.invalid) {
      return;
    }

    this.branchService
      .updateBranch(this.branchId, this.editData)
      .subscribe({
        next: (res) => {
          alert('Branch Profile Configuration Updated Successfully');
          this.isEditMode = false;
          this.loadAllBranchesCatalog(); // Hot refresh master listings arrays
          this.fetchGranularDetailsPanel(this.branchId); // Reload updated display data
        },
        error: (err) => this.interceptError('Update operations rejected by endpoint verification', err)
      });
  }

  // =====================================================
  // DESTRUCTION DISPOSAL ROUTINES
  // =====================================================
  deleteBranch(idToDrop: number): void {
    if (confirm('Are you absolutely sure you want to permanently delete this branch configuration?')) {
      this.branchService.deleteBranch(idToDrop).subscribe({
        next: () => {
          alert('Branch Record Completely Purged');
          this.loadAllBranchesCatalog(); // Sync local catalog arrays immediately
          
          // Clear active detail panels if focused index was destroyed
          if (this.branchId === idToDrop) {
            this.branchDetails = null;
            this.isEditMode = false;
            this.router.navigate(['/branches/details']); // Flush route states safely
          }
          this.cdr.markForCheck();
        },
        error: (err) => this.interceptError('Purge requests halted', err)
      });
    }
  }

  // =====================================================
  // GLOBAL TRANSLATOR EXCEPTION WRAPPERS
  // =====================================================
  private interceptError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}