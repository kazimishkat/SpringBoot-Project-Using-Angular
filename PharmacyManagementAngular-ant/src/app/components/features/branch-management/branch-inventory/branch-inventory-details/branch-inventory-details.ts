import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { BranchInventoryRequest, BranchInventoryResponse } from '../../../../../models/branch-inventory.model';
import { BranchInventoryService } from '../../../../../services/branch-inventory.service';


@Component({
  selector: 'app-branch-inventory-details',
  imports: [CommonModule, FormsModule],
  templateUrl: './branch-inventory-details.html',
  styleUrl: './branch-inventory-details.css'
})
export class BranchInventoryDetails implements OnInit {

  @ViewChild('inventoryForm') inventoryForm!: NgForm;

  // =====================================================
  // DASHBOARD LAYOUT & MUTATION STATE MANAGEMENT
  // =====================================================
  /** Master catalogue array loaded on index container section */
  masterInventoryList: BranchInventoryResponse[] = [];
  
  inventoryId!: number;
  inventoryDetails: BranchInventoryResponse | null = null;
  errorMessage = '';
  submitted = false;

  /** Controls layout template status between view logs vs input editors */
  isEditMode = false;

  /** Form binding request entity map utilized during value patching */
  editData!: BranchInventoryRequest;

  constructor(
    private inventoryService: BranchInventoryService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    // 1. First retrieve all recorded inventory metrics arrays
    this.loadMasterCatalogIndexes();

    // 2. Continuous tracking loop intercepting parameterized route identifier tags
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.inventoryId = Number(idParam);
        this.fetchTargetGranularDetails(this.inventoryId);
      }
    });
  }

  // =====================================================
  // MASTER INVENTORY RECOVERY
  // =====================================================
  loadMasterCatalogIndexes(): void {
    this.inventoryService.getAll().subscribe({
      next: (data) => {
        this.masterInventoryList = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Catalog inventory stream processing crashed', err)
    });
  }

  // =====================================================
  // SPECIFIC TARGET LOGIC TRACKING
  // =====================================================
  fetchTargetGranularDetails(id: number): void {
    this.errorMessage = '';
    this.isEditMode = false; // Collapse form nodes dynamically if rows shift mid-operation
    
    this.inventoryService.getById(id).subscribe({
      next: (data) => {
        this.inventoryDetails = data;
        this.inventoryId = id;
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Failed to capture targeted stock parameter layouts', err)
    });
  }

  // =====================================================
  // FORM INTERACTION DOM SWITCHERS
  // =====================================================
  enableEditMode(): void {
    if (!this.inventoryDetails) return;

    this.isEditMode = true;
    this.submitted = false;

    // Maps loaded inventory details precisely onto the request model shape
    this.editData = {
      id: this.inventoryDetails.id,
      branchId: this.inventoryDetails.branchId,
      batchId: this.inventoryDetails.batchId,
      quantityOnHand: this.inventoryDetails.quantityOnHand,
      isActive: this.inventoryDetails.isActive
    };
    
    this.cdr.markForCheck();
  }

  cancelEdit(): void {
    this.isEditMode = false;
    this.submitted = false;
    this.errorMessage = '';

    if (this.inventoryForm) {
      this.inventoryForm.resetForm();
    }
    this.cdr.markForCheck();
  }

  // =====================================================
  // UPDATE SAVE ROUTINES
  // =====================================================
  updateInventory(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (this.inventoryForm.invalid) {
      return;
    }

    this.inventoryService
      .updateInventory(this.inventoryId, this.editData)
      .subscribe({
        next: (res) => {
          alert('Inventory Stock Quantity Patched Successfully');
          this.isEditMode = false;
          this.loadMasterCatalogIndexes(); // Hot refresh master listings data matrix
          this.fetchTargetGranularDetails(this.inventoryId); // Reload focused details panel context
        },
        error: (err) => this.interceptError('Update transaction rejected by database constraints', err)
      });
  }

  // =====================================================
  // DISPOSAL TERMINATIONS
  // =====================================================
  deleteInventory(idToDrop: number): void {
    if (confirm('Are you completely sure you want to purge this inventory allocation record?')) {
      this.inventoryService.deleteInventory(idToDrop).subscribe({
        next: () => {
          alert('Stock Matrix Log Successfully Dropped');
          this.loadMasterCatalogIndexes(); // Immediately sync local memory structures
          
          // Clear operational active panels safely if the focused row index was drop targeted
          if (this.inventoryId === idToDrop) {
            this.inventoryDetails = null;
            this.isEditMode = false;
            this.router.navigate(['/branch-inventories/details']); // Purge route parameters mapping
          }
          this.cdr.markForCheck();
        },
        error: (err) => this.interceptError('Purge sequence rejected by referential constraints', err)
      });
    }
  }

  // =====================================================
  // CORE EXCEPTIONS ROUTING WRAPPERS
  // =====================================================
  private interceptError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}
