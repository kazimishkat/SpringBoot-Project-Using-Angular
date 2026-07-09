import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BranchResponse } from '../../../../../models/branch.model';
import { BranchService } from '../../../../../services/branch.service';


@Component({
  selector: 'app-branch-list',
  imports: [FormsModule, CommonModule],
  templateUrl: './branch-list.html',
  styleUrl: './branch-list.css'
})
export class BranchListComponent implements OnInit {

  // =========================
  // VIEW GRID & FILTER STATE
  // =========================
  branches: BranchResponse[] = [];
  searchCodeQuery: string = '';
  errorMessage: string = '';

  constructor(
    private branchService: BranchService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadAllBranches();
  }

  // =====================================================
  // LOAD BRANCH DIRECTORY
  // =====================================================
  /**
   * Retrieve all recorded branches from back-end server memory.
   */
  loadAllBranches(): void {
    this.errorMessage = '';
    this.branchService.getAllBranches().subscribe({
      next: (data) => {
        this.branches = data || [];
        this.cdr.markForCheck(); // Explicit target hierarchy rendering push
      },
      error: (err) => this.handleError('Failed to load branches data indices', err)
    });
  }

  // =====================================================
  // FILTER STRATEGY
  // =====================================================
  /**
   * Trigger unique branch code lookup matching endpoint queries.
   */
  onSearchByCode(): void {
    if (!this.searchCodeQuery.trim()) {
      this.loadAllBranches();
      return;
    }

    this.branchService.getBranchByCode(this.searchCodeQuery.trim()).subscribe({
      next: (data) => {
        this.branches = data ? [data] : [];
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Branch search operation failed', err)
    });
  }

  // =====================================================
  // PURGE TRANSACTION
  // =====================================================
  /**
   * Disposes specific branch entity allocations permanently by database ID.
   */
  deleteBranch(id: number): void {
    if (confirm('Are you sure you want to delete this branch tracking reference permanently?')) {
      this.branchService.deleteBranch(id).subscribe({
        next: () => {
          this.loadAllBranches(); // Reload grid indexes seamlessly
        },
        error: (err) => this.handleError('File removal operation rejected', err)
      });
    }
  }

  // =====================================================
  // COMMON ERROR HANDLER
  // =====================================================
  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}
