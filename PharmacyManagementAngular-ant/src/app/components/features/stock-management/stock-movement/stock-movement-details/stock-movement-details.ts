import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { StockMovementService } from '../../../../../services/stock-movement.service';
import { StockMovementResponse } from '../../../../../models/stock-movement.model';

@Component({
  selector: 'app-stock-movement-details',
  imports: [CommonModule, RouterLink],
  templateUrl: './stock-movement-details.html',
  styleUrl: './stock-movement-details.css'
})
export class StockMovementDetailsComponent implements OnInit {

  // =====================================================
  // RECOVERY INDEX STATE CARD PROPERTIES CONFIGURATION
  // =====================================================
  movementId!: number;
  movementDetails: StockMovementResponse | null = null;
  errorMessage = '';

  constructor(
    private movementService: StockMovementService,
    private route: ActivatedRoute,
    private location: Location,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.movementId = Number(idParam);
        this.loadMovementDetails();
      }
    });
  }

  // =====================================================
  // SINGLE DATA BLOCK EXTRACTION SEQUENCES
  // =====================================================
  loadMovementDetails(): void {
    this.errorMessage = '';
    this.movementService.getMovementById(this.movementId).subscribe({
      next: (data) => {
        this.movementDetails = data;
        this.cdr.markForCheck(); // Sync rendering variables state mapping indicators
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Failed to process structural audit sheet tracking properties: ${err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  // =====================================================
  // NAVIGATION LIFE WORKFLOW RETURN COMMAND SWITCHERS
  // =====================================================
  goBack(): void {
    this.location.back();
  }
}