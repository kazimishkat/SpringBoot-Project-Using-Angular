import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MedicineBatchResponse } from '../../../../../models/medicine-batch.model';
import { MedicineBatchService } from '../../../../../services/medicine-batch.service';


@Component({
  selector: 'app-medicine-batch-list',
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './medicine-batch-list.html',
  styleUrl: './medicine-batch-list.css'
})
export class MedicineBatchList implements OnInit {

  // =====================================================
  // VIEW GRID & FILTER CONTROL STATES
  // =====================================================
  batches: MedicineBatchResponse[] = [];
  searchBatchNumber: string = '';
  expiryFilterDate: string = '';
  errorMessage: string = '';

  constructor(
    private batchService: MedicineBatchService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadAllBatches();
  }

  // =====================================================
  // LOAD SYSTEM BATCH INVENTORY
  // =====================================================
  /**
   * Retrieve all recorded medicine batches from backend registry logs.
   */
  loadAllBatches(): void {
    this.errorMessage = '';
    this.batchService.getAllBatches().subscribe({
      next: (data) => {
        this.batches = data || [];
        this.cdr.markForCheck(); // Explicit grid layout state recalculation push
      },
      error: (err) => this.handleError('Failed to load medicine batches directory', err)
    });
  }

  // =====================================================
  // FILTER & SEARCH STRATEGIES
  // =====================================================
  /**
   * Trigger filtering routines using unique batch number identifiers.
   */
  onSearchByNumber(): void {
    if (!this.searchBatchNumber.trim()) {
      this.loadAllBatches();
      return;
    }

    this.errorMessage = '';
    this.batchService.getBatchesByNumber(this.searchBatchNumber.trim()).subscribe({
      next: (data) => {
        this.batches = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Search routine by batch number failed', err)
    });
  }

  /**
   * Filter out batch configurations facing early expiration metrics before specified threshold date.
   */
  onFilterExpiration(): void {
    if (!this.expiryFilterDate) {
      this.loadAllBatches();
      return;
    }

    this.errorMessage = '';
    this.batchService.getExpiringBatches(this.expiryFilterDate).subscribe({
      next: (data) => {
        this.batches = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Expiration threshold filtering aborted', err)
    });
  }

  // =====================================================
  // WIPE RECORD TRANSACTIONS
  // =====================================================
  /**
   * Drops specific batch files permanently from active records using primary IDs.
   */
  deleteBatch(id: number): void {
    if (confirm('Are you sure you want to completely drop this medicine batch from system records?')) {
      this.batchService.deleteBatch(id).subscribe({
        next: () => {
          this.loadAllBatches(); // Sync local catalog arrays seamlessly
        },
        error: (err) => this.handleError('Batch disposal instruction rejected by database rules', err)
      });
    }
  }

  // =====================================================
  // SHARED EXCEPTION INTERCEPTOR LOGGER
  // =====================================================
  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}
