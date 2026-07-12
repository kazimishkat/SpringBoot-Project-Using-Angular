import { Routes } from '@angular/router';
import { Home } from './components/shared/layout/home/home';
import { Dashboard } from './components/shared/layout/dashboard/dashboard';
import { SupplierListComponent } from './components/features/supplier/supplier-list/supplier-list';
import { AddSupplier } from './components/features/supplier/add-supplier/add-supplier';
import { SupplierDetails } from './components/features/supplier/supplier-details/supplier-details';
import { AddBranch } from './components/features/branch-management/branch/add-branch/add-branch';
import { BranchDetails } from './components/features/branch-management/branch/branch-details/branch-details';
import { BranchListComponent } from './components/features/branch-management/branch/branch-list/branch-list';
import { BranchInventoryList } from './components/features/branch-management/branch-inventory/branch-inventory-list/branch-inventory-list';
import { BranchInventoryDetails } from './components/features/branch-management/branch-inventory/branch-inventory-details/branch-inventory-details';
import { CategoryListComponent } from './components/features/medicine-catalog/medicine-category/category-list/category-list';
import { CategoryAdd } from './components/features/medicine-catalog/medicine-category/category-add/category-add';


export const routes: Routes = [
  { path: '', component: Home },
  {
    path: 'dashboard',
    component: Dashboard,
    children: [
      { path: 'suppliers', component: SupplierListComponent },
      { path: 'suppliers/add', component: AddSupplier },
      { path: 'suppliers/details', component: SupplierDetails },
      { path: 'suppliers/details/:id', component: SupplierDetails },
      { path: 'branches', component: BranchListComponent },
      { path: 'branches/add', component: AddBranch },
      { path: 'branches/details', component: BranchDetails },
      { path: 'branches/details/:id', component: BranchDetails },
      { path: 'branch-inventories', component: BranchInventoryList },
      { path: 'branch-inventories/details', component: BranchInventoryDetails },
      { path: 'branch-inventories/details/:id', component: BranchInventoryDetails },
      { path: 'medicine-categories', component: CategoryListComponent },
      { path: 'medicine-categories/add', component: CategoryAdd },
    ]
  }
];


