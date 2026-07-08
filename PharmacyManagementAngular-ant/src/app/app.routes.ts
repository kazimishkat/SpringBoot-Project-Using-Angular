import { Routes } from '@angular/router';
import { Home } from './components/shared/layout/home/home';
import { Supplier } from './components/features/supplier/supplier';
import { Dashboard } from './components/shared/layout/dashboard/dashboard';
import { Branch } from './components/features/branch-management/branch/branch';
import { BranchInventory } from './components/features/branch-management/branch-inventory/branch-inventory';
import { Medicine } from './components/features/medicine-catalog/medicine/medicine';
import { MedicineCategory } from './components/features/medicine-catalog/medicine-category/medicine-category';
import { GenericMedicine } from './components/features/medicine-catalog/generic-medicine/generic-medicine';
import { MedicineBatch } from './components/features/medicine-catalog/medicine-batch/medicine-batch';

export const routes: Routes = [
  { path: '', component: Home },
  {
    path: 'dashboard',
    component: Dashboard,
    children: [
      { path: '', redirectTo: 'suppliers', pathMatch: 'full' },
      { path: 'suppliers', component: Supplier },
      { path: 'branches', component: Branch },
      { path: 'branch-inventories', component: BranchInventory },
      { path: 'medicines', component: Medicine },
      { path: 'medicine-categories', component: MedicineCategory },
      { path: 'generic-medicines', component: GenericMedicine },
      { path: 'medicine-batches', component: MedicineBatch }
    ]
  }
];


