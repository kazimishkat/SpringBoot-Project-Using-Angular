import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { StockTransferService } from '../../../../../services/stock-transfer.service';
import { StockTransferResponse, TransferStatus } from '../../../../../models/stock-transfer.model';

@Component({
  selector: 'app-stock-transfer-list',
  imports: [FormsModule, CommonModule],
  templateUrl: './stock-transfer-list.html',
  styleUrl: './stock-transfer-list.css'
})
export class StockTransferListComponent implements OnInit {

  transfers: StockTransferResponse[] = [];
  masterLogs: StockTransferResponse[] = [];
  statuses = Object.values(TransferStatus);

  searchQuery: string = '';
  selectedStatus: TransferStatus | '' = '';
  selectedBranchId: number | '' = '';
  filterDate: string = '';
  errorMessage: string = '';

  constructor(
    private transferService: StockTransferService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadTransfers();
  }

  loadTransfers(): void {
    this.errorMessage = '';
    this.transferService.getAllTransfers().subscribe({
      next: (data) => {
        this.masterLogs = data || [];
        this.transfers = [...this.masterLogs];
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Failed to load transfers directory', err)
    });
  }

  searchTransfer(): void {
    const query = this.searchQuery.trim();
    if (!query) {
      this.applyLocalFiltersPipeline();
      return;
    }
    this.transferService.getByTransferNumber(query).subscribe({
      next: (data) => {
        this.transfers = data ? [data] : [];
        this.cdr.markForCheck();
      },
      error: () => {
        this.transfers = [];
        this.cdr.markForCheck();
      }
    });
  }

  filterByStatus(): void {
    if (this.selectedStatus) {
      this.transferService.getByStatus(this.selectedStatus).subscribe({
        next: (data) => {
          this.transfers = data || [];
          this.cdr.markForCheck();
        },
        error: (err) => this.handleError('Status mapping endpoint crashed', err)
      });
      return;
    }
    this.applyLocalFiltersPipeline();
  }

  filterByBranch(): void {
    this.applyLocalFiltersPipeline();
  }

  filterByDate(): void {
    this.applyLocalFiltersPipeline();
  }

  createTransfer(): void {
    this.router.navigate(['/stock-transfers/create']);
  }

  approveTransfer(id: number): void {
    this.router.navigate(['/stock-transfers/approve', id]);
  }

  viewDetails(id: number): void {
    this.router.navigate(['/stock-transfers', id]);
  }

  refresh(): void {
    this.searchQuery = '';
    this.selectedStatus = '';
    this.selectedBranchId = '';
    this.filterDate = '';
    this.loadTransfers();
  }

  private applyLocalFiltersPipeline(): void {
    let result = [...this.masterLogs];

    if (this.selectedBranchId) {
      const bId = Number(this.selectedBranchId);
      result = result.filter(x => x.fromBranchId === bId || x.toBranchId === bId);
    }
    if (this.filterDate) {
      result = result.filter(x => x.transferDate.toString().startsWith(this.filterDate));
    }

    this.transfers = result;
    this.cdr.markForCheck();
  }

  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}
