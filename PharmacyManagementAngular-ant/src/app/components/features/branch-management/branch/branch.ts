import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BranchModel, BranchType } from '../../../../models/branch.model';
import { BranchService } from '../../../../services/branch.service';


@Component({
  selector: 'app-branch',
  imports: [CommonModule, FormsModule],
  templateUrl: './branch.html',
  styleUrl: './branch.css',
})
export class Branch implements OnInit {

  branches: BranchModel[] = [];
  
  // HTML-এ ড্রপডাউন জেনারেট করার জন্য Enum-কে ভ্যারিয়েবলে রাখা হলো
  branchTypes = Object.values(BranchType);

  branch: BranchModel = {
    branchCode: '',
    name: '',
    branchType: BranchType.SUB, // Default হিসেবে SUB সেট করা হলো
    phone: '',
    email: '',
    licenseNumber: '',
    managerName: '',
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
    private service: BranchService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.loadBranches();
  }

  // আপনার সার্ভিসের getAllBranches() মেথডটি কল করা হয়েছে
  loadBranches() {
    this.service.getAllBranches()
      .subscribe({
        next: (data) => {
          this.branches = data;
          this.cdr.markForCheck();
          console.log(data);
        },
        error: (err) => console.error('Error fetching branches:', err)
      });
  }

  // আপনার সার্ভিসের createBranch এবং updateBranch মেথড অনুযায়ী সেভ লজিক
  save() {
    if (this.isEdit) {
      this.service.updateBranch(this.branch.id!, this.branch)
        .subscribe({
          next: () => {
            alert("Updated Successfully");
            this.reset();
            this.loadBranches();
          },
          error: (err) => console.error('Error updating branch:', err)
        });
    } else {
      this.service.createBranch(this.branch)
        .subscribe({
          next: () => {
            alert("Saved Successfully");
            this.reset();
            this.loadBranches();
          },
          error: (err) => console.error('Error creating branch:', err)
        });
    }
  }

  edit(b: BranchModel) {
    // Deep copy setup যাতে nested address অবজেক্টটি টেবিলে সরাসরি মিউটেট না হয়
    this.branch = { 
      ...b,
      address: b.address ? { ...b.address } : {
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

  // আপনার সার্ভিসের deleteBranch(id) মেথড কল করা হয়েছে
  delete(id: number) {
    if (confirm("Delete this branch?")) {
      this.service.deleteBranch(id)
        .subscribe({
          next: (response) => {
            // যেহেতু ব্যাকএন্ড থেকে responseType: 'text' হিসেবে স্ট্রিং মেসেজ আসবে
            alert(response || "Deleted Successfully");
            this.loadBranches();
          },
          error: (err) => console.error('Error deleting branch:', err)
        });
    }
  }

  reset() {
    this.branch = {
      branchCode: '',
      name: '',
      branchType: BranchType.SUB,
      phone: '',
      email: '',
      licenseNumber: '',
      managerName: '',
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