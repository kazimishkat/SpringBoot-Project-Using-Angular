import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import {  RouterModule } from '@angular/router';
import { DeliveryCompanyResponse } from '../../../../../models/delivery-company.model';
import { DeliveryCompanyService } from '../../../../../services/delivery-company.service';


@Component({
  selector: 'app-company-list',
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './company-list.html',
  styleUrl: './company-list.css'
})
export class DeliveryCompanyListComponent implements OnInit {

  // =====================================================
  // MASTER ARRAY VIEW CONFIGURATIONS
  // =====================================================
  companies: DeliveryCompanyResponse[] = [];
  errorMessage = '';

  constructor(
    private companyService: DeliveryCompanyService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadActiveCompanies();
  }

  // =====================================================
  // LOAD SYSTEM INVENTORY
  // =====================================================
  /**
   * Retrieve all active documented delivery companies from server indexes.
   */
  loadActiveCompanies(): void {
    this.errorMessage = '';
    this.companyService.getActiveCompanies().subscribe({
      next: (data) => {
        this.companies = data || [];
        this.cdr.markForCheck(); // Trigger explicit rendering validation push
      },
      error: (err) => {
        this.handleError('Failed to fetch structural delivery logs registry', err);
      }
    });
  }

  // =====================================================
  // STATUS MUTATION PATCH LOGICS
  // =====================================================
  /**
   * Toggles the active status parameters of a logistics service provider profile.
   */
  toggleStatus(item: DeliveryCompanyResponse): void {
    const nextStatus = !item.isActive;
    this.companyService.toggleCompanyStatus(item.id, nextStatus).subscribe({
      next: (res) => {
        item.isActive = res.isActive; // Sync local grid reference parameter state
        alert(`Company configuration status shifted to: ${res.isActive ? 'ACTIVE' : 'INACTIVE'}`);
        this.loadActiveCompanies(); // Refresh grid layout context
      },
      error: (err) => {
        this.handleError('Status patch request rejected by backend constraints', err);
      }
    });
  }

  // =====================================================
  // UNIVERSAL CONTEXT INTERCEPTOR EXCEPTION LOGGER
  // =====================================================
  private handleError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}
