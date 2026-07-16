import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MedicineBatchService } from '../../../../../services/medicine-batch.service';
import { MedicineService } from '../../../../../services/medicine.service';
import { SupplierService } from '../../../../../services/supplier.service';
import { MedicineBatchResponse, MedicineBatchRequest } from '../../../../../models/medicine-batch.model';

@Component({
  selector: 'app-medicine-batch-details',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './medicine-batch-details.html',
  styleUrl: './medicine-batch-details.css'
})
export class MedicineBatchDetailsComponent implements OnInit {

  @ViewChild('batchForm') batchForm!: NgForm;

  // =====================================================
  // MASTER-DETAIL SIDE PANEL DIRECTORIES CONTAINER STATES
  // =====================================================
  masterDirectoryList: MedicineBatchResponse[] = [];
  medicinesLookup: any[] = [];
  suppliersLookup: any[] = [];

  batchId!: number;
  batchDetails: MedicineBatchResponse | null = null;
  errorMessage = '';
  submitted = false;

  /** Controls DOM rendering structure between flat panels vs active editors forms */
  isEditMode = false;

  /** Mutation mapping schema for parameters save routines execution */
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
    // 1. Recover full master directory collections
    this.loadMasterCatalogIndexes();
    this.loadDropdownDataLookups();

    // 2. Param tracking loops intercepting paths parameters modifications inside URLs
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.batchId = Number(idParam);
        this.fetchTargetProfileDetails(this.batchId);
      }
    });
  }

  // =====================================================
  // CORE DIRECTORY METRICS RECOVERY PIPELINES
  // =====================================================
  loadMasterCatalogIndexes(): void {
    this.batchService.getAllBatches().subscribe({
      next: (data) => {
        this.masterDirectoryList = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Batch directory array loading collapsed', err)
    });
  }

  loadDropdownDataLookups(): void {
    this.medicineService.getAllMedicines().subscribe(data => { this.medicinesLookup = data || []; this.cdr.markForCheck(); });
    this.supplierService.getAllSuppliers().subscribe(data => { this.suppliersLookup = data || []; this.cdr.markForCheck(); });
  }

  // =====================================================
  // GRANULAR PROFILE FIELDS DETAILED EXTRACTOR
  // =====================================================
  fetchTargetProfileDetails(id: number): void {
    this.errorMessage = '';
    this.isEditMode = false; // Gracefully collapse editor grids if context switches
    
    this.batchService.getBatchById(id).subscribe({
      next: (data) => {
        this.batchDetails = data;
        this.batchId = id;
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Failed to resolve target batch structural metrics logs', err)
    });
  }

  // =====================================================
  // FORMS INTERACTION LAYOUT SWITCH CONTROLLERS
  // =====================================================
  enableEditMode(): void {
    if (!this.batchDetails) return;

    this.isEditMode = true;
    this.submitted = false;

    // Direct mapping implementation properties safely down onto request configurations models
    this.editData = {
      id: this.batchDetails.id,
      medicineId: this.batchDetails.medicineId,
      batchNumber: this.batchDetails.batchNumber,
      supplierId: this.batchDetails.supplierId || undefined,
      manufactureDate: this.batchDetails.manufactureDate ? this.batchDetails.manufactureDate.toString().split('T')[0] : '',
      expiryDate: this.batchDetails.expiryDate ? this.batchDetails.expiryDate.toString().split('T')[0] : '',
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
  // SAVE / PUT TRANS-COMMIT PROCESSING LOGICS
  // =====================================================
  /** Commits the modified parameters using the specific backend PUT signature */
  updateBatchConfigurations(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.batchForm.invalid) {
      return;
    }

    this.batchService
      .updateBatch(this.batchId, this.editData)
      .subscribe({
        next: (res) => {
          alert('Batch Configurations Metrics Committed Successfully via PUT Service Rules');
          this.isEditMode = false;
          this.loadMasterCatalogIndexes(); // Hot refresh master listings data matrix
          this.fetchTargetProfileDetails(this.batchId); // Reload focused visual layout fields parameters card
        },
        error: (err) => this.interceptError('Batch validation rules matching rejected by server rules', err)
      });
  }

  // =====================================================
  // RUNTIME TRANSLATOR EXCEPTION WRAPPERS
  // =====================================================
  private interceptError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}