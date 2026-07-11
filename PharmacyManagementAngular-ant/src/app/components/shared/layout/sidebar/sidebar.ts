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

  toggleSupplierDropdown(): void {
    this.isSupplierDropdownOpen = !this.isSupplierDropdownOpen;
  }
}

