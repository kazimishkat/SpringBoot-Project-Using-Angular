import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

// import { MedicineModel, DosageForm, DrugSchedule, StorageCondition, UnitOfMeasure } from '../../../models/medicine.model';
import { MedicineService } from '../../../../services/medicine.service';
import { DosageForm, DrugSchedule, MedicineModel, StorageCondition, UnitOfMeasure } from '../../../../models/medicine.model';

@Component({
  selector: 'app-medicine',
  imports: [CommonModule, FormsModule],
  templateUrl: './medicine.html',
  styleUrl: './medicine.css',
})
export class Medicine implements OnInit {

  medicines: MedicineModel[] = [];
  selectedFile: File | undefined = undefined;

  // Expose enums to the HTML template for dropdown (*ngFor) iterations
  dosageForms = Object.values(DosageForm);
  drugSchedules = Object.values(DrugSchedule);
  storageConditions = Object.values(StorageCondition);
  unitOfMeasures = Object.values(UnitOfMeasure);

  medicine: MedicineModel = {
    medicineCode: '',
    brandName: '',
    genericMedicineId: 0,
    manufacturer: '',
    dosageForm: '',
    strength: '',
    unitOfMeasure: '',
    unitsPerPack: undefined,
    drugSchedule: '',
    storageCondition: '',
    reorderLevel: undefined,
    reorderQuantity: undefined,
    defaultPurchasePrice: undefined,
    defaultSellingPrice: undefined,
    vatPercentage: undefined,
    description: '',
    image: '',
    isActive: true
  };

  isEdit = false;

  constructor(
    private service: MedicineService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.loadMedicines();
  }

  loadMedicines() {
    this.service.getAllMedicines()
      .subscribe({
        next: (data) => {
          this.medicines = data;
          this.cdr.markForCheck();
          console.log(data);
        },
        error: (err) => console.error('Error fetching medicines:', err)
      });
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }

  save() {
    if (this.isEdit) {
      this.service.updateMedicine(this.medicine.id!, this.medicine, this.selectedFile)
        .subscribe({
          next: () => {
            alert("Updated Successfully");
            this.reset();
            this.loadMedicines();
          },
          error: (err) => console.error('Error updating medicine:', err)
        });
    } else {
      this.service.createMedicine(this.medicine, this.selectedFile)
        .subscribe({
          next: () => {
            alert("Saved Successfully");
            this.reset();
            this.loadMedicines();
          },
          error: (err) => console.error('Error creating medicine:', err)
        });
    }
  }

  edit(m: MedicineModel) {
    this.medicine = { ...m };
    this.selectedFile = undefined; // Reset file input buffer on record switch
    this.isEdit = true;
  }

  delete(id: number) {
    if (confirm("Delete this medicine?")) {
      this.service.deleteMedicine(id)
        .subscribe({
          next: () => {
            alert("Deleted");
            this.loadMedicines();
          },
          error: (err) => console.error('Error deleting medicine:', err)
        });
    }
  }

  reset() {
    this.medicine = {
      medicineCode: '',
      brandName: '',
      genericMedicineId: 0,
      manufacturer: '',
      dosageForm: '',
      strength: '',
      unitOfMeasure: '',
      unitsPerPack: undefined,
      drugSchedule: '',
      storageCondition: '',
      reorderLevel: undefined,
      reorderQuantity: undefined,
      defaultPurchasePrice: undefined,
      defaultSellingPrice: undefined,
      vatPercentage: undefined,
      description: '',
      image: '',
      isActive: true
    };
    this.selectedFile = undefined;
    this.isEdit = false;
  }
}