import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BranchInventoryModel } from '../../../../models/branch-inventory.model';
import { BranchModel } from '../../../../models/branch.model';
import { MedicineBatchModel } from '../../../../models/medicine-batch.model';
import { BranchInventoryService } from '../../../../services/branch-inventory.service';
import { BranchService } from '../../../../services/branch.service';
import { MedicineBatchService } from '../../../../services/medicine-batch.service';


@Component({
  selector: 'app-branch-inventory',
  imports: [CommonModule, FormsModule],
  templateUrl: './branch-inventory.html',
  styleUrl: './branch-inventory.css',
})
export class BranchInventory implements OnInit {

  inventories: BranchInventoryModel[] = [];
  branches: BranchModel[] = [];
  batches: MedicineBatchModel[] = [];

  inventory: BranchInventoryModel = {
    branchId: 0,
    batchId: 0,
    quantityOnHand: 0,
    quantityReserved: 0,
    isActive: true
  };

  isEdit = false;

  constructor(
    private service: BranchInventoryService,
    private branchService: BranchService,
    private batchService: MedicineBatchService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.loadInventories();
    this.loadDropdownData();
  }

  loadInventories() {
    this.service.getAllInventories()
      .subscribe({
        next: (data) => {
          this.inventories = data;
          this.cdr.markForCheck();
          console.log(data);
        },
        error: (err) => console.error('Error fetching branch inventories:', err)
      });
  }

  loadDropdownData() {
    this.branchService.getAllBranches().subscribe({
      next: (data) => {
        this.branches = data;
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Error loading branches:', err)
    });

    this.batchService.getAllBatches().subscribe({
      next: (data) => {
        this.batches = data;
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Error loading medicine batches:', err)
    });
  }

  save() {
    if (this.isEdit) {
      this.service.updateInventory(this.inventory.id!, this.inventory)
        .subscribe({
          next: () => {
            alert("Updated Successfully");
            this.reset();
            this.loadInventories();
          },
          error: (err) => console.error('Error updating inventory:', err)
        });
    } else {
      this.service.createInventory(this.inventory)
        .subscribe({
          next: () => {
            alert("Saved Successfully");
            this.reset();
            this.loadInventories();
          },
          error: (err) => console.error('Error creating inventory:', err)
        });
    }
  }

  edit(i: BranchInventoryModel) {
    this.inventory = { ...i };
    this.isEdit = true;
  }

  delete(id: number) {
    if (confirm("Delete this inventory record?")) {
      this.service.deleteInventory(id)
        .subscribe({
          next: () => {
            alert("Deleted");
            this.loadInventories();
          },
          error: (err) => console.error('Error deleting inventory:', err)
        });
    }
  }

  reset() {
    this.inventory = {
      branchId: 0,
      batchId: 0,
      quantityOnHand: 0,
      quantityReserved: 0,
      isActive: true
    };
    this.isEdit = false;
  }
}