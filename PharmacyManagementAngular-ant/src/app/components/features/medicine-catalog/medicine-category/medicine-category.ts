import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MedicineCategoryModel } from '../../../../models/medicine-category.model';
import { MedicineCategoryService } from '../../../../services/medicine-category.service';


@Component({
  selector: 'app-medicine-category',
  imports: [CommonModule, FormsModule],
  templateUrl: './medicine-category.html',
  styleUrl: './medicine-category.css',
})
export class MedicineCategory implements OnInit {

  categories: MedicineCategoryModel[] = [];

  category: MedicineCategoryModel = {
    name: '',
    description: '',
    isActive: true
  };

  isEdit = false;

  constructor(
    private service: MedicineCategoryService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.loadCategories();
  }

  loadCategories() {
    this.service.getAllCategories()
      .subscribe({
        next: (data) => {
          this.categories = data;
          this.cdr.markForCheck();
          console.log(data);
        },
        error: (err) => console.error('Error fetching medicine categories:', err)
      });
  }

  save() {
    if (this.isEdit) {
      this.service.updateCategory(this.category.id!, this.category)
        .subscribe({
          next: () => {
            alert("Updated Successfully");
            this.reset();
            this.loadCategories();
          },
          error: (err) => console.error('Error updating category:', err)
        });
    } else {
      this.service.createCategory(this.category)
        .subscribe({
          next: () => {
            alert("Saved Successfully");
            this.reset();
            this.loadCategories();
          },
          error: (err) => console.error('Error creating category:', err)
        });
    }
  }

  edit(c: MedicineCategoryModel) {
    this.category = { ...c };
    this.isEdit = true;
  }

  delete(id: number) {
    if (confirm("Delete this medicine category?")) {
      this.service.deleteCategory(id)
        .subscribe({
          next: () => {
            alert("Deleted");
            this.loadCategories();
          },
          error: (err) => console.error('Error deleting category:', err)
        });
    }
  }

  reset() {
    this.category = {
      name: '',
      description: '',
      isActive: true
    };
    this.isEdit = false;
  }
}