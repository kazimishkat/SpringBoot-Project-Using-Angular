import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { NgForm, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MedicineBatchRequest, MedicineBatchResponse } from '../../../../models/medicine-batch.model';
import { MedicineBatchService } from '../../../../services/medicine-batch.service';
import { MedicineService } from '../../../../services/medicine.service';
import { SupplierService } from '../../../../services/supplier.service';


@Component({
  selector: 'app-medicine-batch',
  imports: [FormsModule, CommonModule],
  templateUrl: './medicine-batch.html',
  styleUrl: './medicine-batch.css'
})
export class MedicineBatchComponent implements OnInit {

  @ViewChild('batchForm') batchForm!: NgForm;

  // State data list arrays
  batches: MedicineBatchResponse[] = [];
  medicines: any[] = [];   // Populates medicine picker dropdown
  suppliers: any[] = [];   // Populates supplier picker dropdown
  
  // Filtering and template configuration states
  searchBatchNumber: string = '';
  expiryFilterDate: string = '';
  errorMessage: string = '';
  isEditMode: boolean = false;
  submitted: boolean = false;

  // Request binding model instance
  batchData!: MedicineBatchRequest;

  constructor(
    private batchService: MedicineBatchService,
    private medicineService: MedicineService,
    private supplierService: SupplierService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.resetForm();
    this.loadAllBatches();
    this.loadDropdownData();
  }

  // Fetch full inventory context batches
  loadAllBatches(): void {
    this.batchService.getAllBatches().subscribe({
      next: (data) => {
        this.batches = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Failed to load medicine batches', err)
    });
  }

  // Load contextual target information dependencies
  loadDropdownData(): void {
    this.medicineService.getAllMedicines().subscribe(data => {
      this.medicines = data || [];
      this.cdr.markForCheck();
    });
    
    this.supplierService.getAllSuppliers().subscribe(data => {
      this.suppliers = data || [];
      this.cdr.markForCheck();
    });
  }

  // Search batches via batch number string values
  onSearchByNumber(): void {
    if (!this.searchBatchNumber.trim()) {
      this.loadAllBatches();
      return;
    }
    this.batchService.getBatchesByNumber(this.searchBatchNumber).subscribe({
      next: (data) => {
        this.batches = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Search routine failed', err)
    });
  }

  // Filter out batch entities facing early expiration thresholds
  onFilterExpiration(): void {
    if (!this.expiryFilterDate) {
      this.loadAllBatches();
      return;
    }
    this.batchService.getExpiringBatches(this.expiryFilterDate).subscribe({
      next: (data) => {
        this.batches = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Expiration filtering failed', err)
    });
  }

  // Handle standard transaction payload submission sequence
  onSubmit(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.batchForm.invalid) {
      return;
    }

    if (this.isEditMode && this.batchData.id) {
      // Direct updates implementation route
      this.batchService.updateBatch(this.batchData.id, this.batchData).subscribe({
        next: () => {
          this.loadAllBatches();
          this.resetForm();
        },
        error: (err) => this.handleError('Batch mutation operation rejected', err)
      });
    } else {
      // Direct placement implementation route
      this.batchService.createBatch(this.batchData).subscribe({
        next: () => {
          this.loadAllBatches();
          this.resetForm();
        },
        error: (err) => this.handleError('Batch validation registration rejected', err)
      });
    }
  }

  // Select particular rows structure data targets into the modifier scope
  editBatch(item: MedicineBatchResponse): void {
    this.isEditMode = true;
    this.batchData = { ...item };
    this.cdr.markForCheck();
  }

  // Process operational execution of delete commands
  deleteBatch(id: number): void {
    if (confirm('Are you sure you want to completely drop this batch from system memory?')) {
      this.batchService.deleteBatch(id).subscribe({
        next: () => this.loadAllBatches(),
        error: (err) => this.handleError('Batch dropped request aborted', err)
      });
    }
  }

  // Clear component transactional context profiles completely
  resetForm(): void {
    this.batchData = {
      medicineId: 0,
      batchNumber: '',
      supplierId: undefined,
      manufactureDate: '',
      expiryDate: '',
      purchasePrice: 0,
      sellingPrice: 0,
      isActive: true
    };
    
    this.submitted = false;
    this.isEditMode = false;
    this.errorMessage = '';

    if (this.batchForm) {
      this.batchForm.resetForm();
    }
    this.cdr.markForCheck();
  }

  // Exception logger routing helper
  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}