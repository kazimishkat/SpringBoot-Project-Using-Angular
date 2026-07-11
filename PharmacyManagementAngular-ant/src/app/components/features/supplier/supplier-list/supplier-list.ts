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

  // =========================
  // VIEW GRID & FILTER STATE
  // =========================
  suppliers: SupplierResponse[] = [];
  searchQuery: string = ''; // ভ্যারিয়েবলের নাম আরও জেনারেক (Query) করা হলো
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

    // ১. সার্চ বক্স খালি থাকলে সব সাপ্লায়ার রি-লোড হবে
    if (!query) {
      this.loadAllSuppliers();
      return;
    }

    this.errorMessage = '';

    // ২. যদি সার্চ কিউরি নির্দিষ্ট কোড ফরম্যাট ম্যাচ করে (যেমন সাধারণত MED- বা SUP- দিয়ে শুরু হয়)
    // অথবা আপনার ব্যাকএন্ডের রুলস অনুযায়ী চেক করতে চান, তাহলে কোড দিয়ে সার্চ করবে:
    if (query.toUpperCase().startsWith('SUP') || query.length <= 6) { 
      this.supplierService.getSupplierByCode(query).subscribe({
        next: (data) => {
          this.suppliers = data ? [data] : [];
          this.cdr.markForCheck();
        },
        error: (err) => {
          // যদি কোড দিয়ে না মিলে, তবে ব্যাকআপ হিসেবে নাম দিয়ে সার্চ ট্রাই করবে
          this.searchByNameFallback(query);
        }
      });
    } else {
      // ৩. অন্যথায় সরাসরি নাম দিয়ে সার্চ করার এপিআই কল করবে
      this.searchByNameFallback(query);
    }
  }

  /** Helper method to execute name based registry querying */
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