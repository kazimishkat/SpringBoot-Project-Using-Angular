import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { SalesReturnService } from '../../../../../services/sales-return.service';
import { SalesInvoiceService } from '../../../../../services/sales-invoice.service';
import { StorageService } from '../../../../../services/storage.service';
import { SalesReturnRequest, SalesReturnItemRequest, ReturnReason } from '../../../../../models/sales-return.model';
import { SalesInvoiceResponse } from '../../../../../models/sales-invoice.model';

@Component({
  selector: 'app-create-sales-return',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './create-sales-return.html',
  styleUrl: './create-sales-return.css'
})
export class CreateSalesReturnComponent implements OnInit {

  @ViewChild('returnForm') returnForm!: NgForm;

  reasons = Object.values(ReturnReason);
  invoiceNumberQuery = '';
  selectedInvoice: SalesInvoiceResponse | null = null;
  returnRequest!: SalesReturnRequest;

  submitted = false;
  errorMessage = '';

  constructor(
    private salesReturnService: SalesReturnService,
    private invoiceService: SalesInvoiceService,
    private storageService: StorageService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.resetForm();

    this.route.queryParams.subscribe(params => {
      if (params['invoiceId']) {
        this.getInvoiceById(Number(params['invoiceId']));
      } else if (params['invoiceNumber']) {
        this.invoiceNumberQuery = params['invoiceNumber'];
        this.searchInvoice();
      }
    });
  }

  searchInvoice(): void {
    if (!this.invoiceNumberQuery.trim()) return;
    this.errorMessage = '';

    // 🟢 Uses the added getInvoiceByNumber method
    this.invoiceService.getInvoiceByNumber(this.invoiceNumberQuery.trim()).subscribe({
      next: (invoice) => {
        this.setupInvoiceItems(invoice);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Sales Invoice not found with that number.';
        this.selectedInvoice = null;
        this.cdr.markForCheck();
      }
    });
  }

  getInvoiceById(invoiceId: number): void {
    this.errorMessage = '';
    this.invoiceService.getInvoiceById(invoiceId).subscribe({
      next: (invoice) => {
        this.setupInvoiceItems(invoice);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Failed to fetch parent invoice details.';
        this.cdr.markForCheck();
      }
    });
  }

  setupInvoiceItems(invoice: SalesInvoiceResponse): void {
    this.selectedInvoice = invoice;
    this.returnRequest.invoiceId = invoice.id;
    this.invoiceNumberQuery = invoice.invoiceNumber;

    if (invoice.items && invoice.items.length > 0) {
      // 🟢 Mapped to SalesReturnItemRequest properly
      this.returnRequest.items = invoice.items.map(item => ({
        batchId: item.batchId,
        quantity: item.quantity,
        reason: ReturnReason.CUSTOMER_DISSATISFACTION,
        refundAmount: (item.unitPrice || 0) * item.quantity
      }));
    }
    this.cdr.markForCheck();
  }

  addItem(): void {
    this.returnRequest.items.push({
      batchId: undefined as any,
      quantity: 1,
      reason: ReturnReason.CUSTOMER_DISSATISFACTION,
      refundAmount: 0
    });
    this.cdr.markForCheck();
  }

  removeItem(index: number): void {
    if (this.returnRequest.items.length > 1) {
      this.returnRequest.items.splice(index, 1);
      this.cdr.markForCheck();
    }
  }

  calculateReturnAmount(): number {
    if (!this.returnRequest || !this.returnRequest.items) return 0;
    return this.returnRequest.items.reduce((total, item) => total + (item.refundAmount || 0), 0);
  }

  // 🟢 Correctly typed parameter as SalesReturnItemRequest
  onQuantityOrPriceChange(item: SalesReturnItemRequest): void {
    if (this.selectedInvoice) {
      const originalItem = this.selectedInvoice.items.find(i => i.batchId === item.batchId);
      if (originalItem) {
        const unitPrice = originalItem.unitPrice || 0;
        item.refundAmount = (item.quantity || 0) * unitPrice;
      }
    }
    this.cdr.markForCheck();
  }

  submitReturn(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.returnForm.invalid || !this.returnRequest.invoiceId) {
      if (!this.returnRequest.invoiceId) {
        this.errorMessage = 'Please select a valid parent Sales Invoice.';
      }
      return;
    }

    this.salesReturnService.createReturn(this.returnRequest).subscribe({
      next: (res) => {
        alert('Sales Return submitted successfully! Status set to PENDING.');
        this.router.navigate(['/dashboard/sales-returns', res.id]);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Submission failed: ${err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  resetForm(): void {
    const activeUser = this.storageService.getUser();

    this.returnRequest = {
      returnNumber: 'SRET-' + Math.floor(100000 + Math.random() * 900000),
      invoiceId: undefined as any,
      returnDate: new Date().toISOString().split('T')[0],
      processedById: activeUser?.userId || 1,
      items: []
    };

    this.selectedInvoice = null;
    this.invoiceNumberQuery = '';
    this.submitted = false;
    this.errorMessage = '';
    this.cdr.markForCheck();
  }

  cancel(): void {
    this.router.navigate(['/dashboard/sales-returns']);
  }
}