import { Routes } from '@angular/router';
import { authGuard }       from './auth/guards/auth.guard';
import { individualGuard } from './auth/guards/role.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  {
    path: 'login',
    loadComponent: () => import('./auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./auth/register/register.component').then(m => m.RegisterComponent)
  },

  // Protected routes — require valid JWT
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () => import('./dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
    path: 'categories',
    canActivate: [authGuard],
    loadComponent: () => import('./categories/category-list.component').then(m => m.CategoryListComponent)
  },
  {
    path: 'transactions',
    canActivate: [authGuard],
    loadComponent: () => import('./transactions/transaction-list.component').then(m => m.TransactionListComponent)
  },

  // ROLE_INDIVIDUAL only
  {
    path: 'budgets',
    canActivate: [authGuard, individualGuard],
    loadComponent: () => import('./budgets/budget-list.component').then(m => m.BudgetListComponent)
  },
  {
    path: 'reports',
    canActivate: [authGuard, individualGuard],
    loadComponent: () => import('./reports/reports.component').then(m => m.ReportsComponent)
  },
  {
    path: 'insights',
    canActivate: [authGuard, individualGuard],
    loadComponent: () => import('./insights/insights.component').then(m => m.InsightsComponent)
  },

  { path: '**', redirectTo: 'login' }
];
