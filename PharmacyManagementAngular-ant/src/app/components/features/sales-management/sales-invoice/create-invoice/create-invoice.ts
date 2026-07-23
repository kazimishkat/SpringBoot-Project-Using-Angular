import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { SalesInvoiceService } from '../../../../../services/sales-invoice.service';
import { CustomerService } from '../../../../../services/customer.service';
import { BranchService } from '../../../../../services/branch.service';
import { MedicineBatchService } from '../../../../../services/medicine-batch.service';
import { StorageService } from '../../../../../services/storage.service';
import { SalesInvoiceRequest, SalesInvoiceItemRequest, InvoiceStatus, DiscountType, PaymentMethod } from '../../../../../models/sales-invoice.model';
import { CustomerResponse } from '../../../../../models/customer.model';
import { CustomerAdd } from '../../../customer-management/customer-add/customer-add';

@Component({
  selector: 'app-create-invoice',
  standalone: true,
  imports: [FormsModule, CommonModule, CustomerAdd],
  templateUrl: './create-invoice.html',
  styleUrl: './create-invoice.css'
})
export class CreateInvoiceComponent implements OnInit {

  @ViewChild('invoiceForm') invoiceForm!: NgForm;

  branches: any[] = [];
  availableBatches: any[] = [];
  discountTypes = Object.values(DiscountType);
  paymentMethods = Object.values(PaymentMethod); // 👈 [NEW]: Array for Payment Methods dropdown

  // 🌟 Customer Search & Modal Logic Fields
  customerSearchQuery = '';
  customerSearchResults: CustomerResponse[] = [];
  selectedCustomer: CustomerResponse | null = null;
  isSearchingCustomer = false;
  hasCustomerSearched = false;
  showAddCustomerModal = false;

  invoiceRequest!: SalesInvoiceRequest;
  
  // Local Calc Fields
  vatPercentage = 5; // Default 5% VAT
  overallDiscountType: DiscountType = DiscountType.FIXED_AMOUNT;
  overallDiscountValue = 0;

  submitted = false;
  errorMessage = '';

  constructor(
    private invoiceService: SalesInvoiceService,
    private customerService: CustomerService,
    private branchService: BranchService,
    private batchService: MedicineBatchService,
    private storageService: StorageService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.resetForm();
    this.loadBranches();
    this.loadAvailableMedicineBatches();
  }

  loadBranches(): void {
    this.branchService.getActiveBranches().subscribe(data => {
      this.branches = data || [];
      this.cdr.markForCheck();
    });
  }

  loadAvailableMedicineBatches(): void {
    this.batchService.getAllBatches().subscribe(batches => {
      this.availableBatches = batches || [];
      this.cdr.markForCheck();
    });
  }

  // 🔍 ১. Customer Search Engine
  searchCustomer(): void {
    if (!this.customerSearchQuery.trim()) return;

    this.isSearchingCustomer = true;
    this.hasCustomerSearched = true;

    this.customerService.searchCustomers(this.customerSearchQuery.trim()).subscribe({
      next: (results) => {
        this.customerSearchResults = results || [];
        this.isSearchingCustomer = false;

        if (results.length === 1) {
          this.selectCustomer(results[0]);
        }
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Customer Search Error:', err);
        this.customerSearchResults = [];
        this.isSearchingCustomer = false;
        this.cdr.markForCheck();
      }
    });
  }

  // 🎯 ২. Select Customer Logic
  selectCustomer(customer: CustomerResponse): void {
    this.selectedCustomer = customer;
    this.invoiceRequest.customerId = customer.id;
    this.customerSearchResults = [];
    this.customerSearchQuery = '';
    this.hasCustomerSearched = false;
    this.cdr.markForCheck();
  }

  // ❌ ৩. Clear Selected Customer
  clearSelectedCustomer(): void {
    this.selectedCustomer = null;
    this.invoiceRequest.customerId = undefined;
    this.cdr.markForCheck();
  }

  // ➕ ৪. Customer Modal Trigger Methods
  openCustomerAddModal(): void {
    this.showAddCustomerModal = true;
    this.cdr.markForCheck();
  }

  closeCustomerAddModal(): void {
    this.showAddCustomerModal = false;
    this.cdr.markForCheck();
  }

  onCustomerSaved(newCustomer: CustomerResponse): void {
    this.closeCustomerAddModal();
    if (newCustomer) {
      this.selectCustomer(newCustomer);
    }
  }

  // ── Medicine & Invoice Line Calculations ──
  onBatchSelect(item: SalesInvoiceItemRequest, batchId: number): void {
    const selected = this.availableBatches.find(b => b.id === Number(batchId));
    if (selected) {
      item.unitPrice = selected.sellingPrice || 0;
      this.recalculateSummary();
    }
  }

