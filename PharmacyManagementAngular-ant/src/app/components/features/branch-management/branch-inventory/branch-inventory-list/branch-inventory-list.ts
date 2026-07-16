import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterModule } from '@angular/router';
import { BranchInventoryService } from '../../../../../services/branch-inventory.service';
import { BranchService } from '../../../../../services/branch.service';
import { BranchInventoryResponse } from '../../../../../models/branch-inventory.model';

@Component({
  selector: 'app-inventory-list',
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './branch-inventory-list.html',
  styleUrl: './branch-inventory-list.css'
})
export class BranchInventoryList implements OnInit {

  // =====================================================
  // GRID CONTROL WORKFLOWS & ADVANCED FILTERS STATES
  // =====================================================
  inventoryList: BranchInventoryResponse[] = [];
  branches: any[] = [];

  // Local lookup filter inputs requested
  searchMedicineQuery: string = '';
  selectedBranchId: number | '' = '';
  searchCategoryQuery: string = '';
  isLowStockFilter = false;
  isOutOfStockFilter = false;
  expiryFilterDate: string = '';

  errorMessage = '';

  constructor(
    private inventoryService: BranchInventoryService,
    private branchService: BranchService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadInitialDirectoryData();
    this.loadBranchesDropdown();
  }

  private loadBranchesDropdown(): void {
    this.branchService.getActiveBranches().subscribe({
      next: (data) => {
        this.branches = data || [];
        this.cdr.markForCheck();
      }
    });
  }

  // =====================================================
  // DATAGRID RECOVERY CORE SERVICES
  // =====================================================
  /** Loads master index tracking sheets depending on selected toggles options */
  loadInitialDirectoryData(): void {
    this.errorMessage = '';

    if (this.isOutOfStockFilter) {
      this.inventoryService.getOutOfStock().subscribe({
        next: (res) => this.updateGridResult(res),
        error: (err) => this.handleError('Failed to pull out of stock indexes', err)
      });
      return;
    }

    if (this.isLowStockFilter) {
      this.inventoryService.getLowStock().subscribe({
        next: (res) => this.updateGridResult(res),
        error: (err) => this.handleError('Failed to pull low stock indexes', err)
      });
      return;
    }

    if (this.expiryFilterDate) {
      this.inventoryService.getExpiringInventory(this.expiryFilterDate).subscribe({
        next: (res) => this.updateGridResult(res),
        error: (err) => this.handleError('Failed to parse expiring items parameters', err)
      });
      return;
    }

    this.inventoryService.getAllInventory().subscribe({
      next: (res) => this.updateGridResult(res),
      error: (err) => this.handleError('Failed to load system inventory list', err)
    });
  }

  // =====================================================
  // ADAPTIVE TEXT MATCH SEARCH TRIGGERS
  // =====================================================
  /** Trigger precise filtering algorithms based on UI input bindings */
  onSearchPipelineTrigger(): void {
    this.loadInitialDirectoryData();
  }

  private updateGridResult(data: BranchInventoryResponse[]): void {
    this.inventoryList = data || [];
    this.applyLocalFiltersFallback();
  }

  /** Resolves string fields filters mapping Locally across active sheets records */
  private applyLocalFiltersFallback(): void {
    if (this.selectedBranchId) {
      this.inventoryList = this.inventoryList.filter(x => x.branchId === Number(this.selectedBranchId));
    }

    if (this.searchMedicineQuery.trim()) {
      const q = this.searchMedicineQuery.trim().toLowerCase();
      this.inventoryList = this.inventoryList.filter(x => 
        x.medicineBrandName?.toLowerCase().includes(q) || 
        x.batchNumber?.toLowerCase().includes(q)
      );
    }

    if (this.searchCategoryQuery.trim()) {
      const cat = this.searchCategoryQuery.trim().toLowerCase();
      this.inventoryList = this.inventoryList.filter(x => x.categoryName?.toLowerCase().includes(cat));
    }

    this.cdr.markForCheck(); // Push explicit state modifications down template layout nodes
  }

  // =====================================================
  // HELPER METRICS RENDER STRATEGIES
  // =====================================================
  getStockStatus(item: BranchInventoryResponse): string {
    if (item.quantityOnHand <= 0) return 'OUT_OF_STOCK';
    if (item.reorderLevel && item.quantityOnHand <= item.reorderLevel) return 'LOW_STOCK';
    return 'GOOD';
  }

  // =====================================================
  // UNIVERSAL EXCEPTION HANDLERS
  // =====================================================
  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}