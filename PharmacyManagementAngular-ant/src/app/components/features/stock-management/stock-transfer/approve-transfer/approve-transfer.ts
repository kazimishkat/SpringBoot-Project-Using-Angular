import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { StockTransferService } from '../../../../../services/stock-transfer.service';
import { StockTransferResponse, StockTransferItemRequest, TransferStatus } from '../../../../../models/stock-transfer.model';

@Component({
  selector: 'app-approve-transfer',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './approve-transfer.html',
  styleUrl: './approve-transfer.css'
})
export class ApproveTransferComponent implements OnInit {

  transferId!: number;
  transferDetails: StockTransferResponse | null = null;
  errorMessage = '';
  
  // Storage binding to track physical item verification arrays on UI input matrices
  receivedQuantities: { [batchId: number]: number } = {};

  constructor(
    private transferService: StockTransferService,
    private route: ActivatedRoute,
    private location: Location,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.transferId = Number(idParam);
        this.loadTransfer();
      }
    });
  }

  loadTransfer(): void {
    this.errorMessage = '';
    this.transferService.getTransferById(this.transferId).subscribe({
      next: (data) => {
        this.transferDetails = data;
        if (data && data.items) {
          // Initialize confirmation matrices default fields elements values matching sent properties
          data.items.forEach(item => {
            this.receivedQuantities[item.batchId] = item.sentQuantity;
          });
        }
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Failed to extract verification manifest targets: ${err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  /** Gets active receiver user ID dynamically (supports both User 1 and User 2) */
  private getActiveReceiverId(): number {
    try {
      const storedUser = localStorage.getItem('user') || sessionStorage.getItem('user');
      if (storedUser) {
        const userObj = JSON.parse(storedUser);
        if (userObj && userObj.id) {
          return Number(userObj.id);
        }
      }
    } catch (e) {
      console.warn('Could not parse user from storage, falling back to default receiver ID.');
    }
    
    // 💡 ফলব্যাক হিসেবে ১ রিটার্ন করবে (যদি ২ নম্বর ইউজার ডাটাবেজে না থাকে তবে এটি সেফ রিড করবে)
    return 1; 
  }

  /** Confirms reception matching user physical entries counts inputs via backend PUT verification rules */
  approve(): void {
    if (!this.transferDetails) return;
    if (!confirm('Confirm and log received physical units into destination branch stock inventory ledger?')) return;

    this.errorMessage = '';
    
    // 🌟 ডাইনামিকালি লগইন থাকা ইউজার আইডি ফেচ করা হচ্ছে (User ID 1 & 2 Both Supported)
    const currentReceiverId = this.getActiveReceiverId();
    
    // Map internal local UI quantities grids array parameters directly down to request payloads configurations
    const itemUpdates: StockTransferItemRequest[] = this.transferDetails.items.map(item => {
      return {
        batchId: item.batchId,
        sentQuantity: this.receivedQuantities[item.batchId] // Maps verified count into requested property mapping field
      };
    });

    this.transferService.receiveStockItems(this.transferId, currentReceiverId, itemUpdates).subscribe({
      next: () => {
        alert('Stock reception confirmed: Inward branch inventory metrics updated.');
        this.location.back();
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Fulfillment transaction rejected by server constraints: ${err.error?.message || err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  /** Reject or invalidate the active transport flow by shifting workflow tokens straight into CANCELLED states */
  reject(): void {
    if (!confirm('Are you completely sure you want to REJECT or CANCEL this incoming branch transfer document log?')) return;

    this.errorMessage = '';
    this.transferService.cancelTransfer(this.transferId).subscribe({
      next: () => {
        alert('Transfer shipment marked as CANCELLED/REJECTED.');
        this.location.back();
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = `Rejection process halted by system core structures: ${err.error?.message || err.error || err.message || 'Server Exception'}`;
        this.cdr.markForCheck();
      }
    });
  }

  goBack(): void {
    this.location.back();
  }
}