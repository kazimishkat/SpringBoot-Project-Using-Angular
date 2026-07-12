import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterModule } from '@angular/router';
import { BranchResponse } from '../../../../../models/branch.model';
import { BranchService } from '../../../../../services/branch.service';


@Component({
  selector: 'app-branch-list',
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './branch-list.html',
  styleUrl: './branch-list.css'
})
export class BranchListComponent implements OnInit {

  // =====================================================
  // MASTER ARRAY VIEW & FILTER CONFIGURATIONS
  // =====================================================
  branches: BranchResponse[] = [];
  searchQuery: string = '';
  errorMessage: string = '';

  constructor(
    private branchService: BranchService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadAllBranches();
  }

  // =====================================================
  // LOAD SYSTEM INVENTORY
  // =====================================================
  /**
   * Retrieve all branches documented in system parameters.
   */
  loadAllBranches(): void {
    this.errorMessage = '';
    this.branchService.getAllBranches().subscribe({
      next: (data) => {
        this.branches = data || [];
        this.cdr.markForCheck(); // Trigger precise change detection cycle check
      },
      error: (err) => {
        this.handleError('Failed to fetch structural branches indexes', err);
      }
    });
  }

  // =====================================================
  // ADAPTIVE FILTER SELECTION STRATEGIES
  // =====================================================
  /**
   * Evaluates inputs dynamically to determine exact route codes vs text name searches.
   */
  onSearch(): void {
    const query = this.searchQuery.trim();

    if (!query) {
      this.loadAllBranches();
      return;
    }

    this.errorMessage = '';

    // If query starts with common identifier codes or represents short key length ranges
    if (query.toUpperCase().startsWith('BR') || query.length <= 6) {
      this.branchService.getBranchByCode(query).subscribe({
        next: (data) => {
          this.branches = data ? [data] : [];
          this.cdr.markForCheck();
        },
        error: () => {
          // Gracefully default back to text name lookup chains if exact code matching crashes
          this.searchByNameFallback(query);
        }
      });
    } else {
      this.searchByNameFallback(query);
    }
  }

  /** Helper method executing text keyword query intercepts */
  private searchByNameFallback(query: string): void {
    this.branchService.searchBranchesByName(query).subscribe({
      next: (data) => {
        this.branches = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.handleError('Branch database keyword validation failed', err);
      }
    });
  }

  // =====================================================
  // WIPE RECORD TRANSACTIONS
  // =====================================================
  /**
   * Drops specific branch configurations permanently using database primary identifiers.
   */
  deleteBranch(id: number): void {
    if (confirm('Are you completely sure you want to drop this branch profile?')) {
      this.branchService.deleteBranch(id).subscribe({
        next: () => {
          this.loadAllBranches(); // Reload grid matrix arrays smoothly
        },
        error: (err) => {
          this.handleError('File destruction command rejected by database security', err);
        }
      });
    }
  }

  // =====================================================
  // UNIVERSAL CONTEXT INTERCEPTOR EXCEPTION LOGGER
  // =====================================================
  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}