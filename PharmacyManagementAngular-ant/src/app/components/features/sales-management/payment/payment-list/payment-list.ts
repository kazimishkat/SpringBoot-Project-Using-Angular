import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { PaymentService } from '../../../../../services/payment.service';
import { PaymentResponse, PaymentMethod } from '../../../../../models/payment.model';

@Component({
  selector: 'app-payment-list',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './payment-list.html',
  styleUrl: './payment-list.css'
})
export class PaymentListComponent implements OnInit {

  payments: PaymentResponse[] = [];
  masterLogs: PaymentResponse[] = [];
  paymentMethods = Object.values(PaymentMethod);

  searchKeyword = '';
  selectedMethod: PaymentMethod | '' = '';
  fromDate = '';
  toDate = '';
  errorMessage = '';

  constructor(
    private paymentService: PaymentService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.getAllPayments();
  }

  getAllPayments(): void {
    this.errorMessage = '';
    this.paymentService.getAllPayments().subscribe({
      next: (data) => {
        this.masterLogs = data || [];
        this.payments = [...this.masterLogs];
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Failed to fetch payment transaction ledger', err)
    });
  }

  getPaymentById(id: number): void {
    this.router.navigate(['/dashboard/payments', id]);
  }

  getPaymentsByInvoice(invoiceId: number): void {
    this.errorMessage = '';
    this.paymentService.getPaymentsByInvoice(invoiceId).subscribe({
      next: (data) => {
        this.payments = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Failed to fetch payments for invoice', err)
    });
  }

  searchPayments(keyword?: string): void {
    const query = (keyword !== undefined ? keyword : this.searchKeyword).trim();
    if (!query) {
      this.getAllPayments();
      return;
    }
    this.paymentService.searchPayments(query).subscribe({
      next: (data) => {
        this.payments = data || [];
        this.cdr.markForCheck();
      },
      error: () => this.applyLocalFilters()
    });
  }

  filterPayments(fromDate?: string, toDate?: string): void {
    const start = fromDate || (this.fromDate ? `${this.fromDate}T00:00:00` : undefined);
    const end = toDate || (this.toDate ? `${this.toDate}T23:59:59` : undefined);
    const method = this.selectedMethod || undefined;

    this.paymentService.filterPayments(start, end, undefined, method).subscribe({
      next: (data) => {
        this.payments = data || [];
        this.cdr.markForCheck();
      },
      error: () => this.applyLocalFilters()
    });
  }

  viewDetails(id: number): void {
    this.router.navigate(['/dashboard/payments', id]);
  }

  refresh(): void {
    this.searchKeyword = '';
    this.selectedMethod = '';
    this.fromDate = '';
    this.toDate = '';
    this.getAllPayments();
  }

  private applyLocalFilters(): void {
    let result = [...this.masterLogs];

    if (this.searchKeyword.trim()) {
      const q = this.searchKeyword.trim().toLowerCase();
      result = result.filter(x =>
        x.invoiceNumber?.toLowerCase().includes(q) ||
        (x.transactionNumber && x.transactionNumber.toLowerCase().includes(q))
      );
    }

    if (this.selectedMethod) {
      result = result.filter(x => x.paymentMethod === this.selectedMethod);
    }

    this.payments = result;
    this.cdr.markForCheck();
  }

  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}