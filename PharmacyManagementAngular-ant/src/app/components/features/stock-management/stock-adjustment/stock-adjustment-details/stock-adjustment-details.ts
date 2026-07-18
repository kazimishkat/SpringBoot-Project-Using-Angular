import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { StockAdjustmentService } from '../../../../../services/stock-adjustment.service';
import { StockAdjustmentResponse, AdjustmentStatus } from '../../../../../models/stock-adjustment.model';

@Component({
  selector: 'app-stock-adjustment-details',
  imports: [CommonModule, RouterLink],
  templateUrl: './stock-adjustment-details.html',
  styleUrl: './stock-adjustment-details.css'
})
export class StockAdjustmentDetailsComponent implements OnInit {

  adjustmentId!: number;
  adjDetails: StockAdjustmentResponse | null = null;
  errorMessage = '';

  constructor(
    private adjService: StockAdjustmentService,
    private route: ActivatedRoute,
    private location: Location,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.adjustmentId = Number(idParam);
        this.loadAdjustment();
      }
    });
  }

  loadAdjustment(): void {
    this.errorMessage = '';
    this.adjService.getAdjustmentById(this.adjustmentId).subscribe({
      next: (data) => {
        this.adjDetails = data;
        // Mock status validation parameters sync if backend defaults to flattened values
        if (this.adjDetails && !this.adjDetails.status) {
          this.adjDetails.status = this.adjDetails.approvedBy ? AdjustmentStatus.APPROVED : AdjustmentStatus.PENDING;
        }
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Failed to extract isolated adjustment card indicators: ${err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  approveAdjustment(): void {
    if (!confirm('Approve this adjustment setup? This permanently validates systemic delta synchronization changes.')) return;
    
    this.errorMessage = '';
    const currentUserId = 1; // Simulated active supervisor token
    this.adjService.approveAdjustment(this.adjustmentId, currentUserId).subscribe({
      next: () => {
        alert('Stock Adjustment structure marked as APPROVED.');
        this.loadAdjustment();
      },
      error: (err) => this.interceptError('Approval signature execution aborted', err)
    });
  }

  cancelAdjustment(): void {
    if (!confirm('CRITICAL WARN: Are you sure you want to cancel this tracking sheet context?')) return;

    this.errorMessage = '';
    this.adjService.cancelAdjustment(this.adjustmentId).subscribe({
      next: () => {
        alert('Stock Adjustment status shifted to CANCELLED.');
        this.loadAdjustment();
      },
      error: (err) => this.interceptError('Cancellation pipeline execution aborted', err)
    });
  }

  goBack(): void {
    this.location.back();
  }

  private interceptError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}
