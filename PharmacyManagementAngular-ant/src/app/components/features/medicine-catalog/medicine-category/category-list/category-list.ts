import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterModule } from '@angular/router';
import { MedicineCategoryRequest, MedicineCategoryResponse } from '../../../../../models/medicine-category.model';
import { MedicineCategoryService } from '../../../../../services/medicine-category.service';

@Component({
  selector: 'app-category-list',
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './category-list.html',
  styleUrl: './category-list.css'
})
export class CategoryListComponent implements OnInit {

  @ViewChild('inlineEditForm') inlineEditForm!: NgForm;

  // =====================================================
  // MASTER ARRAY VIEW & FILTER CONFIGURATIONS
  // =====================================================
  categories: MedicineCategoryResponse[] = [];
  searchQuery: string = '';
  errorMessage: string = '';
  submitted = false;

  // =====================================================
  // INLINE EDITING MANAGEMENT STATES
  // =====================================================
  /** Keeps track of which specific row ID is currently inside edit mode */
  editingCategoryId: number | null = null;

  /** Form binding request structure mapped during update operations */
  editData!: MedicineCategoryRequest;

  constructor(
    private categoryService: MedicineCategoryService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadAllCategories();
  }

  // =====================================================
  // LOAD SYSTEM INVENTORY
  // =====================================================
  /**
   * Retrieve all medicine categories documented in system parameters.
   */
  loadAllCategories(): void {
    this.errorMessage = '';
    this.categoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categories = data || [];
        this.cdr.markForCheck(); // Trigger precise change detection cycle check
      },
      error: (err) => {
        this.handleError('Failed to fetch medicine categories indexes', err);
      }
    });
  }

  // =====================================================
  // FILTER SELECTION STRATEGIES
  // =====================================================
  /**
   * Perform exact category name lookup matching backend route.
   */
  onSearch(): void {
    const query = this.searchQuery.trim();

    if (!query) {
      this.loadAllCategories();
      return;
    }

    this.errorMessage = '';
    this.categoryService.getCategoryByName(query).subscribe({
      next: (data) => {
        // Since backend name lookup returns a single DTO, pack it inside an array for the grid
        this.categories = data ? [data] : [];
        this.cancelInlineEdit(); // Collapse any open forms during active search shifts
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.handleError('Category search query match failed', err);
      }
    });
  }

  // =====================================================
  // INLINE EDIT CONTROLLERS & TRIGGER HANDLERS
  // =====================================================
  /**
   * Puts a specific grid row layout into active input field editing mode.
   */
  enableInlineEdit(item: MedicineCategoryResponse): void {
    this.editingCategoryId = item.id;
    this.submitted = false;
    this.errorMessage = '';

    // Map response domain parameters safely into request payload bindings
    this.editData = {
      id: item.id,
      name: item.name,
      description: item.description,
      isActive: item.isActive
    };
    this.cdr.markForCheck();
  }

  /**
   * Commits the modified inline parameters configuration back to server endpoints.
   */
  updateCategory(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.inlineEditForm && this.inlineEditForm.invalid) {
      return;
    }

    if (this.editingCategoryId) {
      this.categoryService
        .updateCategory(this.editingCategoryId, this.editData)
        .subscribe({
          next: (res) => {
            alert('Category Configurations Patched Successfully');
            this.cancelInlineEdit();
            this.loadAllCategories(); // Hot refresh list records array seamlessly
          },
          error: (err) => {
            this.handleError('Mutation request rejected by server validation parameters', err);
          }
        });
    }
  }

  /**
   * Discards active modification changes and rolls layout nodes back to flat data blocks.
   */
  cancelInlineEdit(): void {
    this.editingCategoryId = null;
    this.submitted = false;
    this.errorMessage = '';
    
    if (this.inlineEditForm) {
      this.inlineEditForm.resetForm();
    }
    this.cdr.markForCheck();
  }

  // =====================================================
  // WIPE RECORD TRANSACTIONS
  // =====================================================
  /**
   * Drops specific category configurations permanently using database primary identifiers.
   */
  deleteCategory(id: number): void {
    if (confirm('Are you completely sure you want to drop this medicine category?')) {
      this.categoryService.deleteCategory(id).subscribe({
        next: () => {
          // If the dropped category row was actively being edited, reset target trackers
          if (this.editingCategoryId === id) {
            this.cancelInlineEdit();
          }
          this.loadAllCategories(); // Reload grid matrix arrays smoothly
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