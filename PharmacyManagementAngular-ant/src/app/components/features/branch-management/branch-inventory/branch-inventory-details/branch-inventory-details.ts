import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink, RouterModule } from '@angular/router';
import { BranchInventoryService } from '../../../../../services/branch-inventory.service';
import { StockMovementService } from '../../../../../services/stock-movement.service';
import { BranchInventoryResponse } from '../../../../../models/branch-inventory.model';

@Component({
  selector: 'app-inventory-details',
  imports: [CommonModule, RouterModule],
  templateUrl: './branch-inventory-details.html',
  styleUrl: './branch-inventory-details.css'
})
export class BranchInventoryDetails implements OnInit {

  // =====================================================
  // DASHBOARD PROFILE DATA & LEDGER HISTORY TRACK STATES
  // =====================================================
  masterInventoryList: BranchInventoryResponse[] = [];
  
  inventoryId!: number;
  inventoryDetails: BranchInventoryResponse | null = null;
  
  /** Stores ledger audit records linked to the specific batch container */
  movementHistory: any[] = [];
  
  errorMessage = '';

  constructor(
    private inventoryService: BranchInventoryService,
    private movementService: StockMovementService,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    // 1. Initialise the side navigation catalogs array
    this.loadMasterCatalogIndexes();

    // 2. Continuous tracking loop monitoring parameter updates inside URLs paths
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.inventoryId = Number(idParam);
        this.fetchTargetProfileDetails(this.inventoryId);
      }
    });
  }

  // =====================================================
  // SIDEBAR CATALOG CONTEXT RECOVERIES
  // =====================================================
  loadMasterCatalogIndexes(): void {
    this.inventoryService.getAllInventory().subscribe({
      next: (data) => {
        this.masterInventoryList = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Catalog directory array loading crashed', err)
    });
  }

  // =====================================================
  // SPECIFIC INVENTORY DETAILED EXTRACTION
  // =====================================================
  fetchTargetProfileDetails(id: number): void {
    this.errorMessage = '';
    this.movementHistory = []; // Clear current history rows before reloading
    
    this.inventoryService.getInventoryById(id).subscribe({
      next: (data) => {
        this.inventoryDetails = data;
        this.inventoryId = id;
        
        // Cascade triggers down to fetch dynamic movement history logs from batchId reference
        if (data.batchId) {
          this.fetchBatchMovementHistory(data.batchId);
        }
        
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Failed to capture targeted inventory profiles metrics', err)
    });
  }

  // =====================================================
  // STOCK MOVEMENT HISTORY SEQUENTIAL RETRIEVES
  // =====================================================
  /** Fetch audit trail allocation lines using the centralized stock movement service API */
  private fetchBatchMovementHistory(batchId: number): void {
    this.movementService.getMovementsByBatchId(batchId).subscribe({
      next: (logs) => {
        this.movementHistory = logs || [];
        this.cdr.markForCheck();
      },
      error: (err) => {
        // Log gracefully without stopping the main UI details view from loading
        console.warn('Silent boundary logger: Failed to fetch ledger history tracks', err);
      }
    });
  }

  // =====================================================
  // RUNTIME TRANSLATOR ERROR WRAPPERS
  // =====================================================
  private interceptError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}