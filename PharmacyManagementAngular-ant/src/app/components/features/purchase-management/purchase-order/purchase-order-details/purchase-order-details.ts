import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink, RouterModule } from '@angular/router';
import { PurchaseOrderService } from '../../../../../services/purchase-order.service';
import { PurchaseOrderResponse, PurchaseOrderStatus } from '../../../../../models/purchase-order.model';

@Component({
  selector: 'app-purchase-order-details',
  imports: [CommonModule, RouterModule],
  templateUrl: './purchase-order-details.html',
  styleUrl: './purchase-order-details.css'
})
export class PurchaseOrderDetailsComponent implements OnInit {

  // =====================================================
  // CATALOG RECORD & METRICS DASHBOARD STATES
  // =====================================================
  masterPoList: PurchaseOrderResponse[] = [];
  
  poId!: number;
  poDetails: PurchaseOrderResponse | null = null;
  errorMessage = '';

  constructor(
    private poService: PurchaseOrderService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    // 1. Initialise the side profile catalog rows list array
    this.loadMasterCatalogIndexes();

    // 2. Continual parameter check loop monitoring shifts inside route URLs
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.poId = Number(idParam);
        this.fetchGranularDetailsPanel(this.poId);
      }
    });
  }

  // =====================================================
  // CATALOG RECOVERY SEQUENCES
  // =====================================================
  loadMasterCatalogIndexes(): void {
    this.poService.getAllPurchaseOrders().subscribe({
      next: (data) => {
        this.masterPoList = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Catalog inventory routing stream crashed', err)
    });
  }

  // =====================================================
  // SPECIFIC TARGET LINE DETAILS RESOLUTION
  // =====================================================
  fetchGranularDetailsPanel(id: number): void {
    this.errorMessage = '';
    this.poService.getPurchaseOrderById(id).subscribe({
      next: (data) => {
        this.poDetails = data;
        this.poId = id;
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Failed to capture targeted purchase ledger context', err)
    });
  }

  // =====================================================
  // COMPUTED METRICS READERS (SUBTOTALS CALC)
  // =====================================================
  /** Calculates subtotal parameter lines using arithmetic units price products */
  calculateItemSubtotal(price: number, quantity: number): number {
    return (price || 0) * (quantity || 0);
  }

  /** Calculate total value summary aggregation sums across array lengths */
  calculateOrderGrandTotal(): number {
    if (!this.poDetails || !this.poDetails.items) return 0;
    return this.poDetails.items.reduce((acc, item) => acc + (item.unitPrice * item.orderedQuantity), 0);
  }

  // =====================================================
  // LIFECYCLE ACTION MUTATIONS
  // =====================================================
  /** Approves the active focused document parameters tracking state */
  approveOrder(): void {
    if (!confirm('Approve this purchase order layout configuration?')) return;
    
    this.errorMessage = '';
    this.poService.approvePurchaseOrder(this.poId).subscribe({
      next: (res) => {
        alert('Purchase order approved configuration updated');
        this.loadMasterCatalogIndexes();
        this.fetchGranularDetailsPanel(this.poId);
      },
      error: (err) => this.interceptError('Approval transaction declined', err)
    });
  }

  /** Rejects the active focused document parameters tracking state */
  rejectOrder(): void {
    if (!confirm('Are you sure you want to reject this purchase allocation?')) return;
    
    this.errorMessage = '';
    this.poService.rejectPurchaseOrder(this.poId).subscribe({
      next: (res) => {
        alert('Purchase order marked as rejected');
        this.loadMasterCatalogIndexes();
        this.fetchGranularDetailsPanel(this.poId);
      },
      error: (err) => this.interceptError('Rejection transaction declined', err)
    });
  }

  // =====================================================
  // PRINT & INWARD STOCKS DOWNSTREAM CALLS
  // =====================================================
  /** Format structural components nodes view sheets out to local OS print utilities */
  printInvoiceReceipt(): void {
    window.print();
  }

  /** 
   * Transition order to RECEIVED state, automatically writing audit ledger 
   * entries via internal state transitions.
   */
  generateGoodReceivedNote(): void {
    if (!this.poDetails) return;
    
    if (this.poDetails.status !== PurchaseOrderStatus.APPROVED) {
      alert('GRN generation requires an APPROVED purchase order state sequence status first');
      return;
    }

    if (!confirm('Generate GRN? This action updates system stock balances instantly via backend services.')) {
      return;
    }

    this.errorMessage = '';
    this.poService.updatePurchaseOrderStatus(this.poId, PurchaseOrderStatus.RECEIVED).subscribe({
      next: (res) => {
        alert('GRN Processed: Inward stocks ledger allocations injected successfully');
        this.loadMasterCatalogIndexes();
        this.fetchGranularDetailsPanel(this.poId);
      },
      error: (err) => this.interceptError('Fulfillment operations failed at backend level', err)
    });
  }

  // =====================================================
  // GLOBAL TRANSLATOR RUNTIME EXCEPTIONS
  // =====================================================
  private interceptError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}