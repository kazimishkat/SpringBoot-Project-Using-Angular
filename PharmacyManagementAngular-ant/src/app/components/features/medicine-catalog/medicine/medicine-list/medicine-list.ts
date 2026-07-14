import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MedicineModelResponse } from '../../../../../models/medicine.model';
import { MedicineService } from '../../../../../services/medicine.service';


@Component({
  selector: 'app-medicine-list',
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './medicine-list.html',
  styleUrl: './medicine-list.css'
})
export class MedicineListComponent implements OnInit {

  // =====================================================
  // VIEWS DATA TARGETS & METRICS CONFIGURATIONS
  // =====================================================
  medicines: MedicineModelResponse[] = [];
  searchQuery: string = '';
  errorMessage: string = '';
  isLowStockView: boolean = false;

  constructor(
    private medicineService: MedicineService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadAllMedicines();
  }

  // =====================================================
  // LOAD SYSTEM DIRECTORY
  // =====================================================
  /**
   * Load complete recorded medicine catalogue rows from server backend.
   */
  loadAllMedicines(): void {
    this.errorMessage = '';
    this.isLowStockView = false;
    this.medicineService.getAllMedicines().subscribe({
      next: (data) => {
        this.medicines = data || [];
        this.cdr.markForCheck(); // Explicit grid layout state recalculation call
      },
      error: (err) => this.handleError('Failed to pull system medicine listings data', err)
    });
  }

  // =====================================================
  // FILTER STRATEGIES
  // =====================================================
  /**
   * Process operational input variables to trigger endpoint criteria searches.
   */
  onSearch(): void {
    const query = this.searchQuery.trim();

    if (!query) {
      this.loadAllMedicines();
      return;
    }

    this.errorMessage = '';
    
    // Evaluate if the structure matches a code string identifier vs general brand names text query
    if (query.toUpperCase().startsWith('MED') || query.length <= 5) {
      this.medicineService.getMedicineByCode(query).subscribe({
        next: (data) => {
          this.medicines = data ? [data] : [];
          this.cdr.markForCheck();
        },
        error: () => {
          // Fall back gracefully to textual name keyword lookup routes if unique key route fails
          this.searchByBrandNameFallback(query);
        }
      });
    } else {
      this.searchByBrandNameFallback(query);
    }
  }

  /** Helper method executing standard brand name lookup parameter maps */
  private searchByBrandNameFallback(query: string): void {
    this.medicineService.searchMedicinesByBrandName(query).subscribe({
      next: (data) => {
        this.medicines = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Brand name criteria evaluation sequence failed', err)
    });
  }

  /** Loads medicines whose on-hand counts fell beneath warning level configuration thresholds */
  toggleLowStockView(): void {
    this.errorMessage = '';
    this.isLowStockView = true;
    this.medicineService.getLowStockMedicines().subscribe({
      next: (data) => {
        this.medicines = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Failed to parse low stock inventory warnings', err)
    });
  }

  // =====================================================
  // PURGE TRANSACTIONS
  // =====================================================
  /**
   * Completely drop medicine tracking metrics records via database ID numbers.
   */
  deleteMedicine(id: number): void {
    if (confirm('Are you completely sure you want to delete this medicine product entry?')) {
      this.medicineService.deleteMedicine(id).subscribe({
        next: () => {
          // Reload dashboard indexes depending on the state of the active filter toggles
          if (this.isLowStockView) {
            this.toggleLowStockView();
          } else {
            this.loadAllMedicines();
          }
        },
        error: (err) => this.handleError('Disposal sequence rejected by constraint checks', err)
      });
    }
  }

  // =====================================================
  // SHAPED ERROR LOGGER ROUTING HANDLERS
  // =====================================================
  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}