import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { SalesReturnService } from '../../../../../services/sales-return.service';
import { SalesReturnResponse, ApprovalStatus } from '../../../../../models/sales-return.model';

@Component({
  selector: 'app-sales-return-details',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sales-return-details.html',
  styleUrl: './sales-return-details.css'
})
export class SalesReturnDetailsComponent implements OnInit {

  returnId!: number;
  returnDetails: SalesReturnResponse | null = null;
  errorMessage = '';

  allReturns: SalesReturnResponse[] = [];

  constructor(
    private salesReturnService: SalesReturnService,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.salesReturnService.getAllReturns().subscribe({
      next: (data) => {
        this.allReturns = data || [];
        this.route.paramMap.subscribe(params => {
          const id = params.get('id');
          if (id) {
            this.returnId = Number(id);
            this.getReturnById(this.returnId);
          } else if (this.allReturns.length > 0) {
            this.returnId = this.allReturns[0].id;
            this.getReturnById(this.returnId);
          }
        });
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Failed to prefetch sales returns:', err);
      }
    });
  }

  onReturnChange(event: any): void {
    const selectedId = Number(event.target.value);
    if (selectedId) {
      this.returnId = selectedId;
      this.getReturnById(selectedId);
      this.router.navigate(['/dashboard/sales-returns/details', selectedId]);
    }
  }

  getReturnById(id: number): void {
    this.errorMessage = '';
    this.salesReturnService.getReturnById(id).subscribe({
      next: (data) => {
        this.returnDetails = data;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Failed to fetch sales return details: ${err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  approveReturn(): void {
    if (!this.returnDetails) return;
    if (!confirm('Are you sure you want to APPROVE this sales return? Stock will be credited back into inventory.')) return;

    this.salesReturnService.approveReturn(this.returnDetails.id).subscribe({
      next: (res) => {
        alert('Sales Return approved successfully! Stock restored.');
        this.returnDetails = res;
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Failed to approve sales return', err)
    });
  }

  rejectReturn(): void {
    if (!this.returnDetails) return;
    if (!confirm('Are you sure you want to REJECT this sales return?')) return;

    this.salesReturnService.rejectReturn(this.returnDetails.id).subscribe({
      next: (res) => {
        alert('Sales Return rejected.');
        this.returnDetails = res;
        this.cdr.markForCheck();
      },
      error: (err) => this.handleError('Failed to reject sales return', err)
    });
  }

  getTotalRefundAmount(): number {
    if (!this.returnDetails || !this.returnDetails.items) return 0;
    return this.returnDetails.items.reduce((sum, item) => sum + (item.refundAmount || 0), 0);
  }

  goBack(): void {
    this.location.back();
  }

  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}