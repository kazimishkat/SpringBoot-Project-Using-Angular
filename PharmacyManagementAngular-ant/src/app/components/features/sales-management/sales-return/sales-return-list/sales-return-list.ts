import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { SalesReturnService } from '../../../../../services/sales-return.service';
import { SalesReturnResponse, ApprovalStatus } from '../../../../../models/sales-return.model';

@Component({
  selector: 'app-sales-return-list',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './sales-return-list.html',
  styleUrl: './sales-return-list.css'
})
export class SalesReturnListComponent implements OnInit {

  returns: SalesReturnResponse[] = [];
  masterLogs: SalesReturnResponse[] = [];
  approvalStatuses = Object.values(ApprovalStatus);

  searchKeyword = '';
  selectedStatus: ApprovalStatus | '' = '';
  errorMessage = '';

  constructor(
    private salesReturnService: SalesReturnService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.getAllReturns();
  }

  getAllReturns(): void {
    this.errorMessage = '';
    this.salesReturnService.getAllReturns().subscribe({
      next: (data) => {
        this.masterLogs = data || [];
        this.returns = [...this.masterLogs];
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Failed to fetch sales return history', err)
    });
  }

  searchReturns(): void {
    const keyword = this.searchKeyword.trim();
    if (!keyword) {
      this.getAllReturns();
      return;
    }
    this.salesReturnService.searchReturns(keyword).subscribe({
      next: (data) => {
        this.returns = data || [];
        this.cdr.markForCheck();
      },
      error: () => this.applyLocalFilters()
    });
  }

  filterReturns(): void {
    const status = this.selectedStatus || undefined;
    this.salesReturnService.filterReturns(undefined, status).subscribe({
      next: (data) => {
        this.returns = data || [];
        this.cdr.markForCheck();
      },
      error: () => this.applyLocalFilters()
    });
  }

  viewDetails(id: number): void {
    this.router.navigate(['/dashboard/sales-returns', id]);
  }

  createReturn(): void {
    this.router.navigate(['/dashboard/sales-returns/create']);
  }

  refresh(): void {
    this.searchKeyword = '';
    this.selectedStatus = '';
    this.getAllReturns();
  }

  private applyLocalFilters(): void {
    let result = [...this.masterLogs];

    if (this.searchKeyword.trim()) {
      const q = this.searchKeyword.trim().toLowerCase();
      result = result.filter(x =>
        x.returnNumber.toLowerCase().includes(q) ||
        x.invoiceNumber.toLowerCase().includes(q)
      );
    }

    if (this.selectedStatus) {
      result = result.filter(x => x.status === this.selectedStatus);
    }

    this.returns = result;
    this.cdr.markForCheck();
  }

  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}