import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GenericMedicineModel } from '../../../../models/generic-medicine.model';
import { MedicineCategoryModel } from '../../../../models/medicine-category.model';
import { GenericMedicineService } from '../../../../services/generic-medicine.service';
import { MedicineCategoryService } from '../../../../services/medicine-category.service';


@Component({
  selector: 'app-generic-medicine',
  imports: [CommonModule, FormsModule],
  templateUrl: './generic-medicine.html',
  styleUrl: './generic-medicine.css',
})
export class GenericMedicine implements OnInit {

  genericMedicines: GenericMedicineModel[] = [];
  categories: MedicineCategoryModel[] = [];

  genericMedicine: GenericMedicineModel = {
    genericName: '',
    categoryId: undefined,
    description: '',
    indication: '',
    sideEffects: '',
    contraindications: '',
    isActive: true
  };

  isEdit = false;

  constructor(
    private service: GenericMedicineService,
    private categoryService: MedicineCategoryService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.loadGenericMedicines();
    this.loadCategories();
  }

  loadGenericMedicines() {
    this.service.getAllGenericMedicines()
      .subscribe({
        next: (data) => {
          this.genericMedicines = data;
          this.cdr.markForCheck();
          console.log(data);
        },
        error: (err) => console.error('Error fetching generic medicines:', err)
      });
  }

  loadCategories() {
    this.categoryService.getAllCategories()
      .subscribe({
        next: (data) => {
          this.categories = data;
          this.cdr.markForCheck();
        },
        error: (err) => console.error('Error fetching categories for dropdown:', err)
      });
  }

  save() {
    if (this.isEdit) {
      this.service.updateGenericMedicine(this.genericMedicine.id!, this.genericMedicine)
        .subscribe({
          next: () => {
            alert("Updated Successfully");
            this.reset();
            this.loadGenericMedicines();
          },
          error: (err) => console.error('Error updating generic medicine:', err)
        });
    } else {
      this.service.createGenericMedicine(this.genericMedicine)
        .subscribe({
          next: () => {
            alert("Saved Successfully");
            this.reset();
            this.loadGenericMedicines();
          },
          error: (err) => console.error('Error creating generic medicine:', err)
        });
    }
  }

  edit(gm: GenericMedicineModel) {
    this.genericMedicine = { ...gm };
    this.isEdit = true;
  }

  delete(id: number) {
    if (confirm("Delete this generic medicine profile?")) {
      this.service.deleteGenericMedicine(id)
        .subscribe({
          next: () => {
            alert("Deleted");
            this.loadGenericMedicines();
          },
          error: (err) => console.error('Error deleting generic medicine:', err)
        });
    }
  }

  reset() {
    this.genericMedicine = {
      genericName: '',
      categoryId: undefined,
      description: '',
      indication: '',
      sideEffects: '',
      contraindications: '',
      isActive: true
    };
    this.isEdit = false;
  }
}
