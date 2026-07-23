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
import { GenericListComponent } from './components/features/medicine-catalog/generic-medicine/generic-list/generic-list';
import { GenericAdd } from './components/features/medicine-catalog/generic-medicine/generic-add/generic-add';
import { GenericDetailsComponent } from './components/features/medicine-catalog/generic-medicine/generic-details/generic-details';
import { MedicineListComponent } from './components/features/medicine-catalog/medicine/medicine-list/medicine-list';
import { MedicineAdd } from './components/features/medicine-catalog/medicine/medicine-add/medicine-add';
import { MedicineDetailsComponent } from './components/features/medicine-catalog/medicine/medicine-details/medicine-details'; 
import { DeliveryCompanyListComponent } from './components/features/delivery-management/delivery-company/company-list/company-list';
import { DeliveryCompanyAdd } from './components/features/delivery-management/delivery-company/company-add/company-add';
import { DeliveryCompanyDetails } from './components/features/delivery-management/delivery-company/company-details/company-details';
import { PurchaseOrderList } from './components/features/purchase-management/purchase-order/purchase-order-list/purchase-order-list';
import { CreatePurchaseOrderComponent } from './components/features/purchase-management/purchase-order/create-purchase-order/create-purchase-order';
import { PurchaseOrderDetailsComponent } from './components/features/purchase-management/purchase-order/purchase-order-details/purchase-order-details';
import { GoodsReceivedNoteList } from './components/features/purchase-management/goods-received-note/grn-list/grn-list';
import { ReceiveGoods } from './components/features/purchase-management/goods-received-note/receive-goods/receive-goods';
import { GoodsReceivedNoteDetails } from './components/features/purchase-management/goods-received-note/grn-details/grn-details';
import { MedicineBatchList } from './components/features/medicine-catalog/medicine-batch/medicine-batch-list/medicine-batch-list';
import { MedicineBatchDetailsComponent } from './components/features/medicine-catalog/medicine-batch/medicine-batch-details/medicine-batch-details';
import { StockMovementHistoryComponent } from './components/features/stock-management/stock-movement/stock-movement-history/stock-movement-history';
import { StockMovementDetailsComponent } from './components/features/stock-management/stock-movement/stock-movement-details/stock-movement-details';
import { StockAdjustmentListComponent } from './components/features/stock-management/stock-adjustment/stock-adjustment-list/stock-adjustment-list';
import { CreateAdjustmentComponent } from './components/features/stock-management/stock-adjustment/create-adjustment/create-adjustment';
import { StockAdjustmentDetailsComponent } from './components/features/stock-management/stock-adjustment/stock-adjustment-details/stock-adjustment-details';
import { StockTransferListComponent } from './components/features/stock-management/stock-transfer/stock-transfer-list/stock-transfer-list';
import { CreateTransferComponent } from './components/features/stock-management/stock-transfer/create-transfer/create-transfer';
import { ApproveTransferComponent } from './components/features/stock-management/stock-transfer/approve-transfer/approve-transfer';
import { StockTransferDetailsComponent } from './components/features/stock-management/stock-transfer/stock-transfer-details/stock-transfer-details';
import { LoginComponent } from './components/authentication/authFeatures/login/login';
import { ForgotPasswordComponent } from './components/authentication/authFeatures/forgot-password/forgot-password';
import { ResetPasswordComponent } from './components/authentication/authFeatures/reset-password/reset-password';
import { VerifyEmailComponent } from './components/authentication/authFeatures/verify-email/verify-email';
import { UnauthorizedComponent } from './components/authentication/authFeatures/unauthorized/unauthorized';
import { UserList } from './components/features/user-management/user-list/user-list';
import { UserAdd } from './components/features/user-management/user-add/user-add';
import { UserEdit } from './components/features/user-management/user-edit/user-edit';
import { UserDetails } from './components/features/user-management/user-details/user-details';
import { Profile } from './components/features/user-management/profile/profile';
import { ChangePassword } from './components/features/user-management/change-password/change-password';
import { CustomerAdd } from './components/features/customer-management/customer-add/customer-add';
import { CustomerDetails } from './components/features/customer-management/customer-details/customer-details';
import { CustomerEdit } from './components/features/customer-management/customer-edit/customer-edit';
import { CustomerListComponent } from './components/features/customer-management/customer-list/customer-list';
import { CreateReturnComponent } from './components/features/purchase-management/purchase-return/create-return/create-return';
import { ReturnDetailsComponent } from './components/features/purchase-management/purchase-return/return-details/return-details';
import { ReturnListComponent } from './components/features/purchase-management/purchase-return/return-list/return-list';
import { InvoiceListComponent } from './components/features/sales-management/sales-invoice/invoice-list/invoice-list';
import { CreateInvoiceComponent } from './components/features/sales-management/sales-invoice/create-invoice/create-invoice';
import { InvoiceDetailsComponent } from './components/features/sales-management/sales-invoice/invoice-details/invoice-details';
import { SalesReturnListComponent } from './components/features/sales-management/sales-return/sales-return-list/sales-return-list';
import { CreateSalesReturnComponent } from './components/features/sales-management/sales-return/create-sales-return/create-sales-return';
import { SalesReturnDetailsComponent } from './components/features/sales-management/sales-return/sales-return-details/sales-return-details';
import { PaymentListComponent } from './components/features/sales-management/payment/payment-list/payment-list';
import { PaymentDetailsComponent } from './components/features/sales-management/payment/payment-details/payment-details';


