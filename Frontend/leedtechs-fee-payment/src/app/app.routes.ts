import { Routes } from '@angular/router';
import { PaymentComponent } from './components/payment/payment';
import { LookupComponent } from './components/lookup/lookup';
import { RegisterComponent } from './components/register/register';

export const routes: Routes = [
  { path: '',         redirectTo: 'payment', pathMatch: 'full' },
  { path: 'payment',  component: PaymentComponent },
  { path: 'lookup',   component: LookupComponent },
  { path: 'register', component: RegisterComponent },
];
