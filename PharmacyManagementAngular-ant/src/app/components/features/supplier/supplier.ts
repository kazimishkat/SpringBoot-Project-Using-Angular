import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SupplierService } from '../../../services/supplier.service';
import { SupplierModel } from '../../../models/supplier.model';


@Component({
  selector: 'app-Supplier',
  imports: [CommonModule, FormsModule],
  templateUrl: './supplier.html',
  styleUrl: './supplier.css',
})
export class Supplier implements OnInit {

  suppliers: SupplierModel[] = [];

  supplier: SupplierModel = {
    supplierCode: '',
    name: '',
    contactPerson: '',
    phone: '',
    email: '',
    tradeLicenseNo: '',
    taxId: '',
    isActive: true,
    address: {
      addressLine1: '',
      addressLine2: '',
      city: '',
      state: '',
      postalCode: '',
      country: ''
    }
  };

  isEdit = false;

  constructor(
    private service: SupplierService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.loadSuppliers();
  }

  loadSuppliers() {
    this.service.getAllSuppliers()
      .subscribe({
        next: (data) => {
          this.suppliers = data;
          this.cdr.markForCheck();
          console.log(data);
        },
        error: (err) => console.error('Error fetching suppliers:', err)
      });
  }

  save() {
    if (this.isEdit) {
      this.service.updateSupplier(this.supplier.id!, this.supplier)
        .subscribe({
          next: () => {
            alert("Updated Successfully");
            this.reset();
            this.loadSuppliers();
          },
          error: (err) => console.error('Error updating supplier:', err)
        });
    } else {
      this.service.createSupplier(this.supplier)
        .subscribe({
          next: () => {
            alert("Saved Successfully");
            this.reset();
            this.loadSuppliers();
          },
          error: (err) => console.error('Error creating supplier:', err)
        });
    }
  }

  edit(s: SupplierModel) {
    // Deep copy setup to ensure the nested address object doesn't mutate directly in the table
    this.supplier = { 
      ...s,
      address: s.address ? { ...s.address } : {
        addressLine1: '',
        addressLine2: '',
        city: '',
        state: '',
        postalCode: '',
        country: ''
      }
    };
    this.isEdit = true;
  }

  delete(id: number) {
    if (confirm("Delete this supplier?")) {
      this.service.deleteSupplier(id)
        .subscribe({
          next: () => {
            alert("Deleted");
            this.loadSuppliers();
          },
          error: (err) => console.error('Error deleting supplier:', err)
        });
    }
  }

  reset() {
    this.supplier = {
      supplierCode: '',
      name: '',
      contactPerson: '',
      phone: '',
      email: '',
      tradeLicenseNo: '',
      taxId: '',
      isActive: true,
      address: {
        addressLine1: '',
        addressLine2: '',
        city: '',
        state: '',
        postalCode: '',
        country: ''
      }
    };
    this.isEdit = false;
  }
}