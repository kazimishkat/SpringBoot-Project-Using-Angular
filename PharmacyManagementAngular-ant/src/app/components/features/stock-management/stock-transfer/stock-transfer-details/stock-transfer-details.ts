import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { StockTransferService } from '../../../../../services/stock-transfer.service';
import { StockTransferResponse, TransferStatus } from '../../../../../models/stock-transfer.model';

@Component({
  selector: 'app-stock-transfer-details',
  imports: [CommonModule, RouterLink],
  templateUrl: './stock-transfer-details.html',
  styleUrl: './stock-transfer-details.css'
})
export class StockTransferDetailsComponent implements OnInit {

  transferId!: number;
  transferDetails: StockTransferResponse | null = null;
  errorMessage = '';

  constructor(
    private transferService: StockTransferService,
    private route: ActivatedRoute,
    private location: Location,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.transferId = Number(idParam);
        this.loadTransfer();
      }
    });
  }

  loadTransfer(): void {
    this.errorMessage = '';
    this.transferService.getTransferById(this.transferId).subscribe({
      next: (data) => {
        this.transferDetails = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Failed to resolve layout properties card: ${err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  approveTransfer(): void {
    if (!confirm('Approve and DISPATCH this shipment manifest?')) return;

    this.errorMessage = '';
    const currentUserId = 1; 
    this.transferService.approveTransfer(this.transferId, currentUserId).subscribe({
      next: () => {
        alert('Stock Transfer operational status marked as DISPATCHED.');
        this.loadTransfer();
      },
      error: (err) => this.interceptError('Approval processing rejected', err)
    });
  }

  cancelTransfer(): void {
    if (!confirm('Are you sure you want to cancel this transfer log sequence?')) return;

    this.errorMessage = '';
    this.transferService.cancelTransfer(this.transferId).subscribe({
      next: () => {
        alert('Stock Transfer marked as CANCELLED.');
        this.loadTransfer();
      },
      error: (err) => this.interceptError('Cancellation execution pipeline failed', err)
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
