import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SupplierService } from '../../../../services/supplier.service';
import { SupplierResponse } from '../../../../models/supplier.model';

@Component({
  selector: 'app-supplier-list',
  imports: [FormsModule, CommonModule],
  templateUrl: './supplier-list.html',
  styleUrl: './supplier-list.css'
})
export class SupplierListComponent implements OnInit {

  // =========================
  // VIEW GRID & FILTER STATE
  // =========================
  suppliers: SupplierResponse[] = [];
  searchCodeQuery: string = '';
  errorMessage: string = '';

  constructor(
    private supplierService: SupplierService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadAllSuppliers();
  }

  // =====================================================
  // LOAD SUPPLIER REGISTRY
  // =====================================================
  /**
   * Retrieve complete recorded suppliers dashboard database records.
   */
  loadAllSuppliers(): void {
    this.errorMessage = '';
    this.supplierService.getAllSuppliers().subscribe({
      next: (data) => {
        this.suppliers = data || [];
        this.cdr.markForCheck(); // Push explicit updates down to UI components
      },
      error: (err) => {
        this.handleError('Failed to load supplier directory indexes', err);
      }
    });
  }

  // =====================================================
  // FILTER STRATEGY
  // =====================================================
  /**
   * Trigger unique profile routing match using code lookup strings.
   */
  onSearchByCode(): void {
    if (!this.searchCodeQuery.trim()) {
      this.loadAllSuppliers();
      return;
    }

    this.supplierService.getSupplierByCode(this.searchCodeQuery.trim()).subscribe({
      next: (data) => {
        this.suppliers = data ? [data] : [];
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.handleError('Supplier registry profile search failed', err);
      }
    });
  }

  // =====================================================
  // PURGE TRANSACTION
  // =====================================================
  /**
   * Disposes specific entity accounts using primary identity numbers.
   */
  deleteSupplier(id: number): void {
    if (confirm('Are you sure you want to permanently drop this supplier from system records?')) {
      this.supplierService.deleteSupplier(id).subscribe({
        next: () => {
          // Hot reload local list indexes following complete database drop actions
          this.loadAllSuppliers();
        },
        error: (err) => {
          this.handleError('Corporate file destruction request rejected', err);
        }
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