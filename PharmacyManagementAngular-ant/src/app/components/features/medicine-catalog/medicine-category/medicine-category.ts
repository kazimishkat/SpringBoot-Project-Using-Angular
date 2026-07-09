import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { NgForm, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MedicineCategoryRequest, MedicineCategoryResponse } from '../../../../models/medicine-category.model';
import { MedicineCategoryService } from '../../../../services/medicine-category.service';

@Component({
  selector: 'app-medicine-category',
  imports: [FormsModule, CommonModule],
  templateUrl: './medicine-category.html',
  styleUrl: './medicine-category.css'
})
export class MedicineCategoryComponent implements OnInit {

  @ViewChild('categoryForm') categoryForm!: NgForm;

  // State arrays
  categories: MedicineCategoryResponse[] = [];
  
  // Form handling properties
  searchNameQuery: string = '';
  errorMessage: string = '';
  isEditMode: boolean = false;
  submitted: boolean = false;

  // Request binding model
  categoryData!: MedicineCategoryRequest;

  constructor(
    private categoryService: MedicineCategoryService,
    private cdr: ChangeDetectorRef // Injected ChangeDetectorRef for explicit UI tracking
  ) { }

  ngOnInit(): void {
    this.resetForm();
    this.loadAllCategories();
  }

  // Load all medicine categories
  loadAllCategories(): void {
    this.categoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categories = data || [];
        // Force UI view hierarchy refresh
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Failed to load categories', err)
    });
  }

  // Perform category name lookup matching backend route
  onSearchByName(): void {
    if (!this.searchNameQuery.trim()) {
      this.loadAllCategories();
      return;
    }
    this.categoryService.getCategoryByName(this.searchNameQuery).subscribe({
      next: (data) => {
        this.categories = data ? [data] : [];
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Search failed', err)
    });
  }

  // Submit standard JSON payload (Insert or Update)
  onSubmit(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.categoryForm.invalid) {
      return;
    }

    if (this.isEditMode && this.categoryData.id) {
      // Execute PUT updates
      this.categoryService.updateCategory(this.categoryData.id, this.categoryData).subscribe({
        next: () => {
          this.loadAllCategories();
          this.resetForm();
        },
        error: (err) => this.handleError('Update transaction aborted', err)
      });
    } else {
      // Execute POST insertions
      this.categoryService.createCategory(this.categoryData).subscribe({
        next: () => {
          this.loadAllCategories();
          this.resetForm();
        },
        error: (err) => this.handleError('Creation transaction aborted', err)
      });
    }
  }

  // Trigger edit status and populate structure
  editCategory(item: MedicineCategoryResponse): void {
    this.isEditMode = true;
    this.categoryData = { ...item };
    this.cdr.markForCheck();
  }

  // Delete category confirmation flow
  deleteCategory(id: number): void {
    if (confirm('Are you sure you want to delete this category record?')) {
      this.categoryService.deleteCategory(id).subscribe({
        next: () => this.loadAllCategories(),
        error: (err) => this.handleError('Deletion request rejected', err)
      });
    }
  }

  // Purge component states and recover native forms
  resetForm(): void {
    this.categoryData = {
      name: '',
      description: '',
      isActive: true
    };
    
    this.submitted = false;
    this.isEditMode = false;
    this.errorMessage = '';

    if (this.categoryForm) {
      this.categoryForm.resetForm();
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