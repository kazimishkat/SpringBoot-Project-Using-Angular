import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterModule } from '@angular/router';
import { BranchInventoryResponse } from '../../../../../models/branch-inventory.model';
import { BranchResponse } from '../../../../../models/branch.model';
import { BranchInventoryService } from '../../../../../services/branch-inventory.service';
import { BranchService } from '../../../../../services/branch.service';


@Component({
  selector: 'app-branch-inventory-list',
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './branch-inventory-list.html',
  styleUrl: './branch-inventory-list.css'
})
export class BranchInventoryList implements OnInit {

  // =====================================================
  // VIEW GRID, DROPDOWN & FILTER STATES
  // =====================================================
  inventories: BranchInventoryResponse[] = [];
  branches: BranchResponse[] = [];
  selectedBranchId: number | null = null;
  errorMessage: string = '';

  constructor(
    private inventoryService: BranchInventoryService,
    private branchService: BranchService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadAllInventories();
    this.loadBranchesDropdown();
  }

  // =====================================================
  // LOAD SYSTEM INVENTORIES
  // =====================================================
  /**
   * Retrieve all stock allocation records across all branches.
   */
  loadAllInventories(): void {
    this.errorMessage = '';
    this.inventoryService.getAll().subscribe({
      next: (data) => {
        this.inventories = data || [];
        this.cdr.markForCheck(); // Trigger precise change detection rendering cycle
      },
      error: (err) => {
        this.handleError('Failed to fetch structural inventory logs', err);
      }
    });
  }

  /** Load active branches to populate the layout filter dropdown */
  loadBranchesDropdown(): void {
    this.branchService.getActiveBranches().subscribe({
      next: (data) => {
        this.branches = data || [];
        this.cdr.markForCheck();
      }
    });
  }

  // =====================================================
  // FILTER STRATEGIES
  // =====================================================
  /**
   * Triggers specific branch contextual listings when dropdown option shifts.
   */
  onBranchFilterChange(): void {
    if (!this.selectedBranchId) {
      this.loadAllInventories();
      return;
    }

    this.errorMessage = '';
    this.inventoryService.getByBranch(this.selectedBranchId).subscribe({
      next: (data) => {
        this.inventories = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.handleError('Failed to filter inventories by selected branch', err);
      }
    });
  }

  // =====================================================
  // WIPE RECORD TRANSACTIONS
  // =====================================================
  /**
   * Drops specific stock rows from system databases permanently using IDs.
   */
  deleteInventory(id: number): void {
    if (confirm('Are you sure you want to completely purge this batch inventory record?')) {
      this.inventoryService.deleteInventory(id).subscribe({
        next: () => {
          // Hot reload local logs after complete structural drop operations
          if (this.selectedBranchId) {
            this.onBranchFilterChange();
          } else {
            this.loadAllInventories();
          }
        },
        error: (err) => {
          this.handleError('Inventory destruction command rejected by server rules', err);
        }
      });
    }
  }

  // =====================================================
  // MASTER EXCEPTION INTERCEPTOR LOGGER
  // =====================================================
  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}
