import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PurchaseOrderService } from '../../../../../services/purchase-order.service';
import { MedicineService } from '../../../../../services/medicine.service';
import { SupplierService } from '../../../../../services/supplier.service';
import { BranchService } from '../../../../../services/branch.service';
import { PurchaseOrderRequest, PurchaseOrderStatus, PurchaseOrderItemRequest } from '../../../../../models/purchase-order.model';

@Component({
  selector: 'app-create-purchase-order',
  imports: [FormsModule, CommonModule],
  templateUrl: './create-purchase-order.html',
  styleUrl: './create-purchase-order.css'
})
export class CreatePurchaseOrderComponent implements OnInit {

  @ViewChild('poForm') poForm!: NgForm;

  // =====================================================
  // SYSTEM DIRECTORIES LOOKUPS DATA DEPENDENCY
  // =====================================================
  suppliers: any[] = [];
  branches: any[] = [];
  medicines: any[] = [];

  // =====================================================
  // TRANSACTION MASTER FIELDS DATA STRUCTURE
  // =====================================================
  poRequest!: PurchaseOrderRequest;
  submitted = false;
  errorMessage = '';

  constructor(
    private poService: PurchaseOrderService,
    private supplierService: SupplierService,
    private branchService: BranchService,
    private medicineService: MedicineService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.resetForm();
    this.loadDependencyLookups();
  }

  private loadDependencyLookups(): void {
    this.supplierService.getAllSuppliers().subscribe(data => { this.suppliers = data || []; this.cdr.markForCheck(); });
    this.branchService.getActiveBranches().subscribe(data => { this.branches = data || []; this.cdr.markForCheck(); });
    this.medicineService.getAllMedicines().subscribe(data => { this.medicines = data || []; this.cdr.markForCheck(); });
  }

  // =====================================================
  // DYNAMIC ITEMS ARRAY BUILDERS MATRIX
  // =====================================================
  /** Append a new item grid line row parameters setup */
  addItemRow(): void {
    const item: PurchaseOrderItemRequest = {
      medicineId: undefined as any,
      orderedQuantity: 1,
      unitPrice: 0
    };
    this.poRequest.items.push(item);
    this.cdr.markForCheck();
  }

  /** Discard targeted indices lines blocks from the nested structure array */
  removeItemRow(index: number): void {
    if (this.poRequest.items.length > 1) {
      this.poRequest.items.splice(index, 1);
      this.cdr.markForCheck();
    }
  }

  // =====================================================
  // COMMITTED PERSISTENCE ACTIONS
  // =====================================================
  savePurchaseOrder(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.poForm.invalid) {
      return;
    }

    this.poService.createPurchaseOrder(this.poRequest).subscribe({
      next: (res) => {
        alert('Purchase Order Committed Successfully');
        this.resetForm();
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Creation payload aborted: ${err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  // =====================================================
  // COMPONENT RESET LAYOUTS LIFE CYCLES
  // =====================================================
  resetForm(): void {
    this.poRequest = {
      poNumber: '',
      supplierId: undefined as any,
      branchId: undefined as any,
      orderDate: new Date().toISOString().split('T')[0],
      expectedDeliveryDate: '',
      status: PurchaseOrderStatus.PENDING,
      items: [{ medicineId: undefined as any, orderedQuantity: 1, unitPrice: 0 }],
      isActive: true
    };
    
    this.submitted = false;
    this.errorMessage = '';

    if (this.poForm) {
      this.poForm.resetForm({
        orderDate: this.poRequest.orderDate,
        status: PurchaseOrderStatus.PENDING
      });
    }
    this.cdr.markForCheck();
  }
}