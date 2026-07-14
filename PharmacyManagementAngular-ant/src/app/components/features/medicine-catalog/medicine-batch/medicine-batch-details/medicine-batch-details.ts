import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MedicineBatchRequest, MedicineBatchResponse } from '../../../../../models/medicine-batch.model';
import { MedicineBatchService } from '../../../../../services/medicine-batch.service';
import { MedicineService } from '../../../../../services/medicine.service';
import { SupplierService } from '../../../../../services/supplier.service';


@Component({
  selector: 'app-medicine-batch-details',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './medicine-batch-details.html',
  styleUrl: './medicine-batch-details.css'
})
export class MedicineBatchDetails implements OnInit {

  @ViewChild('batchForm') batchForm!: NgForm;

  // =====================================================
  // DASHBOARD LAYOUT & MUTATION STATE CONFIGURATIONS
  // =====================================================
  /** Master directory container listing array */
  masterBatchList: MedicineBatchResponse[] = [];
  
  medicines: any[] = []; // Used to populate the Medicine select element
  suppliers: any[] = []; // Used to populate the Supplier select element

  batchId!: number;
  batchDetails: MedicineBatchResponse | null = null;
  errorMessage = '';
  submitted = false;

  /** Controls layout template rendering state matrices (View logs vs Input fields form) */
  isEditMode = false;

  /** Form binding model instance utilized during payload patching */
  editData!: MedicineBatchRequest;

  constructor(
    private batchService: MedicineBatchService,
    private medicineService: MedicineService,
    private supplierService: SupplierService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    // 1. Core catalog data streams initializations
    this.loadMasterCatalogIndexes();
    this.loadDropdownDataDependencies();

    // 2. Intercept and capture parameterized shifts inside route URLs continuously
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.batchId = Number(idParam);
        this.fetchTargetGranularDetails(this.batchId);
      }
    });
  }

  // =====================================================
  // CATALOG DEPENDENCIES EXTRACTIONS
  // =====================================================
  loadMasterCatalogIndexes(): void {
    this.batchService.getAllBatches().subscribe({
      next: (data) => {
        this.masterBatchList = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Catalog transaction loading stream crashed', err)
    });
  }

  loadDropdownDataDependencies(): void {
    this.medicineService.getAllMedicines().subscribe(data => {
      this.medicines = data || [];
      this.cdr.markForCheck();
    });

    this.supplierService.getAllSuppliers().subscribe(data => {
      this.suppliers = data || [];
      this.cdr.markForCheck();
    });
  }

  // =====================================================
  // ISOLATED TARGET LINE RESOLUTION
  // =====================================================
  fetchTargetGranularDetails(id: number): void {
    this.errorMessage = '';
    this.isEditMode = false; // Collapse input form layouts automatically if context selection rows shift
    
    this.batchService.getBatchById(id).subscribe({
      next: (data) => {
        this.batchDetails = data;
        this.batchId = id;
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Failed to capture targeted configuration layouts metrics', err)
    });
  }

  // =====================================================
  // FORMS INTERACTION SWITCH CONTROLLERS
  // =====================================================
  enableEditMode(): void {
    if (!this.batchDetails) return;

    this.isEditMode = true;
    this.submitted = false;

    // Maps loaded response entities securely onto modifiable request shape targets
    this.editData = {
      id: this.batchDetails.id,
      medicineId: this.batchDetails.medicineId,
      batchNumber: this.batchDetails.batchNumber,
      supplierId: this.batchDetails.supplierId || undefined,
      manufactureDate: this.batchDetails.manufactureDate,
      expiryDate: this.batchDetails.expiryDate,
      purchasePrice: this.batchDetails.purchasePrice,
      sellingPrice: this.batchDetails.sellingPrice,
      isActive: this.batchDetails.isActive
    };
    
    this.cdr.markForCheck();
  }

  cancelEdit(): void {
    this.isEditMode = false;
    this.submitted = false;
    this.errorMessage = '';

    if (this.batchForm) {
      this.batchForm.resetForm();
    }
    this.cdr.markForCheck();
  }

  // =====================================================
  // SAVE / PATCH TRANSACTION ROUTINES
  // =====================================================
  updateBatch(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.batchForm.invalid) {
      return;
    }

    this.batchService
      .updateBatch(this.batchId, this.editData)
      .subscribe({
        next: (res) => {
          alert('Batch Configurations Metrics Patched Successfully');
          this.isEditMode = false;
          this.loadMasterCatalogIndexes(); // Refresh master directory indexes catalog
          this.fetchTargetGranularDetails(this.batchId); // Reload fresh structural view template values
        },
        error: (err) => this.interceptError('Batch mutation sequence aborted by database constraints', err)
      });
  }

  // =====================================================
  // DISPOSAL DISMISS OPERATIONS
  // =====================================================
  deleteBatch(idToDrop: number): void {
    if (confirm('Are you completely sure you want to drop this batch tracking record permanently?')) {
      this.batchService.deleteBatch(idToDrop).subscribe({
        next: () => {
          alert('Medicine Batch Matrix Log Purged Successfully');
          this.loadMasterCatalogIndexes(); // Sync local catalog memory mappings instantly
          
          // Clear operational displays panels context safely if active focused row was drop targeted
          if (this.batchId === idToDrop) {
            this.batchDetails = null;
            this.isEditMode = false;
            this.router.navigate(['/medicine-batches/details']); // Flush route parameterized keys references
          }
          this.cdr.markForCheck();
        },
        error: (err) => this.interceptError('Purge pipeline processing interrupted', err)
      });
    }
  }

  // =====================================================
  // CORE EXCEPTIONS TRANSLATOR WRAPPER LOGGER
  // =====================================================
  private interceptError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}
