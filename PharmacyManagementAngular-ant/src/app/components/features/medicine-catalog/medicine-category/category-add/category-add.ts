import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MedicineCategoryRequest } from '../../../../../models/medicine-category.model';
import { MedicineCategoryService } from '../../../../../services/medicine-category.service';


@Component({
  selector: 'app-category-add',
  imports: [FormsModule, CommonModule],
  templateUrl: './category-add.html',
  styleUrl: './category-add.css',
})
export class CategoryAdd implements OnInit {

  @ViewChild('categoryForm') categoryForm!: NgForm;

  // =====================================================
  // FORM MUTATION & VALIDATION STATE
  // =====================================================
  category: MedicineCategoryRequest = this.initFormStructure();
  submitted = false;
  errorMessage = '';

  constructor(
    private categoryService: MedicineCategoryService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void { }

  // Helper method to setup pristine model fields structural schema
  private initFormStructure(): MedicineCategoryRequest {
    return {
      name: '',
      description: '',
      isActive: true
    };
  }

  // =====================================================
  // TRANSACTIONS SAVE ROUTINES
  // =====================================================
  /**
   * Validate bindings and dispatch new JSON payload to central service streams.
   */
  saveCategory(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.categoryForm.invalid) {
      return;
    }

    this.categoryService
      .createCategory(this.category)
      .subscribe({
        next: (res) => {
          alert('Medicine Category Saved Successfully');
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
    this.category = this.initFormStructure();
    this.submitted = false;
    this.errorMessage = '';

    if (this.categoryForm) {
      this.categoryForm.resetForm();
    }
    this.cdr.markForCheck();
  }
}