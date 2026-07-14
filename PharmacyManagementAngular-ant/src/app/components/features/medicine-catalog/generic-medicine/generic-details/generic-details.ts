import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { GenericMedicineRequest, GenericMedicineResponse } from '../../../../../models/generic-medicine.model';
import { MedicineCategoryResponse } from '../../../../../models/medicine-category.model';
import { GenericMedicineService } from '../../../../../services/generic-medicine.service';
import { MedicineCategoryService } from '../../../../../services/medicine-category.service';


@Component({
  selector: 'app-generic-details',
  imports: [CommonModule, FormsModule],
  templateUrl: './generic-details.html',
  styleUrl: './generic-details.css'
})
export class GenericDetailsComponent implements OnInit {

  @ViewChild('genericForm') genericForm!: NgForm;

  // =====================================================
  // COMBINED DASHBOARD RENDERING STATES
  // =====================================================
  /** Master catalogue array loaded on index container section */
  masterGenericList: GenericMedicineResponse[] = [];
  categories: MedicineCategoryResponse[] = [];
  
  genericId!: number;
  genericDetails: GenericMedicineResponse | null = null;
  errorMessage = '';
  submitted = false;

  /** Controls layout status between flat sheet visualization vs editable forms */
  isEditMode = false;

  /** Mutation mapping object structure targets updating scripts */
  editData!: GenericMedicineRequest;

  constructor(
    private genericService: GenericMedicineService,
    private categoryService: MedicineCategoryService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    // 1. Initialise full catalog dashboard grid arrays
    this.loadAllGenericsCatalog();
    this.loadCategoriesDropdown();

    // 2. Continuous tracking stream for checking parameters shifts inside URLs
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.genericId = Number(idParam);
        this.fetchGranularDetailsPanel(this.genericId);
      }
    });
  }

  // =====================================================
  // MASTER CATALOG & DROPDOWN RECOVERY
  // =====================================================
  loadAllGenericsCatalog(): void {
    this.genericService.getAll().subscribe({
      next: (data) => {
        this.masterGenericList = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Catalog processing transaction crashed', err)
    });
  }

  loadCategoriesDropdown(): void {
    this.categoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categories = data || [];
        this.cdr.markForCheck();
      }
    });
  }

  // =====================================================
  // SINGLE PROFILE DETAILS EXTRACTION
  // =====================================================
  fetchGranularDetailsPanel(id: number): void {
    this.errorMessage = '';
    this.isEditMode = false; // Collapse form nodes if user switches rows mid-operation
    
    this.genericService.getById(id).subscribe({
      next: (data) => {
        this.genericDetails = data;
        this.genericId = id;
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Failed to capture isolated targets structural context', err)
    });
  }

  // =====================================================
  // TOGGLE & INLINE INJECTION HANDLERS
  // =====================================================
  enableEditMode(): void {
    if (!this.genericDetails) return;

    this.isEditMode = true;
    this.submitted = false;

    // Maps recorded details onto modifiable data models safely
    this.editData = {
      id: this.genericDetails.id,
      genericName: this.genericDetails.genericName,
      categoryId: this.genericDetails.categoryId,
      description: this.genericDetails.description,
      indication: this.genericDetails.indication,
      sideEffects: this.genericDetails.sideEffects,
      contraindications: this.genericDetails.contraindications,
      isActive: this.genericDetails.isActive
    };
    
    this.cdr.markForCheck();
  }

  cancelEdit(): void {
    this.isEditMode = false;
    this.submitted = false;
    this.errorMessage = '';

    if (this.genericForm) {
      this.genericForm.resetForm();
    }
    this.cdr.markForCheck();
  }

  // =====================================================
  // SAVE/PATCH TRANSACTION LOGICS
  // =====================================================
  updateGeneric(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.genericForm.invalid) {
      return;
    }

    this.genericService
      .update(this.genericId, this.editData)
      .subscribe({
        next: (res) => {
          alert('Generic Medicine Updated Successfully');
          this.isEditMode = false;
          this.loadAllGenericsCatalog(); // Hot refresh master listings arrays
          this.fetchGranularDetailsPanel(this.genericId); // Reload updated display data
        },
        error: (err) => this.interceptError('Update operations rejected by endpoint verification', err)
      });
  }

  // =====================================================
  // DESTRUCTION DISPOSAL ROUTINES
  // =====================================================
  deleteGeneric(idToDrop: number): void {
    if (confirm('Are you absolutely sure you want to permanently delete this generic medicine configuration?')) {
      this.genericService.delete(idToDrop).subscribe({
        next: () => {
          alert('Generic Medicine Record Purged');
          this.loadAllGenericsCatalog(); // Sync local catalog arrays immediately
          
          // Clear active detail panels if focused index was destroyed
          if (this.genericId === idToDrop) {
            this.genericDetails = null;
            this.isEditMode = false;
            this.router.navigate(['/generic-medicines/details']); // Flush route states safely
          }
          this.cdr.markForCheck();
        },
        error: (err) => this.interceptError('Purge requests halted', err)
      });
    }
  }

  // =====================================================
  // GLOBAL TRANSLATOR EXCEPTION WRAPPERS
  // =====================================================
  private interceptError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}