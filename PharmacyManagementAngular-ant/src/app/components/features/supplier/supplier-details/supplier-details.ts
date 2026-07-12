import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SupplierService } from '../../../../services/supplier.service';
import { SupplierResponse, SupplierRequest } from '../../../../models/supplier.model';

@Component({
  selector: 'app-supplier-details',
  imports: [CommonModule, FormsModule],
  templateUrl: './supplier-details.html',
  styleUrl: './supplier-details.css'
})
export class SupplierDetails implements OnInit {

  @ViewChild('supplierForm') supplierForm!: NgForm;

  // ==========================================
  // LIST & TARGET RENDERING STATES
  // ==========================================
  /** Holds the complete list of all suppliers loaded into memory */
  suppliersList: SupplierResponse[] = [];
  
  supplierId!: number;
  supplierDetails: SupplierResponse | null = null;
  errorMessage = '';
  submitted = false;

  /** Controls whether the active view details card is editable or read-only */
  isEditMode = false;

  /** Form binding object utilized during payload mutations */
  editData!: SupplierRequest;

  constructor(
    private supplierService: SupplierService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    // 1. First fetch all available suppliers directory indexes
    this.loadAllSuppliersRegistry();

    // 2. Capture and process primary parameters from URL routing map if present
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.supplierId = Number(idParam);
        this.fetchTargetProfileDetails(this.supplierId);
      }
    });
  }

  // =====================================================
  // FETCH LIST DATA
  // =====================================================
  /**
   * Load full corporate suppliers list indices from centralized service tables.
   */
  loadAllSuppliersRegistry(): void {
    this.supplierService.getAllSuppliers().subscribe({
      next: (data) => {
        this.suppliersList = data || [];
        this.cdr.markForCheck(); // Request rendering view push updates
      },
      error: (err) => this.handleError('Failed to pull suppliers directory log', err)
    });
  }

  // =====================================================
  // FETCH SPECIFIC TARGET DETAILS
  // =====================================================
  /**
   * Pull granular data values for a specific target supplier.
   */
  fetchTargetProfileDetails(id: number): void {
    this.errorMessage = '';
    this.isEditMode = false; // Reset modifier toggles on new item selection shifts
    
    this.supplierService.getSupplierById(id).subscribe({
      next: (data) => {
        this.supplierDetails = data;
        this.supplierId = id;
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('System could not load metrics for selected vendor', err)
    });
  }

  // =====================================================
  // TOGGLE & POPULATE INPUT FORMS
  // =====================================================
  /**
   * Switches component visual state matrices into modifiable form parameters.
   */
  enableEditMode(): void {
    if (!this.supplierDetails) return;

    this.isEditMode = true;
    this.submitted = false;
    
    // Map current active details into modifiable request structure securely
    this.editData = {
      id: this.supplierDetails.id,
      supplierCode: this.supplierDetails.supplierCode,
      name: this.supplierDetails.name,
      contactPerson: this.supplierDetails.contactPerson,
      phone: this.supplierDetails.phone,
      email: this.supplierDetails.email,
      address: this.supplierDetails.address ? { ...this.supplierDetails.address } : { addressLine1: '',addressLine2:'', city: '', state: '', postalCode: '', country: '' },
      tradeLicenseNo: this.supplierDetails.tradeLicenseNo,
      taxId: this.supplierDetails.taxId,
      isActive: this.supplierDetails.isActive
    };
    
    this.cdr.markForCheck();
  }

  /**
   * Dismisses current updates and restores the base read-only templates.
   */
  cancelEdit(): void {
    this.isEditMode = false;
    this.submitted = false;
    this.errorMessage = '';
    
    if (this.supplierForm) {
      this.supplierForm.resetForm();
    }
    this.cdr.markForCheck();
  }

  // =====================================================
  // SAVE/UPDATE TRANSACTION
  // =====================================================
  /**
   * Validate fields and submit modified JSON structures to backend endpoints.
   */
  updateSupplier(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.supplierForm.invalid) {
      return;
    }

    this.supplierService
      .updateSupplier(this.supplierId, this.editData)
      .subscribe({
        next: (res) => {
          alert('Supplier Configuration Patched Successfully');
          this.isEditMode = false;
          this.loadAllSuppliersRegistry(); // Refresh master list index records
          this.fetchTargetProfileDetails(this.supplierId); // Reload focused details panel data
        },
        error: (err) => this.handleError('Patch action aborted by server exception', err)
      });
  }

  // =====================================================
  // TERMINATE TRANSACTION
  // =====================================================
  /**
   * Drops targeted vendor entries completely from active registry databases.
   */
  deleteSupplier(idToDelete: number): void {
    if (confirm('Are you absolutely sure you want to delete this supplier corporate registration?')) {
      this.supplierService.deleteSupplier(idToDelete).subscribe({
        next: () => {
          alert('Supplier Corporate Profile Purged Successfully');
          this.loadAllSuppliersRegistry(); // Clean list tracking array
          
          // Clear targeted display contexts if the currently focused row was dropped
          if (this.supplierId === idToDelete) {
            this.supplierDetails = null;
            this.isEditMode = false;
            this.router.navigate(['/suppliers/details']); // Clean parameterized location route state safely
          }
          this.cdr.markForCheck();
        },
        error: (err) => this.handleError('Purge operations halted', err)
      });
    }
  }

  // =====================================================
  // SHARED EXCEPTION INTERCEPTOR
  // =====================================================
  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}