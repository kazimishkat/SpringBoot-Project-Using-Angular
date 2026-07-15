import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DeliveryCompanyRequest, DeliveryCompanyResponse } from '../../../../../models/delivery-company.model';
import { DeliveryCompanyService } from '../../../../../services/delivery-company.service';


@Component({
  selector: 'app-company-details',
  imports: [CommonModule, FormsModule],
  templateUrl: './company-details.html',
  styleUrl: './company-details.css'
})
export class DeliveryCompanyDetails implements OnInit {

  @ViewChild('companyForm') companyForm!: NgForm;

  // =====================================================
  // COMBINED DASHBOARD RENDERING STATES
  // =====================================================
  /** Master catalogue list array loaded on index row containers */
  masterCompanyList: DeliveryCompanyResponse[] = [];
  
  companyId!: number;
  companyDetails: DeliveryCompanyResponse | null = null;
  errorMessage = '';
  submitted = false;

  /** Controls layout status between flat sheet representation vs input form panels */
  isEditMode = false;

  /** Mutation mapping object structure targets updating scripts */
  editData!: DeliveryCompanyRequest;

  constructor(
    private companyService: DeliveryCompanyService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    // 1. Initialise full catalog dashboard grid indices array
    this.loadMasterCatalogIndexes();

    // 2. Intercept and continuous tracking stream checking parameterized shifts inside route URLs
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.companyId = Number(idParam);
        this.fetchGranularDetailsPanel(this.companyId);
      }
    });
  }

  // =====================================================
  // MASTER CATALOG RECOVERY SEQUENCES
  // =====================================================
  loadMasterCatalogIndexes(): void {
    this.companyService.getActiveCompanies().subscribe({
      next: (data) => {
        this.masterCompanyList = data || [];
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Catalog processing transaction crashed', err)
    });
  }

  // =====================================================
  // SINGLE PROFILE DETAILS EXTRACTION
  // =====================================================
  fetchGranularDetailsPanel(id: number): void {
    this.errorMessage = '';
    this.isEditMode = false; // Collapse form input fields layout if context row selection changes
    
    this.companyService.getCompanyById(id).subscribe({
      next: (data) => {
        this.companyDetails = data;
        this.companyId = id;
        this.cdr.markForCheck();
      },
      error: (err) => this.interceptError('Failed to capture isolated company configuration attributes', err)
    });
  }

  // =====================================================
  // TOGGLE & INLINE EDIT PARAMETERS SWITCHERS
  // =====================================================
  enableEditMode(): void {
    if (!this.companyDetails) return;

    this.isEditMode = true;
    this.submitted = false;

    // Maps profile configuration fields securely into request mutation targets
    this.editData = {
      id: this.companyDetails.id,
      companyName: this.companyDetails.companyName,
      contactPerson: this.companyDetails.contactPerson,
      phoneNumber: this.companyDetails.phoneNumber,
      apiKey: '', // Keeps integrating token field empty for explicit secure replacements
      isActive: this.companyDetails.isActive
    };
    
    this.cdr.markForCheck();
  }

  cancelEdit(): void {
    this.isEditMode = false;
    this.submitted = false;
    this.errorMessage = '';

    if (this.companyForm) {
      this.companyForm.resetForm();
    }
    this.cdr.markForCheck();
  }

  // =====================================================
  // SAVE / STATUS MUTATION PATTERNS
  // =====================================================
  /**
   * Toggles active status configurations of target entity records via centralized patching.
   */
  toggleCompanyStatus(): void {
    if (!this.companyDetails) return;
    
    const nextStatus = !this.companyDetails.isActive;
    this.companyService.toggleCompanyStatus(this.companyId, nextStatus).subscribe({
      next: (res) => {
        alert('Company Operational Tracking Status Altered Successfully');
        this.loadMasterCatalogIndexes(); // Refresh main lists grid layout matrix
        this.fetchGranularDetailsPanel(this.companyId); // Refresh tracking detail cards indicators
      },
      error: (err) => this.interceptError('Status update request rejected by endpoint rule structures', err)
    });
  }

  // =====================================================
  // GLOBAL TRANSLATOR EXCEPTION WRAPPERS
  // =====================================================
  private interceptError(context: string, error: any): void {
    console.error(error);
    this.errorMessage = `${context}: ${error.error || error.message || 'Server Exception'}`;
    this.cdr.markForCheck();
  }
}
