import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { StockTransferService } from '../../../../../services/stock-transfer.service';
import { BranchService } from '../../../../../services/branch.service';
import { MedicineService } from '../../../../../services/medicine.service';
import { MedicineBatchService } from '../../../../../services/medicine-batch.service';
import { BranchInventoryService } from '../../../../../services/branch-inventory.service';
import { StockTransferRequest, TransferStatus } from '../../../../../models/stock-transfer.model';

@Component({
  selector: 'app-create-transfer',
  imports: [FormsModule, CommonModule],
  templateUrl: './create-transfer.html',
  styleUrl: './create-transfer.css'
})
export class CreateTransferComponent implements OnInit {

  @ViewChild('transferForm') transferForm!: NgForm;

  branches: any[] = [];
  medicines: any[] = [];
  batchesMap: { [key: number]: any[] } = {};
  stockCheckCache: { [key: string]: number } = {}; // Caches target quantities on hand

  transferRequest!: StockTransferRequest;
  submitted = false;
  errorMessage = '';

  constructor(
    private transferService: StockTransferService,
    private branchService: BranchService,
    private medicineService: MedicineService,
    private batchService: MedicineBatchService,
    private inventoryService: BranchInventoryService,
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
    this.batchService.getBatchesByMedicine(medicineId).subscribe(data => {
      this.batchesMap[rowIndex] = data || [];
      this.cdr.markForCheck();
    });
  }

  onMedicineChange(medicineId: number, rowIndex: number): void {
    this.transferRequest.items[rowIndex].batchId = undefined as any;
    this.transferRequest.items[rowIndex].sentQuantity = 1;
    this.loadBatches(medicineId, rowIndex);
  }

  checkStock(batchId: number, rowIndex: number): void {
    const fromBranch = this.transferRequest.fromBranchId;
    if (!fromBranch || !batchId) return;

    this.inventoryService.getAllInventory().subscribe(invList => {
      const match = invList.find(x => x.branchId === Number(fromBranch) && x.batchId === Number(batchId));
      const available = match ? match.quantityOnHand : 0;
      this.stockCheckCache[`${rowIndex}-${batchId}`] = available;
      
      const requestedQty = this.transferRequest.items[rowIndex].sentQuantity;
      if (requestedQty > available) {
        alert(`Warning: Requested quantity (${requestedQty}) exceeds stock on hand (${available}) at Source Branch.`);
      }
      this.cdr.markForCheck();
    });
  }

  addItemRow(): void {
    this.transferRequest.items.push({ batchId: undefined as any, sentQuantity: 1 });
    this.cdr.markForCheck();
  }

  removeItemRow(index: number): void {
    if (this.transferRequest.items.length > 1) {
      this.transferRequest.items.splice(index, 1);
      delete this.batchesMap[index];
      this.cdr.markForCheck();
    }
  }

  saveTransfer(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.transferForm.invalid) {
      return;
    }

    this.transferService.createTransfer(this.transferRequest).subscribe({
      next: () => {
        alert('Stock Transfer Manifest Registered successfully.');
        this.router.navigate(['/stock-transfers']);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Creation payload rejected: ${err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  resetForm(): void {
    this.transferRequest = {
      transferNumber: 'TRF-' + Math.floor(100000 + Math.random() * 900000),
      fromBranchId: undefined as any,
      toBranchId: undefined as any,
      transferDate: new Date().toISOString().split('T')[0],
      status: TransferStatus.PENDING,
      dispatchedById: 1,
      items: [{ batchId: undefined as any, sentQuantity: 1 }],
      isActive: true
    };
    this.batchesMap = {};
    this.stockCheckCache = {};
    this.submitted = false;
    this.errorMessage = '';
    if (this.transferForm) {
      this.transferForm.resetForm({
        transferNumber: this.transferRequest.transferNumber,
        transferDate: this.transferRequest.transferDate,
        status: TransferStatus.PENDING
      });
    }
    this.cdr.markForCheck();
  }

  cancel(): void {
    this.router.navigate(['/stock-transfers']);
  }
}