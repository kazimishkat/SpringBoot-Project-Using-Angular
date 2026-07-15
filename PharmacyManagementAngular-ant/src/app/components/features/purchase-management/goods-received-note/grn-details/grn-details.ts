import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink, RouterModule } from '@angular/router';
import { GoodsReceivedNoteService } from '../../../../../services/goods-received-note.service';
import { GoodsReceivedNoteResponse, ApprovalStatus } from '../../../../../models/goods-received-note.model';

@Component({
  selector: 'app-grn-details',
  imports: [CommonModule, RouterModule],
  templateUrl: './grn-details.html',
  styleUrl: './grn-details.css'
})
export class GoodsReceivedNoteDetails implements OnInit {

  // =====================================================
  // SIDE PANEL MASTER GRID ARRAY & DETAIL CONTEXT MATRIX
  // =====================================================
  masterGrnList: GoodsReceivedNoteResponse[] = [];
  
  grnId!: number;
  grnDetails: GoodsReceivedNoteResponse | null = null;
  errorMessage = '';

  constructor(
    private grnService: GoodsReceivedNoteService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    // 1. Recover full master sidebar catalogs lists
    this.loadMasterCatalogIndexes();

    // 2. Continual parameters monitor checking updates inside paths tracking streams
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.grnId = Number(idParam);
        this.fetchGranularDetailsPanel(this.grnId);
      }
    });
  }

  // =====================================================
  // MASTER CATALOG INDICES EXTRACTIONS
  // =====================================================
  loadMasterCatalogIndexes(): void {
    this.grnService.getAllGrns().subscribe({
      next: (data) => {
        this.masterGrnList = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Catalog inventory transaction stream crashed', err)
    });
  }

  // =====================================================
  // SPECIFIC TARGET LINE DETAILS RESOLUTION
  // =====================================================
  fetchGranularDetailsPanel(id: number): void {
    this.errorMessage = '';
    this.grnService.getGrnById(id).subscribe({
      next: (data) => {
        this.grnDetails = data;
        this.grnId = id;
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Failed to capture isolated GRN ledger fields properties', err)
    });
  }

  // =====================================================
  // COMPUTED CALCULATOR MATRIX READERS
  // =====================================================
  /** Evaluates linear value product lines */
  calculateItemLineTotal(price: number, quantity: number): number {
    return (price || 0) * (quantity || 0);
  }

  /** Accumulates summation metrics summary arrays total values */
  calculateGrnGrandTotal(): number {
    if (!this.grnDetails || !this.grnDetails.items) return 0;
    return this.grnDetails.items.reduce((acc, item) => acc + (item.purchasePrice * item.receivedQuantity), 0);
  }

  // =====================================================
  // INLINE LIFECYCLE OPERATION MUTATORS
  // =====================================================
  /** Commits approval status workflow shifts, creating real batches and posting ledger movements */
  approveGrnInvoice(): void {
    if (!confirm('Approve this GRN entry? This auto-generates inventory batches and logs stock movements.')) return;

    this.errorMessage = '';
    this.grnService.updateApprovalStatus(this.grnId, ApprovalStatus.APPROVED).subscribe({
      next: (res) => {
        alert('GRN workflow state approved: Medicine batches and inventory entries successfully created.');
        this.loadMasterCatalogIndexes();
        this.fetchGranularDetailsPanel(this.grnId);
      },
      error: (err) => this.interceptError('Workflow state conversion rejected by controller rules', err)
    });
  }

  /** Initiates high-level reverse transactions to subtract items values securely from inventory cards */
  cancelGrnInvoice(): void {
    if (!confirm('CRITICAL WARN: Are you completely sure you want to CANCEL this GRN? If previously approved, this triggers reverse stock entries.')) return;

    this.errorMessage = '';
    this.grnService.cancelGrn(this.grnId).subscribe({
      next: (res) => {
        alert('GRN marked as CANCELLED: Stock inventory records rolled back smoothly via reverse ledger entries.');
        this.loadMasterCatalogIndexes();
        this.fetchGranularDetailsPanel(this.grnId);
      },
      error: (err) => this.interceptError('Cancellation workflow pipeline processing failed', err)
    });
  }

  // =====================================================
  // EXTERNAL PRINT INTERACTION HANDLERS
  // =====================================================
  /** Intercepts layout document blocks to send straight out into hardware system devices */
  printGrnSheetReport(): void {
    window.print();
  }

  // =====================================================
  // GLOBAL CONTEXT EXCEPTIONS LOGGERS ROUTER
  // =====================================================
  private interceptError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}