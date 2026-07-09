import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { NgForm, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { GenericMedicineRequest, GenericMedicineResponse } from '../../../../models/generic-medicine.model';
import { GenericMedicineService } from '../../../../services/generic-medicine.service';
import { MedicineCategoryService } from '../../../../services/medicine-category.service';


@Component({
  selector: 'app-generic-medicine',
  imports: [FormsModule, CommonModule],
  templateUrl: './generic-medicine.html',
  styleUrl: './generic-medicine.css'
})
export class GenericMedicineComponent implements OnInit {

  @ViewChild('genericForm') genericForm!: NgForm;

  // State arrays
  genericMedicines: GenericMedicineResponse[] = [];
  categories: any[] = []; // Used to populate the Category Dropdown
  
  // Form handling properties
  searchNameQuery: string = '';
  errorMessage: string = '';
  isEditMode: boolean = false;
  submitted: boolean = false;

  // Request binding model
  genericData!: GenericMedicineRequest;

  constructor(
    private genericService: GenericMedicineService,
    private categoryService: MedicineCategoryService,
    private cdr: ChangeDetectorRef // Injected ChangeDetectorRef for explicit UI tracking
  ) { }

  ngOnInit(): void {
    this.resetForm();
    this.loadAllGenerics();
    this.loadCategories();
  }

  // Load all generic medicine definitions
  loadAllGenerics(): void {
    this.genericService.getAll().subscribe({
      next: (data) => {
        this.genericMedicines = data || [];
        // Force UI view hierarchy refresh
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Failed to load generic medicines', err)
    });
  }

  // Fetch parent drug categories for selection dropdown
  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categories = data || [],
        // Force UI view hierarchy refresh
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Failed to load categories', err)
    });
  }

  // Perform absolute name lookup matching backend route
  onSearchByName(): void {
    if (!this.searchNameQuery.trim()) {
      this.loadAllGenerics();
      return;
    }
    this.genericService.getByName(this.searchNameQuery).subscribe({
      next: (data) => {
        this.genericMedicines = data ? [data] : [];
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Search failed', err)
    });
  }

  // Submit standard JSON payload (Insert or Update)
  onSubmit(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.genericForm.invalid) {
      return;
    }

    if (this.isEditMode && this.genericData.id) {
      // Execute PUT updates
      this.genericService.update(this.genericData.id, this.genericData).subscribe({
        next: () => {
          this.loadAllGenerics();
          this.resetForm();
        },
        error: (err) => this.handleError('Update transaction aborted', err)
      });
    } else {
      // Execute POST insertions
      this.genericService.create(this.genericData).subscribe({
        next: () => {
          this.loadAllGenerics();
          this.resetForm();
        },
        error: (err) => this.handleError('Creation transaction aborted', err)
      });
    }
  }

  // Trigger edit status and populate structure
  editGeneric(item: GenericMedicineResponse): void {
    this.isEditMode = true;
    this.genericData = { ...item };
    this.cdr.markForCheck();
  }

  // Delete entity confirmation flow
  deleteGeneric(id: number): void {
    if (confirm('Are you sure you want to delete this generic record?')) {
      this.genericService.delete(id).subscribe({
        next: () => this.loadAllGenerics(),
        error: (err) => this.handleError('Deletion request rejected', err)
      });
    }
  }

  // Purge component states and recover native forms
  resetForm(): void {
    this.genericData = {
      genericName: '',
      categoryId: undefined,
      description: '',
      indication: '',
      sideEffects: '',
      contraindications: '',
      isActive: true
    };
    
    this.submitted = false;
    this.isEditMode = false;
    this.errorMessage = '';

    if (this.genericForm) {
      this.genericForm.resetForm();
    }

    // Explicitly update template binding after complete form purge
    this.cdr.markForCheck();
  }

  // Universal mapping error interceptor
  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}