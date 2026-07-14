import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterModule } from '@angular/router';
import { GenericMedicineResponse } from '../../../../../models/generic-medicine.model';
import { GenericMedicineService } from '../../../../../services/generic-medicine.service';


@Component({
  selector: 'app-generic-list',
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './generic-list.html',
  styleUrl: './generic-list.css'
})
export class GenericListComponent implements OnInit {

  // =====================================================
  // MASTER ARRAY VIEW & FILTER CONFIGURATIONS
  // =====================================================
  genericMedicines: GenericMedicineResponse[] = [];
  searchQuery: string = '';
  errorMessage: string = '';

  constructor(
    private genericService: GenericMedicineService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadAllGenerics();
  }

  // =====================================================
  // LOAD SYSTEM INVENTORY
  // =====================================================
  /**
   * Retrieve all generic medicines documented in system parameters.
   */
  loadAllGenerics(): void {
    this.errorMessage = '';
    this.genericService.getAll().subscribe({
      next: (data) => {
        this.genericMedicines = data || [];
        this.cdr.markForCheck(); // Trigger precise change detection cycle check
      },
      error: (err) => {
        this.handleError('Failed to fetch generic medicines indices', err);
      }
    });
  }

  // =====================================================
  // FILTER SELECTION STRATEGIES
  // =====================================================
  /**
   * Perform exact generic name lookup matching backend controller route.
   */
  onSearch(): void {
    const query = this.searchQuery.trim();

    if (!query) {
      this.loadAllGenerics();
      return;
    }

    this.errorMessage = '';
    
    // explicit টাইপ কাস্টিং (any) ব্যবহার করা হলো যাতে কম্পাইলার এরর না দেয়
    (this.genericService as any).getByName(query).subscribe({
      next: (data: any) => {
        // ফ্লেক্সিবল চেক: ব্যাকএন্ড যদি ডাইরেক্ট অ্যারে পাঠায়, তবে সেটাই বসবে।
        // আর যদি সিঙ্গেল অবজেক্ট পাঠায়, তবে সেটিকে অ্যারেতে রুপান্তর করবে।
        if (Array.isArray(data)) {
          this.genericMedicines = data;
        } else {
          this.genericMedicines = data ? [data] : [];
        }
        
        this.cdr.markForCheck();
      },
      error: (err: any) => {
        this.handleError('Generic name search query match failed', err);
      }
    });
  }

  // =====================================================
  // WIPE RECORD TRANSACTIONS
  // =====================================================
  /**
   * Drops specific generic configurations permanently using database primary identifiers.
   */
  deleteGeneric(id: number): void {
    if (confirm('Are you completely sure you want to drop this generic medicine record?')) {
      this.genericService.delete(id).subscribe({
        next: () => {
          this.loadAllGenerics(); // Reload grid matrix arrays smoothly
        },
        error: (err) => {
          this.handleError('File removal command rejected by server rules', err);
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