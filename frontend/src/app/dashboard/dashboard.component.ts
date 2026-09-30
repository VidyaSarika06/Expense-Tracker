import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgChartsModule } from 'ng2-charts';
import { ChartData, ChartOptions } from 'chart.js';
import { HeaderComponent }  from '../layout/header/header.component';
import { SidebarComponent } from '../layout/sidebar/sidebar.component';
import { FooterComponent }  from '../layout/footer/footer.component';
import { DashboardService } from '../services/dashboard.service';
import { TransactionService } from '../services/transaction.service';
import { SessionStorageService } from '../services/session-storage.service';
import { MonthlySummary, BudgetAlert, Transaction } from '../models/models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, NgChartsModule,
            HeaderComponent, SidebarComponent, FooterComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  sidebarCollapsed = false;
  role  = this.session.getRole() ?? '';
  month = new Date().toISOString().slice(0, 7); // yyyy-MM

  summary: MonthlySummary | null = null;
  alerts:  BudgetAlert[]  = [];
  recentTransactions: Transaction[] = [];
  loading = true;

  get isIndividual(): boolean { return this.role === 'ROLE_INDIVIDUAL'; }
  get isStudent():    boolean { return this.role === 'ROLE_STUDENT'; }

  // ── Pie Chart ──────────────────────────────────────────────────────────────
  pieData: ChartData<'pie'> = { labels: [], datasets: [{ data: [] }] };
  /*pieOptions: ChartOptions<'pie'> = {
    responsive: true,
    plugins: { legend: { position: 'bottom' } }
  };
  pieOptions: ChartOptions<'pie'> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom'
      }
    }
  };*/
// Individual Dashboard
/*pieOptions: ChartOptions<'pie'> = {
  responsive: true,
  plugins: {
    legend: {
      position: 'bottom'
    }
  }
};*/
pieOptions: ChartOptions<'pie'> = {
  responsive: true,
  plugins: {
    legend: {
      position: 'bottom'
    }
  },
  elements: {
    arc: {
      hoverOffset: 3
    }
  }
};

// Student Dashboard
/*studentPieOptions: ChartOptions<'pie'> = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'bottom'
    }
  }
};*/
studentPieOptions: ChartOptions<'pie'> = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'bottom'
    }
  },
  elements: {
    arc: {
      hoverOffset: 3
    }
  }
};



  // ── Bar Chart (Individual only) ────────────────────────────────────────────
  barData: ChartData<'bar'> = {
    labels: [],
    datasets: [
      { label: 'Income',  data: [], backgroundColor: '#10b981' },
      { label: 'Expense', data: [], backgroundColor: '#ef4444' }
    ]
  };
  barOptions: ChartOptions<'bar'> = {
    responsive: true,
    plugins: { legend: { position: 'top' } },
    scales: { y: { beginAtZero: true } }
  };

  constructor(
    private dashSvc:  DashboardService,
    private txSvc:    TransactionService,
    private session:  SessionStorageService
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;

    this.dashSvc.getMonthlySummary(this.month).subscribe({
      next: data => {
        this.summary = data;
        this.buildPieChart(data);
        if (this.isIndividual) this.buildBarChart(data);
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });

    this.dashSvc.getBudgetAlerts(this.month).subscribe({
      next: data => this.alerts = data
    });

    this.txSvc.getAll({ page: 0, size: 5, sortBy: 'date', sortDir: 'desc' }).subscribe({
      next: page => this.recentTransactions = page.content
    });
  }

  private buildPieChart(data: MonthlySummary): void {
    this.pieData = {
      labels: data.expenseByCategory.map(c => c.category),
      datasets: [{
        data: data.expenseByCategory.map(c => c.amount),
        backgroundColor: ['#4f46e5','#10b981','#f59e0b','#ef4444','#3b82f6','#8b5cf6','#ec4899'],
        hoverBackgroundColor: ['#4f46e5','#10b981','#f59e0b','#ef4444','#3b82f6','#8b5cf6','#ec4899'],
        borderColor: '#ffffff',
        hoverBorderColor: '#ffffff'
      }]
    };
  }

  private buildBarChart(data: MonthlySummary): void {
    this.barData = {
      labels: data.monthlyTrend.map(t => t.month),
      datasets: [
        { label: 'Income',  data: data.monthlyTrend.map(t => t.income),  backgroundColor: '#10b981' },
        { label: 'Expense', data: data.monthlyTrend.map(t => t.expense), backgroundColor: '#ef4444' }
      ]
    };
  }

  get firstAlertBudget(): number {
    return this.alerts.length ? this.alerts[0].budget : 0;
  }

  get savings(): number {
    if (!this.summary) return 0;
    return this.summary.totalIncome - this.summary.totalExpense;
  }

  get remainingBudget(): number {
    if (!this.alerts.length || !this.summary) return 0;
    const totalBudget = this.alerts.reduce((s, a) => s + a.budget, 0);
    // summary.totalExpense is always a number here (guarded above), no ?? needed
    return totalBudget - this.summary.totalExpense;
  }

  get topCategory(): string {
    if (!this.summary?.expenseByCategory?.length) {
      return 'N/A';
    }

    return this.summary.expenseByCategory
      .sort((a, b) => b.amount - a.amount)[0].category;
  }
}
