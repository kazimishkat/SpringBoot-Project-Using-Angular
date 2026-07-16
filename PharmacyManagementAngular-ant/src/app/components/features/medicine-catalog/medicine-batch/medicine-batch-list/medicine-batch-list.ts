import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterModule } from '@angular/router';
import { MedicineBatchService } from '../../../../../services/medicine-batch.service';
import { MedicineBatchResponse } from '../../../../../models/medicine-batch.model';

@Component({
  selector: 'app-medicine-batch-list',
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './medicine-batch-list.html',
  styleUrl: './medicine-batch-list.css'
})
export class MedicineBatchList implements OnInit {

  // =====================================================
  // FILTER BINDINGS & DATAGRID CONTAINER STATES
  // =====================================================
  batches: MedicineBatchResponse[] = [];
  
  // UI filter parameter strings requested
  searchMedicineQuery: string = '';
  searchSupplierQuery: string = '';
  expiryFilterDate: string = '';
  statusToggleFilter: string = ''; // Supports "ALL", "ACTIVE", "INACTIVE"
  searchBatchNum: string = '';     // Unique batch number fast search bypass

  errorMessage: string = '';

  constructor(
    private batchService: MedicineBatchService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadAllSystemBatches();
  }

  // =====================================================
  // CATALOG RETRIEVAL LOOPS
  // =====================================================
  /** Load master index matrix from centralized services backend */
  loadAllSystemBatches(): void {
    this.errorMessage = '';
    this.batchService.getAllBatches().subscribe({
      next: (data) => {
        this.batches = data || [];
        this.cdr.markForCheck(); // Push immediate grid rendering updates down the layout tree
      },
      error: (err) => this.handleError('Failed to parse central medicine batches catalogue', err)
    });
  }

  // =====================================================
  // ADAPTIVE FILTER SELECTION STRATEGIES
  // =====================================================
  /** Decodes filter parameters to call dedicated endpoints or process locally */
  onFilterPipelineExecute(): void {
    this.errorMessage = '';

    // 1. Fast unique endpoint execution route using direct batch text tokens lookup
    if (this.searchBatchNum.trim()) {
      this.batchService.getBatchesByNumber(this.searchBatchNum.trim()).subscribe({
        next: (data) => {
          this.batches = data || [];
          this.cdr.markForCheck();
        },
        error: (err) => this.handleError('Batch string locator processing aborted', err)
      });
      return;
    }

    // 2. Continuous time bounds checks utilizing date parameters map route
    if (this.expiryFilterDate) {
      this.batchService.getExpiringBatches(this.expiryFilterDate).subscribe({
        next: (data) => {
          this.batches = data || [];
          this.applyLocalFiltersFallback();
        },
        error: (err) => this.handleError('Expiration data validation streams broken', err)
      });
      return;
    }

    // Baseline catalog reload loop executing custom text block match algorithms
    this.batchService.getAllBatches().subscribe({
      next: (data) => {
        this.batches = data || [];
        this.applyLocalFiltersFallback();
      },
      error: (err) => this.handleError('Catalog trace load fault encountered', err)
    });
  }

  /** Resolves localized properties adjustments matching string criteria blocks */
  private applyLocalFiltersFallback(): void {
    if (this.searchMedicineQuery.trim()) {
      const med = this.searchMedicineQuery.trim().toLowerCase();
      this.batches = this.batches.filter(x => x.brandName?.toLowerCase().includes(med) || x.medicineId.toString() === med);
    }

    if (this.searchSupplierQuery.trim()) {
      const sup = this.searchSupplierQuery.trim().toLowerCase();
      this.batches = this.batches.filter(x => x.supplierName?.toLowerCase().includes(sup));
    }

    if (this.statusToggleFilter) {
      const targetFlag = this.statusToggleFilter === 'ACTIVE';
      this.batches = this.batches.filter(x => x.isActive === targetFlag);
    }

    this.cdr.markForCheck();
  }

  // =====================================================
  // LOGGERS FOR UNIVERSAL EXCEPTIONS
  // =====================================================
  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}