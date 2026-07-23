import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { PaymentService } from '../../../../../services/payment.service';
import { PaymentResponse } from '../../../../../models/payment.model';

@Component({
  selector: 'app-payment-details',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './payment-details.html',
  styleUrl: './payment-details.css'
})
export class PaymentDetailsComponent implements OnInit {

  paymentId!: number;
  paymentDetails: PaymentResponse | null = null;
  errorMessage = '';

  allPayments: PaymentResponse[] = [];

  constructor(
    private paymentService: PaymentService,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.paymentService.getAllPayments().subscribe({
      next: (data) => {
        this.allPayments = data || [];
        this.route.paramMap.subscribe(params => {
          const id = params.get('id');
          if (id) {
            this.paymentId = Number(id);
            this.getPaymentById(this.paymentId);
          } else if (this.allPayments.length > 0) {
            this.paymentId = this.allPayments[0].id;
            this.getPaymentById(this.paymentId);
          }
        });
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Failed to prefetch payment history:', err);
      }
    });
  }

  onPaymentChange(event: any): void {
    const selectedId = Number(event.target.value);
    if (selectedId) {
      this.paymentId = selectedId;
      this.getPaymentById(selectedId);
      this.router.navigate(['/dashboard/payments', selectedId]);
    }
  }

  getPaymentById(id: number): void {
    this.errorMessage = '';
    this.paymentService.getPaymentById(id).subscribe({
      next: (data) => {
        this.paymentDetails = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Failed to fetch payment details: ${err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  viewParentInvoice(): void {
    if (this.paymentDetails?.invoiceId) {
      this.router.navigate(['/dashboard/sales-invoices', this.paymentDetails.invoiceId]);
    }
  }

  printReceipt(): void {
    window.print();
  }

  deletePayment(): void {
    if (!this.paymentDetails) return;
    if (!confirm('Are you sure you want to delete this payment transaction record?')) return;

    this.paymentService.deletePayment(this.paymentDetails.id).subscribe({
      next: () => {
        alert('Payment record removed successfully.');
        this.location.back();
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Failed to delete payment: ${err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  goBack(): void {
    this.location.back();
  }
}