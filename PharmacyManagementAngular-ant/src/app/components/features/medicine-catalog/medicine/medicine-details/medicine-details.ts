import { ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MedicineService } from '../../../../../services/medicine.service';
import { GenericMedicineService } from '../../../../../services/generic-medicine.service';
import { 
  MedicineModelResponse, 
  MedicineModelRequest,
  DosageForm,
  DrugSchedule,
  StorageCondition,
  UnitOfMeasure
} from '../../../../../models/medicine.model';

@Component({
  selector: 'app-medicine-details',
  imports: [CommonModule, FormsModule],
  templateUrl: './medicine-details.html',
  styleUrl: './medicine-details.css'
})
export class MedicineDetailsComponent implements OnInit {

  @ViewChild('medicineForm') medicineForm!: NgForm;
  @ViewChild('fileInput') fileInputRef!: ElementRef<HTMLInputElement>;

  // =====================================================
  // COMBINED LAYOUT MATRICES & OBJECT MODES STATE
  // =====================================================
  /** Master directory container listing array */
  medicineList: MedicineModelResponse[] = [];
  genericMedicines: any[] = [];

  // Enums values for binding input element iterations
  dosageForms = Object.values(DosageForm);
  drugSchedules = Object.values(DrugSchedule);
  unitOfMeasures = Object.values(UnitOfMeasure);
  storageConditions = Object.values(StorageCondition);

  medicineId!: number;
  medicineDetails: MedicineModelResponse | null = null;
  errorMessage = '';
  submitted = false;

  /** Controls layout rendering switch parameters (View mode vs Multipart form editor) */
  isEditMode = false;
  
  // Modifiable payload model and binary storage
  editData!: MedicineModelRequest;
  selectedFile: File | null = null;
  imagePreview: any = null;

  constructor(
    private medicineService: MedicineService,
    private genericMedicineService: GenericMedicineService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    // 1. Core catalog initializations
    this.loadAllMedicinesCatalog();
    this.loadGenericMedicinesDropdown();

    // 2. Intercept and continuous capture parameterized shifts inside route URLs
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.medicineId = Number(idParam);
        this.fetchIsolatedProfileDetails(this.medicineId);
      }
    });
  }

  // =====================================================
  // CATALOG DEPENDENCIES EXTRACTIONS
  // =====================================================
  loadAllMedicinesCatalog(): void {
    this.medicineService.getAllMedicines().subscribe({
      next: (data) => {
        this.medicineList = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Catalog inventory routing stream crashed', err)
    });
  }

  loadGenericMedicinesDropdown(): void {
    this.genericMedicineService.getAll().subscribe({
      next: (data) => {
        this.genericMedicines = data || [];
        this.cdr.markForCheck();
      }
    });
  }

  // =====================================================
  // GRANULAR DATA LINE RESOLUTION
  // =====================================================
  fetchIsolatedProfileDetails(id: number): void {
    this.errorMessage = '';
    this.isEditMode = false; // Collapse form input nodes safely if target selection context shifts
    this.selectedFile = null;
    this.imagePreview = null;
    
    this.medicineService.getMedicineById(id).subscribe({
      next: (data) => {
        this.medicineDetails = data;
        this.medicineId = id;
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Failed to retrieve targeted core medication properties', err)
    });
  }

  // =====================================================
  // IMAGE / MULTIPART INTERACTION TRICKGERS
  // =====================================================
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

  // =====================================================
  // INLINE FORMS LAYOUT SWITCHERS
  // =====================================================
  enableEditMode(): void {
    if (!this.medicineDetails) return;

    this.isEditMode = true;
    this.submitted = false;
    this.selectedFile = null;
    this.imagePreview = this.medicineDetails.image ? this.medicineDetails.image : null;

    // Direct mapping configuration response properties securely onto requests target structure
    this.editData = {
      id: this.medicineDetails.id,
      medicineCode: this.medicineDetails.medicineCode,
      brandName: this.medicineDetails.brandName,
      genericMedicineId: this.medicineDetails.genericMedicineId,
      manufacturer: this.medicineDetails.manufacturer,
      dosageForm: this.medicineDetails.dosageForm,
      strength: this.medicineDetails.strength,
      unitOfMeasure: this.medicineDetails.unitOfMeasure,
      unitsPerPack: this.medicineDetails.unitsPerPack,
      drugSchedule: this.medicineDetails.drugSchedule,
      storageCondition: this.medicineDetails.storageCondition,
      reorderLevel: this.medicineDetails.reorderLevel,
      reorderQuantity: this.medicineDetails.reorderQuantity,
      defaultPurchasePrice: this.medicineDetails.defaultPurchasePrice,
      defaultSellingPrice: this.medicineDetails.defaultSellingPrice,
      vatPercentage: this.medicineDetails.vatPercentage,
      description: this.medicineDetails.description,
      isActive: this.medicineDetails.isActive
    };
    
    this.cdr.markForCheck();
  }

  cancelEdit(): void {
    this.isEditMode = false;
    this.submitted = false;
    this.errorMessage = '';
    this.selectedFile = null;
    this.imagePreview = null;

    if (this.medicineForm) {
      this.medicineForm.resetForm();
    }
    this.cdr.markForCheck();
  }

  // =====================================================
  // SAVE / PATCH TRANSACTION LIFE CYCLES
  // =====================================================
  updateMedicine(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.medicineForm.invalid) {
      return;
    }

    this.medicineService
      .updateMedicine(this.medicineId, this.editData, this.selectedFile || undefined)
      .subscribe({
        next: (res) => {
          alert('Medicine Profile Configuration Patched Successfully');
          this.isEditMode = false;
          this.loadAllMedicinesCatalog(); // Refresh indices catalog logs array
          this.fetchIsolatedProfileDetails(this.medicineId); // Reload fresh view layouts
        },
        error: (err) => this.interceptError('Update operations rejected by endpoint rules validation', err)
      });
  }

  // =====================================================
  // DESTRUCTION METHODS DROPS
  // =====================================================
  deleteMedicine(idToDrop: number): void {
    if (confirm('Are you completely sure you want to drop this medicine from central records reference tracking?')) {
      this.medicineService.deleteMedicine(idToDrop).subscribe({
        next: () => {
          alert('Medicine Product Profile Purged');
          this.loadAllMedicinesCatalog(); // Sync tracking grids array structure instantly
          
          // Clean active focused displays if target view profile was destroyed
          if (this.medicineId === idToDrop) {
            this.medicineDetails = null;
            this.isEditMode = false;
            this.router.navigate(['/medicines/details']); // Safely route state cleanup tracking parameters
          }
          this.cdr.markForCheck();
        },
        error: (err) => this.interceptError('Purge pipeline request rejected by constraints validation', err)
      });
    }
  }

  // =====================================================
  // SHARED CONTEXT RUNTIME LOGGER EXCEPTIONS
  // =====================================================
  private interceptError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}
