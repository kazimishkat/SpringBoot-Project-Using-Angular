import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { StockTransferService } from '../../../../../services/stock-transfer.service';
import { StockTransferResponse, TransferStatus } from '../../../../../models/stock-transfer.model';

@Component({
  selector: 'app-stock-transfer-details',
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './stock-transfer-details.html',
  styleUrl: './stock-transfer-details.css'
})
export class StockTransferDetailsComponent implements OnInit {

  /** Master list of all available stock transfers for selector dropdown */
  transfersList: StockTransferResponse[] = [];

  transferId!: number;
  transferDetails: StockTransferResponse | null = null;
  searchQuery: string = '';
  isLoading: boolean = false;
  errorMessage = '';

  constructor(
    private transferService: StockTransferService,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    // 1. Fetch available stock transfers for registry dropdown
    this.loadAllTransfers();

    // 2. Listen to route param changes (e.g. /dashboard/stock-transfers/details/:id or /stock-transfers/:id)
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.transferId = Number(idParam);
        this.fetchTransferDetails(this.transferId);
      }
    });

    // 3. Listen to query params (e.g. ?id=1)
    this.route.queryParamMap.subscribe(queryParams => {
      const queryId = queryParams.get('id');
      if (queryId && !this.transferId) {
        this.transferId = Number(queryId);
        this.fetchTransferDetails(this.transferId);
      }
    });
  }

  /**
   * Load master registry of stock transfers.
   */
  loadAllTransfers(): void {
    this.transferService.getAllTransfers().subscribe({
      next: (data) => {
        this.transfersList = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Failed to load stock transfers list', err);
        this.cdr.markForCheck();
      }
    });
  }

  /**
   * Clears selection and navigates back to list directory view.
   */
  clearSelection(): void {
    this.transferDetails = null;
    this.transferId = undefined!;
    this.router.navigate(['/dashboard/stock-transfers/details']);
  }

  /**
   * Fetch details for a specific transfer by numeric ID.
   */
  fetchTransferDetails(id: number): void {
    if (!id || isNaN(id)) return;

    this.isLoading = true;
    this.errorMessage = '';
    this.transferId = id;

    this.transferService.getTransferById(id).subscribe({
      next: (data) => {
        this.transferDetails = data;
        this.isLoading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.transferDetails = null;
        this.isLoading = false;
        this.errorMessage = `Failed to load stock transfer details: ${err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  /**
   * Selection change handler for transfer drop-down menu.
   */
  onSelectTransfer(targetId: string | number): void {
    const numericId = Number(targetId);
    if (numericId) {
      this.router.navigate(['/dashboard/stock-transfers/details', numericId]);
    }
  }

  /**
   * Search transfer by ID or Transfer Number.
   */
  onSearch(query: string): void {
    const trimmed = query ? query.trim() : '';
    if (!trimmed) return;

    this.isLoading = true;
    this.errorMessage = '';

    if (!isNaN(Number(trimmed))) {
      this.fetchTransferDetails(Number(trimmed));
    } else {
      this.transferService.getByTransferNumber(trimmed).subscribe({
        next: (data) => {
          if (data) {
            this.transferDetails = data;
            this.transferId = data.id;
          } else {
            this.transferDetails = null;
            this.errorMessage = `No transfer found matching record "${trimmed}".`;
          }
          this.isLoading = false;
          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error(err);
          this.transferDetails = null;
          this.isLoading = false;
          this.errorMessage = `Transfer lookup failed: ${err.error || err.message || 'Not Found'}`;
          this.cdr.markForCheck();
        }
      });
    }
  }

  approveTransfer(): void {
    if (!this.transferId) return;
    if (!confirm('Approve and DISPATCH this shipment manifest?')) return;

    this.errorMessage = '';
    const currentUserId = 1; 
    this.transferService.approveTransfer(this.transferId, currentUserId).subscribe({
      next: () => {
        alert('Stock Transfer operational status marked as DISPATCHED.');
        this.loadAllTransfers();
        this.fetchTransferDetails(this.transferId);
      },
      error: (err) => this.interceptError('Approval processing rejected', err)
    });
  }

  receiveTransfer(): void {
    if (!this.transferId) return;
    this.router.navigate(['/dashboard/stock-transfers/approve', this.transferId]);
  }

  cancelTransfer(): void {
    if (!this.transferId) return;
    if (!confirm('Are you sure you want to cancel this transfer log sequence?')) return;

    this.errorMessage = '';
    this.transferService.cancelTransfer(this.transferId).subscribe({
      next: () => {
        alert('Stock Transfer marked as CANCELLED.');
        this.loadAllTransfers();
        this.fetchTransferDetails(this.transferId);
      },
      error: (err) => this.interceptError('Cancellation execution pipeline failed', err)
    });
  }

  printManifest(): void {
    window.print();
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

