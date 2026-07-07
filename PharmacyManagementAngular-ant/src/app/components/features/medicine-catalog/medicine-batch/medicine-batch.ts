import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { MedicineBatchModel } from "../../../../models/medicine-batch.model";
import { MedicineBatchService } from "../../../../services/medicine-batch.service";
import { MedicineModel } from "../../../../models/medicine.model";
import { SupplierModel } from "../../../../models/supplier.model";
import { MedicineService } from "../../../../services/medicine.service";
import { SupplierService } from "../../../../services/supplier.service";

@Component({
  selector: 'app-medicine-batch',
  imports: [CommonModule, FormsModule],
  templateUrl: './medicine-batch.html',
  styleUrl: './medicine-batch.css',
})
export class MedicineBatch implements OnInit {

  batches: MedicineBatchModel[] = [];
  medicines: MedicineModel[] = [];
  suppliers: SupplierModel[] = [];

  batch: MedicineBatchModel = {
    medicineId: 0,
    batchNumber: '',
    supplierId: undefined,
    manufactureDate: '',
    expiryDate: '',
    purchasePrice: 0,
    sellingPrice: 0,
    isActive: true
  };

  isEdit = false;

  constructor(
    private service: MedicineBatchService,
    private medicineService: MedicineService,
    private supplierService: SupplierService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.loadBatches();
    this.loadDropdownData();
  }

  loadBatches() {
    this.service.getAllBatches()
      .subscribe({
        next: (data) => {
          this.batches = data;
          this.cdr.markForCheck();
        },
        error: (err) => console.error('Error fetching medicine batches:', err)
      });
  }

  /**
   * Loads lists of medicines and suppliers to populate form selectors
   */
  loadDropdownData() {
    this.medicineService.getAllMedicines().subscribe({
      next: (data) => {
        this.medicines = data;
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Error loading medicines for dropdown:', err)
    });

    this.supplierService.getAllSuppliers().subscribe({
      next: (data) => {
        this.suppliers = data;
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Error loading suppliers for dropdown:', err)
    });
  }

  save() {
    if (this.isEdit) {
      this.service.updateBatch(this.batch.id!, this.batch)
        .subscribe({
          next: () => {
            alert("Updated Successfully");
            this.reset();
            this.loadBatches();
          },
          error: (err) => console.error('Error updating batch:', err)
        });
    } else {
      this.service.createBatch(this.batch)
        .subscribe({
          next: () => {
            alert("Saved Successfully");
            this.reset();
            this.loadBatches();
          },
          error: (err) => console.error('Error creating batch:', err)
        });
    }
  }

  edit(b: MedicineBatchModel) {
    this.batch = { ...b };
    this.isEdit = true;
  }

  delete(id: number) {
    if (confirm("Delete this medicine batch?")) {
      this.service.deleteBatch(id)
        .subscribe({
          next: () => {
            alert("Deleted");
            this.loadBatches();
          },
          error: (err) => console.error('Error deleting batch:', err)
        });
    }
  }

  reset() {
    this.batch = {
      medicineId: 0,
      batchNumber: '',
      supplierId: undefined,
      manufactureDate: '',
      expiryDate: '',
      purchasePrice: 0,
      sellingPrice: 0,
      isActive: true
    };
    this.isEdit = false;
  }
}