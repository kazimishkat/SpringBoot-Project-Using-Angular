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
  selector: 'app-medicine-add',
  imports: [FormsModule, CommonModule],
  templateUrl: './medicine-add.html',
  styleUrl: './medicine-add.css',
})
export class MedicineAdd implements OnInit {

  @ViewChild('medicineForm') medicineForm!: NgForm;
  @ViewChild('fileInput') fileInputRef!: ElementRef<HTMLInputElement>;

  // =====================================================
  // ENUM DROPDOWNS & DEPENDENCY SELECTION DATA
  // =====================================================
  dosageForms = Object.values(DosageForm);
  drugSchedules = Object.values(DrugSchedule);
  unitOfMeasures = Object.values(UnitOfMeasure);
  storageConditions = Object.values(StorageCondition);
  
  genericMedicines: any[] = []; // Used to populate the Generic dropdown

  // =====================================================
  // FORM & MULTIPART FILE STATE
  // =====================================================
  medicine: MedicineModelRequest = this.initFormStructure();
  selectedFile: File | null = null;
  imagePreview: any = null;
  submitted = false;
  errorMessage = '';

  constructor(
    private medicineService: MedicineService,
    private genericMedicineService: GenericMedicineService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadGenericMedicinesDropdown();
  }

  /** Fetch available generic medicines from backend for selection mapping */
  loadGenericMedicinesDropdown(): void {
    this.genericMedicineService.getAll().subscribe({
      next: (data) => {
        this.genericMedicines = data || [];
        this.cdr.markForCheck();
      }
    });
  }

  // =====================================================
  // FILE BINDING CONTROLLERS
  // =====================================================
  /** Capture selected image binary block and generate raw reader preview */
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

  /** Removes selected file context and purges input HTML nodes value mapping */
  removeSelectedFile(fileInput: HTMLInputElement): void {
    this.selectedFile = null;
    this.imagePreview = null;
    fileInput.value = '';
  }

  // Helper method to setup baseline data form structure
  private initFormStructure(): MedicineModelRequest {
    return {
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
      vatPercentage: undefined,
      description: '',
      isActive: true
    };
  }

  // =====================================================
  // TRANSACTIONS SAVE ROUTINES
  // =====================================================
  /**
   * Validate parameters and post complete Multipart payload configurations.
   */
  saveMedicine(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.medicineForm.invalid) {
      return;
    }

    this.medicineService
      .createMedicine(this.medicine, this.selectedFile || undefined)
      .subscribe({
        next: (res) => {
          alert('Medicine Profile Saved Successfully');
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
   * Clears component entity definitions and updates template dirty tracking parameters.
   */
  resetForm(): void {
    this.medicine = this.initFormStructure();
    this.submitted = false;
    this.selectedFile = null;
    this.imagePreview = null;
    this.errorMessage = '';

    if (this.fileInputRef) {
      this.fileInputRef.nativeElement.value = '';
    }

    if (this.medicineForm) {
      this.medicineForm.resetForm();
    }
    this.cdr.markForCheck();
  }
}
