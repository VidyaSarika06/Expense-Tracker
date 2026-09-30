import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HeaderComponent }  from '../layout/header/header.component';
import { SidebarComponent } from '../layout/sidebar/sidebar.component';
import { FooterComponent }  from '../layout/footer/footer.component';
import { DashboardService } from '../services/dashboard.service';
import { MonthlySummary, BudgetAlert, CategoryExpense } from '../models/models';

@Component({
  selector: 'app-insights',
  standalone: true,
  imports: [CommonModule, FormsModule,
            HeaderComponent, SidebarComponent, FooterComponent],
  templateUrl: './insights.component.html',
  styleUrls: ['./insights.component.scss']
})
export class InsightsComponent implements OnInit {

  sidebarCollapsed = false;
  month   = new Date().toISOString().slice(0, 7);
  summary: MonthlySummary | null = null;
  alerts:  BudgetAlert[]  = [];
  loading = false;

  constructor(private dashSvc: DashboardService) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.dashSvc.getMonthlySummary(this.month).subscribe({
      next: d => { this.summary = d; this.loading = false; },
      error: ()  => { this.loading = false; }
    });
    this.dashSvc.getBudgetAlerts(this.month).subscribe({
      next: d => this.alerts = d
    });
  }

  get highestCategory(): CategoryExpense | null {
    if (!this.summary || !this.summary.expenseByCategory.length) return null;
    return [...this.summary.expenseByCategory].sort((a, b) => b.amount - a.amount)[0];
  }

  get avgMonthlyExpense(): number {
    if (!this.summary || !this.summary.monthlyTrend.length) return 0;
    const total = this.summary.monthlyTrend.reduce((s, t) => s + t.expense, 0);
    return total / this.summary.monthlyTrend.length;
  }

  get savingsRate(): number {
    if (!this.summary || this.summary.totalIncome === 0) return 0;
    return ((this.summary.totalIncome - this.summary.totalExpense) / this.summary.totalIncome) * 100;
  }

  // Returns count of categories that exceeded their budget this month
  get overrunCount(): number {
    return this.alerts.length;
  }

getWidth(value: number, income: number, expense: number): number {
  const max = Math.max(income, expense, 1);
  return (value / max) * 100;
}

}
