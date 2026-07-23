import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { SalesInvoiceService } from '../../../../../services/sales-invoice.service';
import { SalesInvoiceResponse, PaymentResponse } from '../../../../../models/sales-invoice.model';

@Component({
  selector: 'app-invoice-details',
  imports: [CommonModule, RouterModule],
  templateUrl: './invoice-details.html',
  styleUrl: './invoice-details.css'
})
export class InvoiceDetailsComponent implements OnInit {

  invoiceId!: number;
  invoiceDetails: SalesInvoiceResponse | null = null;
  paymentHistory: PaymentResponse[] = [];
  errorMessage = '';

  allInvoices: SalesInvoiceResponse[] = [];

  constructor(
    private invoiceService: SalesInvoiceService,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.invoiceService.getAllInvoices().subscribe({
      next: (data) => {
        this.allInvoices = data || [];
        this.route.paramMap.subscribe(params => {
          const id = params.get('id');
          if (id) {
            this.invoiceId = Number(id);
            this.getInvoiceById(this.invoiceId);
            this.getInvoicePaymentHistory(this.invoiceId);
          } else if (this.allInvoices.length > 0) {
            this.invoiceId = this.allInvoices[0].id;
            this.getInvoiceById(this.invoiceId);
            this.getInvoicePaymentHistory(this.invoiceId);
          }
        });
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Failed to prefetch invoices:', err);
      }
    });
  }

  onInvoiceChange(event: any): void {
    const selectedId = Number(event.target.value);
    if (selectedId) {
      this.invoiceId = selectedId;
      this.getInvoiceById(selectedId);
      this.getInvoicePaymentHistory(selectedId);
      this.router.navigate(['/dashboard/sales-invoices/details', selectedId]);
    }
  }

  getInvoiceById(id: number): void {
    this.errorMessage = '';
    this.invoiceService.getInvoiceById(id).subscribe({
      next: (data) => {
        this.invoiceDetails = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Failed to fetch invoice details: ${err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  getInvoicePaymentHistory(id: number): void {
    // 🟢 Updated to match sales-invoice.service.ts method: getInvoicePayment(id)
    this.invoiceService.getInvoicePayment(id).subscribe({
      next: (payments) => {
        this.paymentHistory = payments || [];
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Failed to load payment logs:', err)
    });
  }

  printInvoice(id?: number): void {
    const targetId = id || this.invoiceId;
    if (targetId) {
      this.invoiceService.printInvoice(targetId);
    } else {
      window.print();
    }
  }

  downloadInvoicePdf(id: number): void {
    this.invoiceService.downloadInvoicePdf(id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `Invoice_${this.invoiceDetails?.invoiceNumber || id}.pdf`;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: (err) => this.handleError('Failed to download invoice PDF', err)
    });
  }

  cancelInvoice(id: number): void {
    if (!confirm('Are you sure you want to CANCEL this invoice? Debited inventory will be restored back to stock.')) return;

    this.invoiceService.cancelInvoice(id).subscribe({
      next: () => {
        alert('Invoice cancelled successfully. Stock restored.');
        this.getInvoiceById(id);
      },
      error: (err) => this.handleError('Failed to cancel invoice', err)
    });
  }

  createSalesReturn(): void {
    if (!this.invoiceDetails) return;
    this.router.navigate(['/dashboard/sales-returns/create'], {
      queryParams: { invoiceId: this.invoiceDetails.id, invoiceNumber: this.invoiceDetails.invoiceNumber }
    });
  }

  goBack(): void {
    this.location.back();
  }

  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}