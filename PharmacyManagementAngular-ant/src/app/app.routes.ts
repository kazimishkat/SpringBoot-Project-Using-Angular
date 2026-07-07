import { Routes } from '@angular/router';
import { Home } from './components/shared/layout/home/home';
import { Supplier } from './components/features/supplier/supplier';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'suppliers', component: Supplier }
];
