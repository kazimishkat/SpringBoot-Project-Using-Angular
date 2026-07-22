import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { PurchaseReturnService } from '../../../../../services/purchase-return.service';
import { PurchaseReturnResponse, ApprovalStatus } from '../../../../../models/purchase-return.model';

@Component({
  selector: 'app-return-details',
  imports: [CommonModule, RouterModule],
  templateUrl: './return-details.html',
  styleUrl: './return-details.css'
})
export class ReturnDetailsComponent implements OnInit {

  masterReturnList: PurchaseReturnResponse[] = [];
  returnId!: number;
  returnDetails: PurchaseReturnResponse | null = null;
  errorMessage = '';

  constructor(
    private returnService: PurchaseReturnService,
    private route: ActivatedRoute,
    private location: Location,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadMasterCatalogIndexes();
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id && id !== 'undefined') {
        this.returnId = Number(id);
        this.loadReturn(this.returnId);
      } else {
        this.returnDetails = null;
        this.cdr.markForCheck();
      }
    });
  }

  loadMasterCatalogIndexes(): void {
    this.returnService.getAllPurchaseReturns().subscribe({
      next: (data) => {
        this.masterReturnList = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.cdr.markForCheck();
      }
    });
  }

  loadReturn(id: number): void {
    if (!id || isNaN(id)) return;
    this.errorMessage = '';
    this.returnService.getPurchaseReturnById(id).subscribe({
      next: (data) => {
        this.returnDetails = data;
        if (data && data.id) {
          this.returnId = data.id; // 💡 নিশ্চিত করা হলো valid ID সেটিং
        }
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Failed to fetch return details: ${err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  /** Safe helper method to prevent sending 'undefined' ID to backend */
  private getValidTargetId(): number | null {
    const activeId = this.returnId || this.returnDetails?.id;
    if (!activeId || isNaN(activeId)) {
      this.errorMessage = 'Action blocked: Invalid or missing Purchase Return ID.';
      this.cdr.markForCheck();
      return null;
    }
    return activeId;
  }

  approveReturn(): void {
    const targetId = this.getValidTargetId();
    if (!targetId) return;

    if (!confirm('Approve this supplier return? This confirms inventory debiting.')) return;

    this.errorMessage = '';
    this.returnService.approvePurchaseReturn(targetId).subscribe({
      next: (res) => {
        alert('Purchase Return status updated to APPROVED.');
        if (this.returnDetails) {
          this.returnDetails.approvalStatus = ApprovalStatus.APPROVED;
        }
        this.loadMasterCatalogIndexes();
      },
      error: (err) => this.handleError('Approval rejected', err)
    });
  }

  cancelReturn(): void {
    const targetId = this.getValidTargetId();
    if (!targetId) return;

    if (!confirm('Cancel and remove this Purchase Return record? This restores debited stock back into inventory.')) return;

    this.errorMessage = '';
    this.returnService.cancelPurchaseReturn(targetId).subscribe({
      next: () => {
        alert('Purchase Return deleted. Debited stock added back to inventory.');
        this.location.back();
      },
      error: (err) => this.handleError('Cancellation procedure failed', err)
    });
  }

  printReturn(): void {
    const targetId = this.getValidTargetId();
    if (!targetId) return;

    this.returnService.printPurchaseReturn(targetId).subscribe({
      next: () => {
        window.print();
      },
      error: () => {
        window.print();
      }
    });
  }

  goBack(): void {
    this.location.back();
  }

  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error?.message || error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}