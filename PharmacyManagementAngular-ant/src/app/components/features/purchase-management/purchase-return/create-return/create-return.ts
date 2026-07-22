import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { PurchaseReturnService } from '../../../../../services/purchase-return.service';
import { SupplierService } from '../../../../../services/supplier.service';
import { BranchService } from '../../../../../services/branch.service';
import { PurchaseOrderService } from '../../../../../services/purchase-order.service';
import { MedicineBatchService } from '../../../../../services/medicine-batch.service';
import { PurchaseReturnRequest, ReturnReason, ApprovalStatus } from '../../../../../models/purchase-return.model';

@Component({
  selector: 'app-create-return',
  imports: [FormsModule, CommonModule],
  templateUrl: './create-return.html',
  styleUrl: './create-return.css'
})
export class CreateReturnComponent implements OnInit {

  @ViewChild('returnForm') returnForm!: NgForm;

  suppliers: any[] = [];
  branches: any[] = [];
  purchaseOrders: any[] = [];
  availableBatches: any[] = [];
  reasons = Object.values(ReturnReason);

  selectedPoId: number | '' = '';
  returnRequest!: PurchaseReturnRequest;

  submitted = false;
  errorMessage = '';

  constructor(
    private returnService: PurchaseReturnService,
    private supplierService: SupplierService,
    private branchService: BranchService,
    private poService: PurchaseOrderService,
    private batchService: MedicineBatchService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.resetForm();
    this.loadSuppliers();
    this.loadBranches();
  }

  loadSuppliers(): void {
    this.supplierService.getAllSuppliers().subscribe(data => {
      this.suppliers = data || [];
      this.cdr.markForCheck();
    });
  }

  loadBranches(): void {
    this.branchService.getAllBranches().subscribe({
      next: (data) => {
        this.branches = (data || []).filter(b => b.isActive !== false);
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Failed to load branches:', err);
        this.cdr.markForCheck();
      }
    });
  }

  /**
   * 🟢 Uses existing getAllPurchaseOrders() and filters client-side by supplier ID.
   * No backend changes required!
   */
  loadPurchaseOrders(): void {
    if (!this.returnRequest.supplierId) {
      this.purchaseOrders = [];
      this.selectedPoId = '';
      return;
    }

    const targetSupplierId = Number(this.returnRequest.supplierId);

    this.poService.getAllPurchaseOrders().subscribe({
      next: (data) => {
        // Client-side filtering matching the chosen supplier
        this.purchaseOrders = (data || []).filter(
          po => po.supplierId === targetSupplierId
        );
        this.selectedPoId = '';
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Failed to load purchase orders:', err);
        this.purchaseOrders = [];
        this.cdr.markForCheck();
      }
    });
  }

  /**
   * 🟢 Uses existing getPurchaseOrderById(id) to fetch selected PO details and items.
   */
  loadPurchaseItems(): void {
    if (!this.selectedPoId) return;

    const poId = Number(this.selectedPoId);

    this.poService.getPurchaseOrderById(poId).subscribe({
      next: (po) => {
        if (po && po.items && po.items.length > 0) {
          this.returnRequest.items = po.items.map(i => ({
            batchId: undefined as any,
            quantity: 1,
            reason: ReturnReason.DAMAGED,
            creditAmount: i.unitPrice || 0,
            medicineId: i.medicineId
          }));
          this.fetchBatchesForMedicines();
        }
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Failed to load purchase order items:', err);
        this.cdr.markForCheck();
      }
    });
  }

  fetchBatchesForMedicines(): void {
    this.batchService.getAllBatches().subscribe(batches => {
      this.availableBatches = batches || [];
      this.cdr.markForCheck();
    });
  }

  addItem(): void {
    this.returnRequest.items.push({
      batchId: undefined as any,
      quantity: 1,
      reason: ReturnReason.DAMAGED,
      creditAmount: 0
    });
    this.cdr.markForCheck();
  }

  removeItem(index: number): void {
    if (this.returnRequest.items.length > 1) {
      this.returnRequest.items.splice(index, 1);
      this.cdr.markForCheck();
    }
  }

  calculateTotal(): number {
    if (!this.returnRequest || !this.returnRequest.items) return 0;
    return this.returnRequest.items.reduce((sum, item) => {
      const qty = item.quantity || 0;
      const amt = item.creditAmount || 0;
      return sum + (qty * amt);
    }, 0);
  }

  submitReturn(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.returnForm.invalid) {
      return;
    }

    this.returnService.createPurchaseReturn(this.returnRequest).subscribe({
      next: () => {
        alert('Purchase Return lodged successfully. Inventory debited automatically.');
        this.router.navigate(['/dashboard/purchase-returns']);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Creation failed: ${err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  resetForm(): void {
    this.returnRequest = {
      returnNumber: 'RET-' + Math.floor(100000 + Math.random() * 900000),
      supplierId: undefined as any,
      branchId: undefined as any,
      returnDate: new Date().toISOString().split('T')[0],
      approvalStatus: ApprovalStatus.PENDING,
      items: [{ batchId: undefined as any, quantity: 1, reason: ReturnReason.DAMAGED, creditAmount: 0 }],
      isActive: true
    };
    this.selectedPoId = '';
    this.purchaseOrders = [];
    this.submitted = false;
    this.errorMessage = '';

    if (this.returnForm) {
      this.returnForm.resetForm({
        returnNumber: this.returnRequest.returnNumber,
        returnDate: this.returnRequest.returnDate
      });
    }
    this.cdr.markForCheck();
  }

  cancel(): void {
    this.router.navigate(['/dashboard/purchase-returns']);
  }
}
