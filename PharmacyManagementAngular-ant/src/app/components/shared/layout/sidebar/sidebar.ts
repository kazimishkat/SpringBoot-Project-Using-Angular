import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.html',
  styleUrls: ['./sidebar.css']
})
export class Sidebar {
  // Dropdown Toggle States
  isSupplierDropdownOpen = false;
  isBranchDropdownOpen = false;
  isBranchInventoryDropdownOpen = false;
  isCategoryDropdownOpen = false;
  isGenericDropdownOpen = false;
  isMedicineDropdownOpen = false;
  isMedicineBatchDropdownOpen = false;
  isDeliveryCompanyDropdownOpen = false;
  isPurchaseOrderDropdownOpen = false;
  isGrnDropdownOpen = false;
  isStockMovementDropdownOpen = false;
  isStockAdjustmentDropdownOpen = false;
  isStockTransferDropdownOpen = false;
  isUserDropdownOpen = false;
  isCustomerDropdownOpen = false;
  isPurchaseReturnDropdownOpen = false;
  isSalesInvoiceDropdownOpen = false;
  isSalesReturnDropdownOpen = false;
  isPaymentDropdownOpen = false;

  // Toggle Methods
  toggleSupplierDropdown(): void {
    this.isSupplierDropdownOpen = !this.isSupplierDropdownOpen;
  }

  toggleBranchDropdown(): void {
    this.isBranchDropdownOpen = !this.isBranchDropdownOpen;
  }

  toggleBranchInventoryDropdown(): void {
    this.isBranchInventoryDropdownOpen = !this.isBranchInventoryDropdownOpen;
  }

  toggleCategoryDropdown(): void {
    this.isCategoryDropdownOpen = !this.isCategoryDropdownOpen;
  }

  toggleGenericDropdown(): void {
    this.isGenericDropdownOpen = !this.isGenericDropdownOpen;
  }

  toggleMedicineDropdown(): void {
    this.isMedicineDropdownOpen = !this.isMedicineDropdownOpen;
  }

  toggleMedicineBatchDropdown(): void {
    this.isMedicineBatchDropdownOpen = !this.isMedicineBatchDropdownOpen;
  }

  toggleDeliveryCompanyDropdown(): void {
    this.isDeliveryCompanyDropdownOpen = !this.isDeliveryCompanyDropdownOpen;
  }

  togglePurchaseOrderDropdown(): void {
    this.isPurchaseOrderDropdownOpen = !this.isPurchaseOrderDropdownOpen;
  }

  toggleGrnDropdown(): void {
    this.isGrnDropdownOpen = !this.isGrnDropdownOpen;
  }

  toggleStockMovementDropdown(): void {
    this.isStockMovementDropdownOpen = !this.isStockMovementDropdownOpen;
  }

  toggleStockAdjustmentDropdown(): void {
    this.isStockAdjustmentDropdownOpen = !this.isStockAdjustmentDropdownOpen;
  }

  toggleStockTransferDropdown(): void {
    this.isStockTransferDropdownOpen = !this.isStockTransferDropdownOpen;
  }

  toggleUserDropdown(): void {
    this.isUserDropdownOpen = !this.isUserDropdownOpen;
  }

  toggleCustomerDropdown(): void {
    this.isCustomerDropdownOpen = !this.isCustomerDropdownOpen;
  }

  togglePurchaseReturnDropdown(): void {
    this.isPurchaseReturnDropdownOpen = !this.isPurchaseReturnDropdownOpen;
  }

  toggleSalesInvoiceDropdown(): void {
    this.isSalesInvoiceDropdownOpen = !this.isSalesInvoiceDropdownOpen;
  }

  toggleSalesReturnDropdown(): void {
    this.isSalesReturnDropdownOpen = !this.isSalesReturnDropdownOpen;
  }

  togglePaymentDropdown(): void {
    this.isPaymentDropdownOpen = !this.isPaymentDropdownOpen;
  }
}