import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { StockAdjustmentService } from '../../../../../services/stock-adjustment.service';
import { BranchService } from '../../../../../services/branch.service';
import { MedicineService } from '../../../../../services/medicine.service';
import { MedicineBatchService } from '../../../../../services/medicine-batch.service';
import { StockAdjustmentRequest, AdjustmentReason } from '../../../../../models/stock-adjustment.model';

@Component({
  selector: 'app-create-adjustment',
  imports: [FormsModule, CommonModule],
  templateUrl: './create-adjustment.html',
  styleUrl: './create-adjustment.css'
})
export class CreateAdjustmentComponent implements OnInit {

  @ViewChild('adjForm') adjForm!: NgForm;

  branches: any[] = [];
  medicines: any[] = [];
  batchesMap: { [key: number]: any[] } = {}; // Caches batch lists indexed by row indices
  reasons = Object.values(AdjustmentReason);

  adjRequest!: StockAdjustmentRequest;
  submitted = false;
  errorMessage = '';

  constructor(
    private adjService: StockAdjustmentService,
    private branchService: BranchService,
    private medicineService: MedicineService,
    private batchService: MedicineBatchService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.resetForm();
    this.loadBranches();
    this.loadMedicines();
  }

  loadBranches(): void {
    this.branchService.getActiveBranches().subscribe(data => { this.branches = data || []; this.cdr.markForCheck(); });
  }

  loadMedicines(): void {
    this.medicineService.getAllMedicines().subscribe(data => { this.medicines = data || []; this.cdr.markForCheck(); });
  }

  loadBatches(medicineId: number, rowIndex: number): void {
    if (!medicineId) return;
    this.batchService.getBatchesByMedicine(medicineId).subscribe({
      next: (data) => {
        this.batchesMap[rowIndex] = data || [];
        this.cdr.markForCheck();
      }
    });
  }

  onMedicineChange(medicineId: number, rowIndex: number): void {
    this.adjRequest.items[rowIndex].batchId = undefined as any;
    this.adjRequest.items[rowIndex].quantityBefore = 0;
    this.adjRequest.items[rowIndex].quantityAfter = 0;
    this.loadBatches(medicineId, rowIndex);
  }

  calculateDifference(before: number, after: number): number {
    return (after || 0) - (before || 0);
  }

  addItemRow(): void {
    this.adjRequest.items.push({
      batchId: undefined as any,
      quantityBefore: 0,
      quantityAfter: 0,
      reason: AdjustmentReason.PHYSICAL_COUNT_DISCREPANCY,
      remarks: ''
    });
    this.cdr.markForCheck();
  }

  removeItemRow(index: number): void {
    if (this.adjRequest.items.length > 1) {
      this.adjRequest.items.splice(index, 1);
      delete this.batchesMap[index];
      this.cdr.markForCheck();
    }
  }

  saveAdjustment(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.adjForm.invalid) {
      return;
    }

    this.adjService.createAdjustment(this.adjRequest).subscribe({
      next: () => {
        alert('Stock Adjustment Logged and Background Movements Dispatched Successfully');
        this.router.navigate(['/stock-adjustments']);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Failed to process adjustment payload: ${err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  resetForm(): void {
    this.adjRequest = {
      adjustmentNumber: 'ADJ-' + Math.floor(100000 + Math.random() * 900000),
      branchId: undefined as any,
      adjustmentDate: new Date().toISOString().split('T')[0],
      items: [{ batchId: undefined as any, quantityBefore: 0, quantityAfter: 0, reason: AdjustmentReason.PHYSICAL_COUNT_DISCREPANCY, remarks: '' }],
      isActive: true
    };
    this.batchesMap = {};
    this.submitted = false;
    this.errorMessage = '';
    if (this.adjForm) {
      this.adjForm.resetForm({
        adjustmentNumber: this.adjRequest.adjustmentNumber,
        adjustmentDate: this.adjRequest.adjustmentDate
      });
    }
    this.cdr.markForCheck();
  }

  cancel(): void {
    this.router.navigate(['/stock-adjustments']);
  }
}