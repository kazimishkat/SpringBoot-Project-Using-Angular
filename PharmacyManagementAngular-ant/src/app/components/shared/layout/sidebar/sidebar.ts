import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sidebar',
  imports: [RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {
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
}


