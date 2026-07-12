import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { SupplierService } from '../../../../services/supplier.service';
import { SupplierResponse } from '../../../../models/supplier.model';

@Component({
  selector: 'app-supplier-list',
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './supplier-list.html',
  styleUrl: './supplier-list.css'
})
export class SupplierListComponent implements OnInit {

  // =====================================================
  // VIEW GRID & FILTER STATE
  // =====================================================
  suppliers: SupplierResponse[] = [];
  searchQuery: string = ''; 
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
  // FILTER STRATEGY (DYNAMIC SEARCH BY CODE OR NAME)
  // =====================================================
  /**
   * Trigger profile lookup matching either specific string codes or corporate name keywords.
   */
  onSearch(): void {
    const query = this.searchQuery.trim();

    // 1. Reload all data if the search query field is cleared
    if (!query) {
      this.loadAllSuppliers();
      return;
    }

    this.errorMessage = '';

    // 2. Check if query looks like a code format (e.g., starts with SUP or represents short technical keys)
    if (query.toUpperCase().startsWith('SUP') || query.length <= 6) { 
      this.supplierService.getSupplierByCode(query).subscribe({
        next: (data) => {
          this.suppliers = data ? [data] : [];
          this.cdr.markForCheck();
        },
        error: () => {
          // Fallback seamlessly to text-matching name searches if exact code route fails
          this.searchByNameFallback(query);
        }
      });
    } else {
      // 3. Otherwise, directly execute default text query pipeline routing
      this.searchByNameFallback(query);
    }
  }

  /** 
   * Helper method to execute name based registry querying 
   */
  private searchByNameFallback(query: string): void {
    this.supplierService.searchSuppliersByName(query).subscribe({
      next: (data) => {
        this.suppliers = data || [];
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