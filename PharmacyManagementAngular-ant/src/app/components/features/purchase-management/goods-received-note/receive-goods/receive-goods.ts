import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { GoodsReceivedNoteService } from '../../../../../services/goods-received-note.service';
import { PurchaseOrderService } from '../../../../../services/purchase-order.service';
import { GoodsReceivedNoteRequest, GoodsReceivedNoteItemRequest, ApprovalStatus } from '../../../../../models/goods-received-note.model';
import { PurchaseOrderResponse } from '../../../../../models/purchase-order.model';

@Component({
  selector: 'app-receive-goods',
  imports: [FormsModule, CommonModule],
  templateUrl: './receive-goods.html',
  styleUrl: './receive-goods.css'
})
export class ReceiveGoods implements OnInit {

  @ViewChild('grnForm') grnForm!: NgForm;

  // =====================================================
  // DYNAMIC LOOKUPS & TRANSACTION OBJECT STATES
  // =====================================================
  purchaseOrders: PurchaseOrderResponse[] = [];
  selectedOrderDetails: PurchaseOrderResponse | null = null;
  
  statuses = [ApprovalStatus.APPROVED, ApprovalStatus.PENDING];

  grnRequest!: GoodsReceivedNoteRequest;
  submitted = false;
  errorMessage = '';

  constructor(
    private grnService: GoodsReceivedNoteService,
    private poService: PurchaseOrderService,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.resetFormStructure();
    this.loadApprovedPurchaseOrders();
  }

  /** Load only approved purchase orders ready for warehouse stocking operations */
  private loadApprovedPurchaseOrders(): void {
    this.poService.getAllPurchaseOrders().subscribe({
      next: (data) => {
        this.purchaseOrders = data ? data.filter(x => x.status.toString() === 'APPROVED') : [];
        
        // Auto-select purchase order if poId query parameter is present in URL
        const poIdParam = this.route.snapshot.queryParamMap.get('poId');
        if (poIdParam) {
          const poId = Number(poIdParam);
          this.grnRequest.purchaseOrderId = poId;
          this.onPurchaseOrderSelectionChange(poId);
        }

        this.cdr.markForCheck();
      }
    });
  }

  // =====================================================
  // SOURCE INTERCEPT RELATION TRIGGER SELECTORS
  // =====================================================
  /** Cascades selection modifications to load matching tracking properties and build tables rows */
  onPurchaseOrderSelectionChange(orderId: number): void {
    this.selectedOrderDetails = null;
    this.grnRequest.items = [];
    
    if (!orderId) {
      this.cdr.markForCheck();
      return;
    }

    const match = this.purchaseOrders.find(x => x.id === Number(orderId));
    if (match) {
      this.selectedOrderDetails = match;
      
      // Auto build items structure array mapping upstream layout requirements
      if (match.items && match.items.length > 0) {
        this.grnRequest.items = match.items.map(poItem => {
          return {
            medicineId: poItem.medicineId,
            batchNumber: '', // Form input required from UI layer
            manufactureDate: '',
            expiryDate: '',
            receivedQuantity: poItem.orderedQuantity, 
            purchasePrice: poItem.unitPrice,
            sellingPrice: Number((poItem.unitPrice * 1.25).toFixed(2)) // Pre-calculate 25% profit margin setup
          };
        });
      }
    }
    this.cdr.markForCheck();
  }

  // =====================================================
  // TRANS-COMMIT ACTIONS PERSISTENCE ROUTINES
  // =====================================================
  /** Validates array segments blocks and dispatches final payload packets to services */
  saveReceivedGoods(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.grnForm.invalid || !this.grnRequest.purchaseOrderId) {
      this.errorMessage = 'Please select an Approved Purchase Order and fill in all required fields correctly (Batch No, Expiry Date, Qty, Cost, and Sell price for each item).';
      this.grnForm.control.markAllAsTouched();
      this.cdr.markForCheck();
      return;
    }

    this.grnService.receiveGoods(this.grnRequest).subscribe({
      next: (res) => {
        alert('Goods Received Note Document Logged Into Stock Engine Successfully! Inventory stock updated.');
        this.resetFormStructure();
        this.loadApprovedPurchaseOrders(); 
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Inward transaction layout rejected: ${err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  // =====================================================
  // STATE CLEANUP LIFE CYCLES RESET
  // =====================================================
  resetFormStructure(): void {
    this.grnRequest = {
      grnNumber: 'GRN-' + Math.floor(100000 + Math.random() * 900000), 
      purchaseOrderId: undefined as any,
      receivedDate: new Date().toISOString().split('T')[0],
      receivedById: 1, 
      approvalStatus: ApprovalStatus.APPROVED,
      items: [],
      isActive: true
    };
    
    this.selectedOrderDetails = null;
    this.submitted = false;
    this.errorMessage = '';

    if (this.grnForm) {
      this.grnForm.resetForm({
        grnNumber: this.grnRequest.grnNumber,
        receivedDate: this.grnRequest.receivedDate,
        approvalStatus: ApprovalStatus.APPROVED
      });
    }
    this.cdr.markForCheck();
  }
}