  addMedicine(): void {
    this.invoiceRequest.items.push({
      batchId: undefined as any,
      quantity: 1,
      unitPrice: 0,
      discountType: DiscountType.FIXED_AMOUNT,
      discountValue: 0
    });
    this.recalculateSummary();
  }

  removeMedicine(index: number): void {
    if (this.invoiceRequest.items.length > 1) {
      this.invoiceRequest.items.splice(index, 1);
      this.recalculateSummary();
    }
  }

  calculateItemLineTotal(item: SalesInvoiceItemRequest): number {
    const base = (item.quantity || 0) * (item.unitPrice || 0);
    if (!item.discountValue || item.discountValue <= 0) return base;

    if (item.discountType === DiscountType.PERCENTAGE) {
      return base - (base * (item.discountValue / 100));
    } else {
      return Math.max(0, base - item.discountValue);
    }
  }

  calculateInvoiceTotal(): number {
    if (!this.invoiceRequest || !this.invoiceRequest.items) return 0;
    return this.invoiceRequest.items.reduce((sum, item) => sum + this.calculateItemLineTotal(item), 0);
  }

  calculateDiscount(): number {
    const subTotal = this.calculateInvoiceTotal();
    if (!this.overallDiscountValue || this.overallDiscountValue <= 0) return 0;

    if (this.overallDiscountType === DiscountType.PERCENTAGE) {
      return subTotal * (this.overallDiscountValue / 100);
    } else {
      return this.overallDiscountValue;
    }
  }

  calculateVat(): number {
    const subTotalAfterDiscount = this.calculateInvoiceTotal() - this.calculateDiscount();
    return Math.max(0, subTotalAfterDiscount * (this.vatPercentage / 100));
  }

  recalculateSummary(): void {
    const sub = this.calculateInvoiceTotal();
    const disc = this.calculateDiscount();
    const vat = this.calculateVat();
    const total = sub - disc + vat;

    this.invoiceRequest.subTotal = sub;
    this.invoiceRequest.discountAmount = disc;
    this.invoiceRequest.vatAmount = vat;
    this.invoiceRequest.totalAmount = total;

    if (this.invoiceRequest.status === InvoiceStatus.PAID) {
      this.invoiceRequest.paidAmount = total;
      this.invoiceRequest.dueAmount = 0;
    } else {
      this.invoiceRequest.dueAmount = Math.max(0, total - (this.invoiceRequest.paidAmount || 0));
    }

    this.cdr.markForCheck();
  }

  onPaidAmountChange(): void {
    const total = this.invoiceRequest.totalAmount || 0;
    const paid = this.invoiceRequest.paidAmount || 0;

    if (paid >= total) {
      this.invoiceRequest.dueAmount = 0;
      this.invoiceRequest.status = InvoiceStatus.PAID;
    } else if (paid > 0) {
      this.invoiceRequest.dueAmount = total - paid;
      this.invoiceRequest.status = InvoiceStatus.PARTIALLY_PAID;
    } else {
      this.invoiceRequest.dueAmount = total;
      this.invoiceRequest.status = InvoiceStatus.DRAFT;
    }
    this.cdr.markForCheck();
  }

  submitInvoice(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (!this.invoiceRequest.customerId) {
      this.errorMessage = 'Please search and select a customer before submitting.';
      this.cdr.markForCheck();
      return;
    }

    if (this.invoiceForm.invalid) {
      return;
    }

    this.recalculateSummary();

    this.invoiceService.createInvoice(this.invoiceRequest).subscribe({
      next: (res) => {
        alert('Sales Invoice created successfully. Inventory debited!');
        this.router.navigate(['/dashboard/sales-invoices', res.id]);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Creation failed: ${err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  resetForm(): void {
    const activeUser = this.storageService.getUser();
    
    this.invoiceRequest = {
      invoiceNumber: 'INV-' + Math.floor(100000 + Math.random() * 900000),
      branchId: activeUser?.branchId || 1,
      customerId: undefined,
      prescriptionId: undefined,
      soldById: activeUser?.userId || 1,
      invoiceDate: new Date().toISOString().substring(0, 16),
      subTotal: 0,
      discountAmount: 0,
      vatAmount: 0,
      totalAmount: 0,
      paidAmount: 0,
      dueAmount: 0,
      status: InvoiceStatus.PAID,
      paymentMethod: PaymentMethod.CASH, // 👈 [NEW]: Default set to CASH
      items: [{
        batchId: undefined as any,
        quantity: 1,
        unitPrice: 0,
        discountType: DiscountType.FIXED_AMOUNT,
        discountValue: 0
      }]
    };

    this.selectedCustomer = null;
    this.customerSearchQuery = '';
    this.customerSearchResults = [];
    this.hasCustomerSearched = false;
    this.submitted = false;
    this.errorMessage = '';
    this.cdr.markForCheck();
  }

  cancel(): void {
    this.router.navigate(['/dashboard/sales-invoices']);
  }
}