import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HeaderComponent }  from '../layout/header/header.component';
import { SidebarComponent } from '../layout/sidebar/sidebar.component';
import { FooterComponent }  from '../layout/footer/footer.component';
import { TransactionService } from '../services/transaction.service';
import { DashboardService }   from '../services/dashboard.service';
import { ReportService }      from '../services/report.service';
import { Transaction, MonthlySummary } from '../models/models';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule, FormsModule,
            HeaderComponent, SidebarComponent, FooterComponent],
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.scss']
})
export class ReportsComponent implements OnInit {

  sidebarCollapsed = false;
  month = new Date().toISOString().slice(0, 7);
  summary: MonthlySummary | null = null;
  transactions: Transaction[] = [];
  loading = false;

  constructor(
    private txSvc:      TransactionService,
    private dashSvc:    DashboardService,
    private reportSvc:  ReportService
  ) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.dashSvc.getMonthlySummary(this.month).subscribe({
      next: d  => { this.summary = d; this.loading = false; },
      error: () => { this.loading = false; }
    });
    // Load all transactions for the selected month for export
    this.txSvc.getAll({
      startDate: `${this.month}-01`,
      endDate:   `${this.month}-31`,
      size: 1000
    }).subscribe({ next: p => this.transactions = p.content });
  }

  exportCSV():   void { this.reportSvc.exportCSV(this.transactions,   `report-${this.month}`); }
  
}
