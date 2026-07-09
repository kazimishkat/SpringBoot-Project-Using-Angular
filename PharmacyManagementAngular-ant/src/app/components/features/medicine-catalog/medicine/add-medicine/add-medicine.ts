import { ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { 
  MedicineModelRequest, 
  DosageForm, 
  DrugSchedule, 
  StorageCondition, 
  UnitOfMeasure 
} from '../../../../../models/medicine.model';
import { MedicineService } from '../../../../../services/medicine.service';
import { GenericMedicineService } from '../../../../../services/generic-medicine.service';

@Component({
  selector: 'app-add-medicine',
  imports: [FormsModule, CommonModule],
  templateUrl: './add-medicine.html',
  styleUrl: './add-medicine.css',
})
export class AddMedicine implements OnInit {

  @ViewChild('medicineForm') medicineForm!: NgForm;
  @ViewChild('fileInput') fileInputRef!: ElementRef<HTMLInputElement>;

  // =========================
  // ENUM DROPDOWN DATA
  // =========================
  dosageForms = Object.values(DosageForm);
  drugSchedules = Object.values(DrugSchedule);
  unitOfMeasures = Object.values(UnitOfMeasure);
  storageConditions = Object.values(StorageCondition);

  // =========================
  // DEPENDENT DROPDOWN DATA
  // =========================
  /** List of generic categories available */
  genericMedicines: any[] = [];

  // =========================
  // FORM & FILE STATE
  // =========================
  medicine: MedicineModelRequest = {
    medicineCode: '',
    brandName: '',
    genericMedicineId: 0,
    manufacturer: '',
    dosageForm: undefined,
    strength: '',
    unitOfMeasure: undefined,
    unitsPerPack: undefined,
    drugSchedule: undefined,
    storageCondition: undefined,
    reorderLevel: undefined,
    reorderQuantity: undefined,
    defaultPurchasePrice: undefined,
    defaultSellingPrice: undefined,
    description: '',
    isActive: true
  };

  selectedFile: File | null = null;
  imagePreview: any = null;
  submitted = false;

  constructor(
    private medicineService: MedicineService,
    private genericMedicineService: GenericMedicineService, // Injecting generic service to populate generic options
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadGenericMedicines();
  }

  // =====================================================
  // LOAD GENERIC MEDICINES LIST
  // =====================================================
  /**
   * Fetch all active generic names/categories from backend.
   */
  loadGenericMedicines(): void {
    this.genericMedicineService.getAll()
    .subscribe(data => {
        this.genericMedicines = data;
        
        // Force UI refresh if needed
        this.cdr.markForCheck();
        console.log('Generics Loaded:', data);
      });
  }

  // =====================================================
  // IMAGE/FILE HANDLING
  // =====================================================
  /**
   * Handle image file selection and generate view preview.
   */
  onFileSelected(event: any): void {
    const file = event.target.files[0];

    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();

      reader.onload = () => {
        this.imagePreview = reader.result;
        this.cdr.markForCheck();
      };

      reader.readAsDataURL(file);
    }
  }

  /**
   * Removes selected preview and clears original input node value.
   */
  removeSelectedFile(fileInput: HTMLInputElement): void {
    this.selectedFile = null;
    this.imagePreview = null;
    fileInput.value = '';
  }

  // =====================================================
  // SAVE OPERATION
  // =====================================================
  /**
   * Validate and submit full Multipart payload to the Service Layer.
   */
  saveMedicine(): void {
    this.submitted = true;

    // Direct structural validation check before firing API
    if (this.medicineForm.invalid) {
      return;
    }

    this.medicineService
      .createMedicine(this.medicine, this.selectedFile!)
      .subscribe({
        next: (res) => {
          alert('Medicine Saved Successfully');
          this.resetForm();
        },
        error: (err) => {
          console.error('Failed to create product entry', err);
        }
      });
  }

  // =====================================================
  // FORM RESET ROUTINE
  // =====================================================
  /**
   * Resets the medicine form completely:
   * - clears the model state values
   * - clears the selected image payload + visual template preview
   * - resets Angular state parameters (touched/dirty/submitted triggers)
   */
  resetForm(): void {
    this.medicine = {
      medicineCode: '',
      brandName: '',
      genericMedicineId: 0,
      manufacturer: '',
      dosageForm: undefined,
      strength: '',
      unitOfMeasure: undefined,
      unitsPerPack: undefined,
      drugSchedule: undefined,
      storageCondition: undefined,
      reorderLevel: undefined,
      reorderQuantity: undefined,
      defaultPurchasePrice: undefined,
      defaultSellingPrice: undefined,
      description: '',
      isActive: true
    };

    this.submitted = false;
    this.selectedFile = null;
    this.imagePreview = null;

    // Reset native file input template reference node explicitly
    if (this.fileInputRef) {
      this.fileInputRef.nativeElement.value = '';
    }

    // Reset Angular's form state control classes entirely
    if (this.medicineForm) {
      this.medicineForm.resetForm();
    }

    this.cdr.markForCheck();
  }
}