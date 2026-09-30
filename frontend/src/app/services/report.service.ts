import { Injectable } from '@angular/core';
import { Transaction } from '../models/models';

@Injectable({ providedIn: 'root' })
export class ReportService {

  /** Export transactions array to CSV and trigger browser download */
  exportCSV(transactions: Transaction[], filename = 'transactions'): void {
    const headers = ['ID', 'Amount', 'Type', 'Date', 'Category', 'Note'];
    const rows = transactions.map(t => [
      t.id, t.amount, t.type, t.date, t.categoryName ?? '', t.note ?? ''
    ]);
    const csvContent = [headers, ...rows]
      .map(row => row.map(v => `"${v}"`).join(','))
      .join('\n');
    this.download(csvContent, `${filename}.csv`, 'text/csv');
  }

  /** Export transactions to JSON (acts as Excel-importable format) */
  exportExcel(transactions: Transaction[], filename = 'transactions'): void {
    const json = JSON.stringify(transactions, null, 2);
    this.download(json, `${filename}.json`, 'application/json');
  }

  /** Export transactions to plain-text PDF-like format */
  exportPDF(transactions: Transaction[], filename = 'transactions'): void {
    const lines = transactions.map(t =>
      `${t.date} | ${t.type} | ${t.categoryName} | ₹${t.amount} | ${t.note ?? ''}`
    );
    const content = ['Expense Tracker Report', '='.repeat(60), ...lines].join('\n');
    this.download(content, `${filename}.txt`, 'text/plain');
  }

  private download(content: string, filename: string, mimeType: string): void {
    const blob = new Blob([content], { type: mimeType });
    const url  = URL.createObjectURL(blob);
    const a    = document.createElement('a');
    a.href     = url;
    a.download = filename;
    a.click();
    URL.revokeObjectURL(url);
  }
}
