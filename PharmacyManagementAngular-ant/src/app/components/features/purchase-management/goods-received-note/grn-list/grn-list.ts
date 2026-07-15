import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterModule } from '@angular/router';
import { GoodsReceivedNoteService } from '../../../../../services/goods-received-note.service';
import { GoodsReceivedNoteResponse, ApprovalStatus } from '../../../../../models/goods-received-note.model';

@Component({
  selector: 'app-grn-list',
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './grn-list.html',
  styleUrl: './grn-list.css'
})
export class GoodsReceivedNoteList implements OnInit {

  // =====================================================
  // GRID SEARCH & ADVANCED CRITERIA FILTERS STATE
  // =====================================================
  grnList: GoodsReceivedNoteResponse[] = [];
  statuses = Object.values(ApprovalStatus);
  
  // Search filter bindings requested
  searchGrnNumber: string = '';
  searchSupplierName: string = ''; 
  searchDate: string = '';         
  selectedStatus: ApprovalStatus | '' = '';
  searchPoNumber: string = '';

  errorMessage: string = '';

  constructor(
    private grnService: GoodsReceivedNoteService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadAllGrnRecords();
  }

  // =====================================================
  // CATALOG DATA FETCH ROUTINES
  // =====================================================
  /** Retrieve complete goods received logs dataset matrix from core engines */
  loadAllGrnRecords(): void {
    this.errorMessage = '';
    this.grnService.getAllGrns().subscribe({
      next: (data) => {
        this.grnList = data || [];
        this.cdr.markForCheck(); // Trigger precise change detection cycle check
      },
      error: (err) => this.handleError('Failed to populate core GRN transaction sheets', err)
    });
  }

  // =====================================================
  // ADAPTIVE FILTER SELECTION PIPELINES
  // =====================================================
  /** Intercepts advanced options to trigger conditional filtering routines */
  onAdvancedSearchFilter(): void {
    this.errorMessage = '';

    // 1. If search matches specific GRN tokens string format
    if (this.searchGrnNumber.trim()) {
      this.grnService.getGrnByNumber(this.searchGrnNumber.trim()).subscribe({
        next: (data) => {
          this.grnList = data ? [data] : [];
          this.cdr.markForCheck();
        },
        error: (err) => this.handleError('GRN unique code index target resolution aborted', err)
      });
      return;
    }

    // 2. Evaluate filter routing based on dynamic workflows status fields
    if (this.selectedStatus) {
      this.grnService.getGrnsByStatus(this.selectedStatus).subscribe({
        next: (data) => {
          this.grnList = data || [];
          this.applyLocalFiltersFallback();
        },
        error: (err) => this.handleError('Workflow state metrics matching aborted', err)
      });
      return;
    }

    // Default tracking path: Pull structural base indexes and compile local configurations shifts
    this.grnService.getAllGrns().subscribe({
      next: (data) => {
        this.grnList = data || [];
        this.applyLocalFiltersFallback();
      },
      error: (err) => this.handleError('Reset baseline metrics failed', err)
    });
  }

  /** Performs inline localized property evaluations over master catalog collections */
  private applyLocalFiltersFallback(): void {
    if (this.searchSupplierName.trim()) {
      const sName = this.searchSupplierName.trim().toLowerCase();
      this.grnList = this.grnList.filter(x => x.receivedByName?.toLowerCase().includes(sName) || x.poNumber?.toLowerCase().includes(sName));
    }
    
    if (this.searchDate) {
      this.grnList = this.grnList.filter(x => x.receivedDate.toString().startsWith(this.searchDate));
    }

    if (this.searchPoNumber.trim()) {
      const poNum = this.searchPoNumber.trim().toLowerCase();
      this.grnList = this.grnList.filter(x => x.poNumber?.toLowerCase().includes(poNum));
    }
    
    this.cdr.markForCheck();
  }

  // =====================================================
  // EXCEPTION ROUTING WRAPPER LOGGER
  // =====================================================
  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}