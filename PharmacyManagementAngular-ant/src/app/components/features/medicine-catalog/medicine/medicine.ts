import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
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
          console.log("Loaded Medicines:", data);
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
    // সাবমিট করার জন্য অবজেক্টের একটি ক্লোন তৈরি করছি
    const medicineDataToSubmit = { ...this.medicine };
    
    if (!this.isEdit) {
      // নতুন ওষুধ তৈরি করার সময় id এবং ইমেজ পাথ ক্লিন করে পাঠানো ভালো
      delete medicineDataToSubmit.id;
      delete medicineDataToSubmit.image;
    }

    if (this.isEdit) {
      this.service.updateMedicine(this.medicine.id!, medicineDataToSubmit, this.selectedFile)
        .subscribe({
          next: () => {
            alert("Updated Successfully");
            this.reset();
            this.loadMedicines();
          },
          error: (err) => console.error('Error updating medicine:', err)
        });
    } else {
      this.service.createMedicine(medicineDataToSubmit, this.selectedFile)
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
    this.selectedFile = undefined; 
    this.isEdit = true;
  }

  delete(id: number) {
    if (confirm("Delete this medicine?")) {
      this.service.deleteMedicine(id)
        .subscribe({
          next: (res) => {
            alert(res || "Deleted");
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

    // HTML-এর ফাইল ইনপুট ফিল্ডটি ক্লিন করার জন্য (নিচের ২ নম্বর স্টেপ দেখুন)
    const fileInput = document.getElementById('imageFile') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
  }
}