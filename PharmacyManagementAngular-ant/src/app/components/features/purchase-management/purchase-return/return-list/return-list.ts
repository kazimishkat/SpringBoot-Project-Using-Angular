import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { PurchaseReturnService } from '../../../../../services/purchase-return.service';
import { SupplierService } from '../../../../../services/supplier.service';
import { PurchaseReturnResponse, ApprovalStatus } from '../../../../../models/purchase-return.model';

@Component({
  selector: 'app-return-list',
  imports: [FormsModule, CommonModule],
  templateUrl: './return-list.html',
  styleUrl: './return-list.css'
})
export class ReturnListComponent implements OnInit {

  returns: PurchaseReturnResponse[] = [];
  masterLogs: PurchaseReturnResponse[] = [];
  suppliers: any[] = [];
  approvalStatuses = Object.values(ApprovalStatus);

  searchQuery = '';
  selectedStatus: ApprovalStatus | '' = '';
  selectedSupplierId: number | '' = '';
  errorMessage = '';

  constructor(
    private returnService: PurchaseReturnService,
    private supplierService: SupplierService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadReturns();
    this.loadSuppliers();
  }

  loadReturns(): void {
    this.errorMessage = '';
    this.returnService.getAllPurchaseReturns().subscribe({
      next: (data) => {
        this.masterLogs = data || [];
        this.returns = [...this.masterLogs];
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Failed to load supplier return ledger', err)
    });
  }

  loadSuppliers(): void {
    this.supplierService.getAllSuppliers().subscribe(data => {
      this.suppliers = data || [];
      this.cdr.markForCheck();
    });
  }

  searchReturns(): void {
    const query = this.searchQuery.trim();
    if (!query) {
      this.applyLocalFiltersPipeline();
      return;
    }
    this.returnService.getPurchaseReturnByNumber(query).subscribe({
      next: (data) => {
        this.returns = data ? [data] : [];
        this.cdr.markForCheck();
      },
      error: () => {
        this.applyLocalFiltersPipeline();
      }
    });
  }

  filterByStatus(): void {
    if (this.selectedStatus) {
      this.returnService.getPurchaseReturnsByStatus(this.selectedStatus).subscribe({
        next: (data) => {
          this.returns = data || [];
          this.cdr.markForCheck();
        },
        error: () => this.applyLocalFiltersPipeline()
      });
      return;
    }
    this.applyLocalFiltersPipeline();
  }

  filterBySupplier(): void {
    if (this.selectedSupplierId) {
      this.returnService.getPurchaseReturnsBySupplier(Number(this.selectedSupplierId)).subscribe({
        next: (data) => {
          this.returns = data || [];
          this.cdr.markForCheck();
        },
        error: () => this.applyLocalFiltersPipeline()
      });
      return;
    }
    this.applyLocalFiltersPipeline();
  }

  viewDetails(id: number): void {
    this.router.navigate(['/dashboard/purchase-returns', id]);
  }

  createReturn(): void {
    this.router.navigate(['/dashboard/purchase-returns/create']);
  }

  refresh(): void {
    this.searchQuery = '';
    this.selectedStatus = '';
    this.selectedSupplierId = '';
    this.loadReturns();
  }

  private applyLocalFiltersPipeline(): void {
    let result = [...this.masterLogs];

    if (this.searchQuery.trim()) {
      const q = this.searchQuery.trim().toLowerCase();
      result = result.filter(x => 
        x.returnNumber?.toLowerCase().includes(q) || 
        x.supplierName?.toLowerCase().includes(q)
      );
    }

    if (this.selectedStatus) {
      result = result.filter(x => x.approvalStatus === this.selectedStatus);
    }

    if (this.selectedSupplierId) {
      result = result.filter(x => x.supplierId === Number(this.selectedSupplierId));
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