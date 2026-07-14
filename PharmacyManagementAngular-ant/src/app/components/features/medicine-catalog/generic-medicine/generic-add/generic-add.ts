import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MedicineCategoryResponse } from '../../../../../models/medicine-category.model';
import { GenericMedicineRequest } from '../../../../../models/generic-medicine.model';
import { GenericMedicineService } from '../../../../../services/generic-medicine.service';
import { MedicineCategoryService } from '../../../../../services/medicine-category.service';


@Component({
  selector: 'app-generic-add',
  imports: [FormsModule, CommonModule],
  templateUrl: './generic-add.html',
  styleUrl: './generic-add.css',
})
export class GenericAdd implements OnInit {

  @ViewChild('genericForm') genericForm!: NgForm;

  // =====================================================
  // DROPDOWN & DEPENDENCY SELECTION DATA
  // =====================================================
  categories: MedicineCategoryResponse[] = [];

  // =====================================================
  // FORM MUTATION & VALIDATION STATE
  // =====================================================
  generic: GenericMedicineRequest = this.initFormStructure();
  submitted = false;
  errorMessage = '';

  constructor(
    private genericService: GenericMedicineService,
    private categoryService: MedicineCategoryService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadCategoriesDropdown();
  }

  /** Load available parent medicine categories to populate the select element */
  loadCategoriesDropdown(): void {
    this.categoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categories = data || [];
        this.cdr.markForCheck();
      }
    });
  }

  // Helper method to setup pristine model fields structural schema
  private initFormStructure(): GenericMedicineRequest {
    return {
      genericName: '',
      categoryId: undefined,
      description: '',
      indication: '',
      sideEffects: '',
      contraindications: '',
      isActive: true
    };
  }

  // =====================================================
  // TRANSACTIONS SAVE ROUTINES
  // =====================================================
  /**
   * Validate bindings and dispatch new JSON payload to central service streams.
   */
  saveGeneric(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.genericForm.invalid) {
      return;
    }

    this.genericService
      .create(this.generic)
      .subscribe({
        next: (res) => {
          alert('Generic Medicine Saved Successfully');
          this.resetForm();
        },
        error: (err) => {
          console.error(err);
          this.errorMessage = `Creation transaction aborted: ${err.error || err.message || 'Server Exception'}`;
          this.cdr.markForCheck();
        }
      });
  }

  // =====================================================
  // FORM LIFE CYCLE CLEANER
  // =====================================================
  /**
   * Wipe tracking variables back to base blueprints and clear structural DOM classes.
   */
  resetForm(): void {
    this.generic = this.initFormStructure();
    this.submitted = false;
    this.errorMessage = '';

    if (this.genericForm) {
      this.genericForm.resetForm();
    }
    this.cdr.markForCheck();
  }
}
