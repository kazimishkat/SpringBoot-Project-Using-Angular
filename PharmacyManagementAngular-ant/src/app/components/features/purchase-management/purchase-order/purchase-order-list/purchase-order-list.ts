import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterModule } from '@angular/router';
import { PurchaseOrderResponse, PurchaseOrderStatus } from '../../../../../models/purchase-order.model';
import { PurchaseOrderService } from '../../../../../services/purchase-order.service';

@Component({
  selector: 'app-purchase-order-list',
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './purchase-order-list.html',
  styleUrl: './purchase-order-list.css'
})
export class PurchaseOrderList implements OnInit {

  // =====================================================
  // VIEW GRID & STATE FILTERS CONFIGURATIONS
  // =====================================================
  purchaseOrders: PurchaseOrderResponse[] = [];
  statuses = Object.values(PurchaseOrderStatus);
  selectedStatus: PurchaseOrderStatus | '' = '';
  searchPoNumber: string = '';
  errorMessage: string = '';

  constructor(
    private poService: PurchaseOrderService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadAllOrders();
  }

  // =====================================================
  // CATALOG RECORD EXTRACTION SEQUENCES
  // =====================================================
  /** Retrieve all recorded purchase order invoices from tables */
  loadAllOrders(): void {
    this.errorMessage = '';
    this.selectedStatus = '';
    this.poService.getAllPurchaseOrders().subscribe({
      next: (data) => {
        this.purchaseOrders = data || [];
        this.cdr.markForCheck(); // Push explicit view layout updates
      },
      error: (err) => this.handleError('Failed to pull system purchase orders directory', err)
    });
  }

  // =====================================================
  // ADAPTIVE CRITERIA INTERCEPTORS
  // =====================================================
  /** Trigger precise filtering routines based on dropdown state selection */
  onStatusFilterChange(): void {
    if (!this.selectedStatus) {
      this.loadAllOrders();
      return;
    }

    this.errorMessage = '';
    this.poService.getPurchaseOrdersByStatus(this.selectedStatus).subscribe({
      next: (data) => {
        this.purchaseOrders = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Status matching criteria pipeline failed', err)
    });
  }

  /** Lookup individual document rows using exact invoice number tokens */
  onSearchByPoNumber(): void {
    const query = this.searchPoNumber.trim();
    if (!query) {
      this.loadAllOrders();
      return;
    }

    this.errorMessage = '';
    this.poService.getPurchaseOrderByPoNumber(query).subscribe({
      next: (data) => {
        this.purchaseOrders = data ? [data] : [];
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('PO number criteria evaluation aborted', err)
    });
  }

  /** Drops non-committed files completely from tracking rows */
  deleteOrder(id: number): void {
    if (confirm('Are you completely sure you want to drop this purchase order registration trace?')) {
      this.poService.deletePurchaseOrder(id).subscribe({
        next: () => {
          alert('Purchase Order references dropped successfully');
          this.loadAllOrders();
        },
        error: (err) => this.handleError('Purge action halted by server constraints', err)
      });
    }
  }

  // =====================================================
  // EXCEPTION ROUTER INTERCEPTOR LOGGER
  // =====================================================
  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}