export const routes: Routes = [
  { path: '', component: Home },
  { path: 'customers/add', redirectTo: 'dashboard/customers/add', pathMatch: 'full' },
  { path: 'customers/edit/:id', redirectTo: 'dashboard/customers/edit/:id', pathMatch: 'full' },
  { path: 'customers/edit', redirectTo: 'dashboard/customers/edit', pathMatch: 'full' },
  { path: 'customers/details', redirectTo: 'dashboard/customers/details', pathMatch: 'full' },
  { path: 'customers/:id', redirectTo: 'dashboard/customers/:id', pathMatch: 'full' },
  { path: 'customers', redirectTo: 'dashboard/customers', pathMatch: 'full' },
  { path: 'purchase-returns/create', redirectTo: 'dashboard/purchase-returns/create', pathMatch: 'full' },
  { path: 'purchase-returns/details', redirectTo: 'dashboard/purchase-returns/details', pathMatch: 'full' },
  { path: 'purchase-returns/details/:id', redirectTo: 'dashboard/purchase-returns/details/:id', pathMatch: 'full' },
  { path: 'purchase-returns/:id', redirectTo: 'dashboard/purchase-returns/:id', pathMatch: 'full' },
  { path: 'purchase-returns', redirectTo: 'dashboard/purchase-returns', pathMatch: 'full' },
  { path: 'stock-movements/:id', redirectTo: 'dashboard/stock-movements/:id', pathMatch: 'full' },
  { path: 'stock-movements', redirectTo: 'dashboard/stock-movements', pathMatch: 'full' },
  { path: 'stock-adjustments/:id', redirectTo: 'dashboard/stock-adjustments/:id', pathMatch: 'full' },
  { path: 'stock-adjustments/create', redirectTo: 'dashboard/stock-adjustments/create', pathMatch: 'full' },
  { path: 'stock-adjustments', redirectTo: 'dashboard/stock-adjustments', pathMatch: 'full' },
  { path: 'stock-transfers/create', redirectTo: 'dashboard/stock-transfers/create', pathMatch: 'full' },
  { path: 'stock-transfers/approve/:id', redirectTo: 'dashboard/stock-transfers/approve/:id', pathMatch: 'full' },
  { path: 'stock-transfers/:id', redirectTo: 'dashboard/stock-transfers/:id', pathMatch: 'full' },
  { path: 'stock-transfers', redirectTo: 'dashboard/stock-transfers', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'reset-password', component: ResetPasswordComponent },
  { path: 'verify-email', component: VerifyEmailComponent },
  { path: 'unauthorized', component: UnauthorizedComponent },
  { path: 'users/add', redirectTo: 'dashboard/users/add', pathMatch: 'full' },
  { path: 'users/edit/:id', redirectTo: 'dashboard/users/edit/:id', pathMatch: 'full' },
  { path: 'users/:id', redirectTo: 'dashboard/users/:id', pathMatch: 'full' },
  { path: 'users', redirectTo: 'dashboard/users', pathMatch: 'full' },
  { path: 'profile', redirectTo: 'dashboard/profile', pathMatch: 'full' },
  { path: 'change-password', redirectTo: 'dashboard/change-password', pathMatch: 'full' },
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
      { path: 'generic-medicines', component: GenericListComponent },
      { path: 'generic-medicines/add', component: GenericAdd },
      { path: 'generic-medicines/details', component: GenericDetailsComponent },
      { path: 'generic-medicines/details/:id', component: GenericDetailsComponent },
      { path: 'medicines', component: MedicineListComponent },
      { path: 'medicines/add', component: MedicineAdd },
      { path: 'medicines/details', component: MedicineDetailsComponent },
      { path: 'medicines/details/:id', component: MedicineDetailsComponent },
      { path: 'medicine-batches', component: MedicineBatchList },
      { path: 'medicine-batches/details', component: MedicineBatchDetailsComponent },
      { path: 'medicine-batches/details/:id', component: MedicineBatchDetailsComponent },
      { path: 'delivery-companies', component: DeliveryCompanyListComponent },
      { path: 'delivery-companies/add', component: DeliveryCompanyAdd },
      { path: 'delivery-companies/details', component: DeliveryCompanyDetails },
      { path: 'delivery-companies/details/:id', component: DeliveryCompanyDetails },
      { path: 'purchase-orders', component: PurchaseOrderList },
      { path: 'purchase-orders/create', component: CreatePurchaseOrderComponent },
      { path: 'purchase-orders/details', component: PurchaseOrderDetailsComponent },
      { path: 'purchase-orders/details/:id', component: PurchaseOrderDetailsComponent },
      { path: 'grns', component: GoodsReceivedNoteList },
      { path: 'grns/receive', component: ReceiveGoods },
      { path: 'grns/details', component: GoodsReceivedNoteDetails },
      { path: 'grns/details/:id', component: GoodsReceivedNoteDetails },
      { path: 'stock-movements', component: StockMovementHistoryComponent },
      { path: 'stock-movements/details', component: StockMovementDetailsComponent },
      { path: 'stock-movements/details/:id', component: StockMovementDetailsComponent },
      { path: 'stock-movements/:id', component: StockMovementDetailsComponent },
      { path: 'stock-adjustments', component: StockAdjustmentListComponent },
      { path: 'stock-adjustments/create', component: CreateAdjustmentComponent },
      { path: 'stock-adjustments/details', component: StockAdjustmentDetailsComponent },
      { path: 'stock-adjustments/details/:id', component: StockAdjustmentDetailsComponent },
      { path: 'stock-adjustments/:id', component: StockAdjustmentDetailsComponent },
      { path: 'stock-transfers', component: StockTransferListComponent },
      { path: 'stock-transfers/create', component: CreateTransferComponent },
      { path: 'stock-transfers/approve', component: ApproveTransferComponent },
      { path: 'stock-transfers/approve/:id', component: ApproveTransferComponent },
      { path: 'stock-transfers/details', component: StockTransferDetailsComponent },
      { path: 'stock-transfers/details/:id', component: StockTransferDetailsComponent },
      { path: 'stock-transfers/:id', component: StockTransferDetailsComponent },
      { path: 'users', component: UserList },
      { path: 'users/add', component: UserAdd },
      { path: 'users/edit/:id', component: UserEdit },
      { path: 'users/:id', component: UserDetails },
      { path: 'profile', component: Profile },
      { path: 'change-password', component: ChangePassword },
      { path: 'customers', component: CustomerListComponent },
      { path: 'customers/add', component: CustomerAdd },
      { path: 'customers/details', component: CustomerDetails },
      { path: 'customers/details/:id', component: CustomerDetails },
      { path: 'customers/edit', component: CustomerEdit },
      { path: 'customers/edit/:id', component: CustomerEdit },
      { path: 'customers/:id', component: CustomerDetails },
      { path: 'purchase-returns', component: ReturnListComponent },
      { path: 'purchase-returns/create', component: CreateReturnComponent },
      { path: 'purchase-returns/details', component: ReturnDetailsComponent },
      { path: 'purchase-returns/details/:id', component: ReturnDetailsComponent },
      { path: 'purchase-returns/:id', component: ReturnDetailsComponent },
      { path: 'sales-invoices', component: InvoiceListComponent },
      { path: 'sales-invoices/create', component: CreateInvoiceComponent },
      { path: 'sales-invoices/details', component: InvoiceDetailsComponent },
      { path: 'sales-invoices/details/:id', component: InvoiceDetailsComponent },
      { path: 'sales-invoices/:id', component: InvoiceDetailsComponent },
      { path: 'sales-returns', component: SalesReturnListComponent },
      { path: 'sales-returns/create', component: CreateSalesReturnComponent },
      { path: 'sales-returns/details', component: SalesReturnDetailsComponent },
      { path: 'sales-returns/details/:id', component: SalesReturnDetailsComponent },
      { path: 'sales-returns/:id', component: SalesReturnDetailsComponent },
      { path: 'payments', component: PaymentListComponent },
      { path: 'payments/details', component: PaymentDetailsComponent },
      { path: 'payments/details/:id', component: PaymentDetailsComponent },
      { path: 'payments/:id', component: PaymentDetailsComponent },
    ]
  }
];


