import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MedicineBatchRequest } from '../../../../../models/medicine-batch.model';
import { MedicineBatchService } from '../../../../../services/medicine-batch.service';
import { MedicineService } from '../../../../../services/medicine.service';
import { SupplierService } from '../../../../../services/supplier.service';


@Component({
  selector: 'app-medicine-batch-add',
  imports: [FormsModule, CommonModule],
  templateUrl: './medicine-batch-add.html',
  styleUrl: './medicine-batch-add.css',
})
export class MedicineBatchAdd implements OnInit {

  @ViewChild('batchForm') batchForm!: NgForm;

  // =====================================================
  // DROPDOWN DATA DEPENDENCY ARRAYS
  // =====================================================
  medicines: any[] = []; // Populates the target Medicine dropdown configuration
  suppliers: any[] = []; // Populates the target Supplier dropdown configuration

  // =====================================================
  // FORM MUTATION & VALIDATION STATE
  // =====================================================
  batch: MedicineBatchRequest = this.initFormStructure();
  submitted = false;
  errorMessage = '';

  constructor(
    private batchService: MedicineBatchService,
    private medicineService: MedicineService,
    private supplierService: SupplierService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadDropdownDependencies();
  }

  /** Fetch available master dependencies to initialize select component menus */
  loadDropdownDependencies(): void {
    this.medicineService.getAllMedicines().subscribe({
      next: (data) => {
        this.medicines = data || [];
        this.cdr.markForCheck();
      }
    });

    this.supplierService.getAllSuppliers().subscribe({
      next: (data) => {
        this.suppliers = data || [];
        this.cdr.markForCheck();
      }
    });
  }

  // Helper method to setup pristine model fields structural schema
  private initFormStructure(): MedicineBatchRequest {
    return {
      medicineId: undefined as any,
      batchNumber: '',
      supplierId: undefined,
      manufactureDate: '',
      expiryDate: '',
      purchasePrice: undefined as any,
      sellingPrice: undefined as any,
      isActive: true
    };
  }

  // =====================================================
  // TRANSACTIONS SAVE ROUTINES
  // =====================================================
  /**
   * Validate bindings and dispatch new JSON payload to central batch service streams.
   */
  saveBatch(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.batchForm.invalid) {
      return;
    }

    this.batchService
      .createBatch(this.batch)
      .subscribe({
        next: (res) => {
          alert('Medicine Batch Saved Successfully');
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
   * Wipe tracking variables back to base blueprints and clear template validation flags.
   */
  resetForm(): void {
    this.batch = this.initFormStructure();
    this.submitted = false;
    this.errorMessage = '';

    if (this.batchForm) {
      this.batchForm.resetForm();
    }
    this.cdr.markForCheck();
  }
}