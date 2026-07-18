import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { StockMovementService } from '../../../../../services/stock-movement.service';
import { StockMovementResponse, StockMovementType } from '../../../../../models/stock-movement.model';

@Component({
  selector: 'app-stock-movement-history',
  imports: [FormsModule, CommonModule],
  templateUrl: './stock-movement-history.html',
  styleUrl: './stock-movement-history.css'
})
export class StockMovementHistoryComponent implements OnInit {

  // =====================================================
  // GRID CONTROL DATAS & ADVANCED FILTER BOUNDARIES
  // =====================================================
  movements: StockMovementResponse[] = [];
  masterLogs: StockMovementResponse[] = []; // Base copy backing up local filters execution
  movementTypes = Object.values(StockMovementType);

  // Advanced binding queries variables matching checklist requirements
  searchQuery: string = '';
  branchIdFilter: number | '' = '';
  medicineIdFilter: number | '' = '';
  batchIdFilter: number | '' = '';
  selectedTypeFilter: StockMovementType | '' = '';
  filterDateStr: string = '';

  errorMessage: string = '';

  constructor(
    private movementService: StockMovementService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadMovements();
  }

  // =====================================================
  // LEDGER RECORD RETRIEVAL ENGINE CALLS
  // =====================================================
  loadMovements(): void {
    this.errorMessage = '';
    this.movementService.getAllMovements().subscribe({
      next: (data) => {
        this.masterLogs = data || [];
        this.movements = [...this.masterLogs];
        this.cdr.markForCheck(); // Push instant DOM layout calculations
      },
      error: (err) => this.handleError('Failed to populate system inventory audit paths', err)
    });
  }

  // =====================================================
  // CRITERIA INTERCEPTORS ACTIONS METHODS
  // =====================================================
  searchMovement(): void {
    this.applyCombinedFiltersPipeline();
  }

  filterByBranch(): void {
    if (this.branchIdFilter && this.selectedTypeFilter) {
      this.errorMessage = '';
      this.movementService.getByMovementType(Number(this.branchIdFilter), this.selectedTypeFilter).subscribe({
        next: (data) => {
          this.movements = data || [];
          this.cdr.markForCheck();
        },
        error: (err) => this.handleError('Server type routing intercept crashed', err)
      });
      return;
    }
    this.applyCombinedFiltersPipeline();
  }

  filterByMedicine(): void {
    this.applyCombinedFiltersPipeline();
  }

  filterByBatch(): void {
    if (this.batchIdFilter) {
      this.errorMessage = '';
      this.movementService.getByBatch(Number(this.batchIdFilter)).subscribe({
        next: (data) => {
          this.movements = data || [];
          this.cdr.markForCheck();
        },
        error: (err) => this.handleError('Target batch tracking row aborted', err)
      });
      return;
    }
    this.applyCombinedFiltersPipeline();
  }

  filterByMovementType(): void {
    this.filterByBranch(); // Cascade evaluation to process concurrent branch constraints
  }

  filterByDate(): void {
    this.applyCombinedFiltersPipeline();
  }

  // =====================================================
  // ROUTING & PERSISTENCE MANAGEMENT LOGICS
  // =====================================================
  goToDetails(id: number): void {
    this.router.navigate(['/stock-movements', id]);
  }

  refresh(): void {
    this.searchQuery = '';
    this.branchIdFilter = '';
    this.medicineIdFilter = '';
    this.batchIdFilter = '';
    this.selectedTypeFilter = '';
    this.filterDateStr = '';
    this.loadMovements();
  }

  // =====================================================
  // DYNAMIC LOCAL COMPILING PIPELINE ALGORITHMS
  // =====================================================
  private applyCombinedFiltersPipeline(): void {
    let result = [...this.masterLogs];

    if (this.searchQuery.trim()) {
      const q = this.searchQuery.trim().toLowerCase();
      result = result.filter(x => 
        x.medicineName?.toLowerCase().includes(q) || 
        x.batchNumber?.toLowerCase().includes(q) ||
        x.referenceType?.toLowerCase().includes(q)
      );
    }

    if (this.branchIdFilter) {
      result = result.filter(x => x.branchId === Number(this.branchIdFilter));
    }

    if (this.medicineIdFilter) {
      result = result.filter(x => x.medicineName?.toLowerCase().includes(this.medicineIdFilter.toString().toLowerCase()));
    }

    if (this.selectedTypeFilter) {
      result = result.filter(x => x.movementType === this.selectedTypeFilter);
    }

    if (this.filterDateStr) {
      result = result.filter(x => x.createdAt?.toString().startsWith(this.filterDateStr));
    }

    this.movements = result;
    this.cdr.markForCheck();
  }

  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}
