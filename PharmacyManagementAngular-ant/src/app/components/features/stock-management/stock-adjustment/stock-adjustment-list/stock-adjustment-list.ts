import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { StockAdjustmentService } from '../../../../../services/stock-adjustment.service';
import { StockAdjustmentResponse, AdjustmentReason, AdjustmentStatus } from '../../../../../models/stock-adjustment.model';

@Component({
  selector: 'app-stock-adjustment-list',
  imports: [FormsModule, CommonModule],
  templateUrl: './stock-adjustment-list.html',
  styleUrl: './stock-adjustment-list.css'
})
export class StockAdjustmentListComponent implements OnInit {

  adjustments: StockAdjustmentResponse[] = [];
  masterLogs: StockAdjustmentResponse[] = [];
  reasons = Object.values(AdjustmentReason);
  statuses = Object.values(AdjustmentStatus);

  searchQuery: string = '';
  selectedReason: AdjustmentReason | '' = '';
  selectedStatus: AdjustmentStatus | '' = '';
  filterDate: string = '';
  errorMessage: string = '';

  constructor(
    private adjService: StockAdjustmentService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadAdjustments();
  }

  loadAdjustments(): void {
    this.errorMessage = '';
    this.adjService.getAllAdjustments().subscribe({
      next: (data) => {
        this.masterLogs = data || [];
        this.adjustments = [...this.masterLogs];
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Failed to fetch system adjustment logs directory', err)
    });
  }

  searchAdjustment(): void {
    const q = this.searchQuery.trim();
    if (!q) {
      this.applyLocalFiltersPipeline();
      return;
    }
    this.adjService.getAdjustmentByNumber(q).subscribe({
      next: (data) => {
        this.adjustments = data ? [data] : [];
        this.cdr.markForCheck();
      },
      error: () => {
        this.adjustments = [];
        this.cdr.markForCheck();
      }
    });
  }

  filterByReason(): void {
    this.applyLocalFiltersPipeline();
  }

  filterByStatus(): void {
    this.applyLocalFiltersPipeline();
  }

  filterByDate(): void {
    this.applyLocalFiltersPipeline();
  }

  createAdjustment(): void {
    this.router.navigate(['/stock-adjustments/create']);
  }

  viewDetails(id: number): void {
    this.router.navigate(['/stock-adjustments', id]);
  }

  cancelAdjustment(id: number): void {
    if (confirm('Are you sure you want to cancel this adjustment workflow sequence?')) {
      this.adjService.cancelAdjustment(id).subscribe({
        next: () => {
          alert('Stock Adjustment marked as CANCELLED successfully.');
          this.loadAdjustments();
        },
        error: (err) => this.handleError('Cancellation action rejected', err)
      });
    }
  }

  refresh(): void {
    this.searchQuery = '';
    this.selectedReason = '';
    this.selectedStatus = '';
    this.filterDate = '';
    this.loadAdjustments();
  }

  private applyLocalFiltersPipeline(): void {
    let result = [...this.masterLogs];

    if (this.selectedReason) {
      result = result.filter(x => x.items?.some(i => i.reason === this.selectedReason));
    }
    if (this.selectedStatus) {
      result = result.filter(x => x.status === this.selectedStatus);
    }
    if (this.filterDate) {
      result = result.filter(x => x.adjustmentDate.toString().startsWith(this.filterDate));
    }

    this.adjustments = result;
    this.cdr.markForCheck();
  }

  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}
