import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { SalesInvoiceService } from '../../../../../services/sales-invoice.service';
import { CustomerService } from '../../../../../services/customer.service';
import { SalesInvoiceResponse, InvoiceStatus } from '../../../../../models/sales-invoice.model';

@Component({
  selector: 'app-invoice-list',
  imports: [FormsModule, CommonModule],
  templateUrl: './invoice-list.html',
  styleUrl: './invoice-list.css'
})
export class InvoiceListComponent implements OnInit {

  invoices: SalesInvoiceResponse[] = [];
  masterLogs: SalesInvoiceResponse[] = [];
  customers: any[] = [];
  invoiceStatuses = Object.values(InvoiceStatus);

  searchKeyword = '';
  selectedCustomerId: number | '' = '';
  selectedStatus: InvoiceStatus | '' = '';
  fromDate = '';
  toDate = '';
  errorMessage = '';

  constructor(
    private invoiceService: SalesInvoiceService,
    private customerService: CustomerService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.getAllInvoices();
    this.loadCustomers();
  }

  getAllInvoices(): void {
    this.errorMessage = '';
    this.invoiceService.getAllInvoices().subscribe({
      next: (data) => {
        this.masterLogs = data || [];
        this.invoices = [...this.masterLogs];
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Failed to fetch sales invoice directory', err)
    });
  }

  loadCustomers(): void {
    this.customerService.getAllCustomers().subscribe(data => {
      this.customers = data || [];
      this.cdr.markForCheck();
    });
  }

  searchInvoices(): void {
    if (!this.searchKeyword.trim()) {
      this.getAllInvoices();
      return;
    }
    this.invoiceService.searchInvoices(this.searchKeyword.trim()).subscribe({
      next: (data) => {
        this.invoices = data || [];
        this.cdr.markForCheck();
      },
      error: () => this.applyLocalFilters()
    });
  }

  filterInvoices(): void {
    const custId = this.selectedCustomerId ? Number(this.selectedCustomerId) : undefined;
    const status = this.selectedStatus || undefined;
    const start = this.fromDate ? `${this.fromDate}T00:00:00` : undefined;
    const end = this.toDate ? `${this.toDate}T23:59:59` : undefined;

    this.invoiceService.filterInvoices(start, end, custId, status).subscribe({
      next: (data) => {
        this.invoices = data || [];
        this.cdr.markForCheck();
      },
      error: () => this.applyLocalFilters()
    });
  }

  filterInvoicesByCustomer(): void {
    if (this.selectedCustomerId) {
      this.invoiceService.filterInvoicesByCustomer(Number(this.selectedCustomerId)).subscribe({
        next: (data) => {
          this.invoices = data || [];
          this.cdr.markForCheck();
        },
        error: () => this.applyLocalFilters()
      });
      return;
    }
    this.filterInvoices();
  }

  filterInvoicesByStatus(): void {
    if (this.selectedStatus) {
      this.invoiceService.filterInvoicesByStatus(this.selectedStatus).subscribe({
        next: (data) => {
          this.invoices = data || [];
          this.cdr.markForCheck();
        },
        error: () => this.applyLocalFilters()
      });
      return;
    }
    this.filterInvoices();
  }

  viewDetails(id: number): void {
    this.router.navigate(['/dashboard/sales-invoices', id]);
  }

  createInvoice(): void {
    this.router.navigate(['/dashboard/sales-invoices/create']);
  }

  refresh(): void {
    this.searchKeyword = '';
    this.selectedCustomerId = '';
    this.selectedStatus = '';
    this.fromDate = '';
    this.toDate = '';
    this.getAllInvoices();
  }

  private applyLocalFilters(): void {
    let result = [...this.masterLogs];

    if (this.searchKeyword.trim()) {
      const q = this.searchKeyword.trim().toLowerCase();
      result = result.filter(x => 
        x.invoiceNumber.toLowerCase().includes(q) || 
        (x.customerName && x.customerName.toLowerCase().includes(q))
      );
    }

    if (this.selectedCustomerId) {
      result = result.filter(x => x.customerId === Number(this.selectedCustomerId));
    }

    if (this.selectedStatus) {
      result = result.filter(x => x.status === this.selectedStatus);
    }

    this.invoices = result;
    this.cdr.markForCheck();
  }

  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}