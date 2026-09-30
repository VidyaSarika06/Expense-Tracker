import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { HeaderComponent }  from '../layout/header/header.component';
import { SidebarComponent } from '../layout/sidebar/sidebar.component';
import { FooterComponent }  from '../layout/footer/footer.component';
import { TransactionService, TransactionFilter } from '../services/transaction.service';
import { CategoryService } from '../services/category.service';
import { Transaction, Category, PageResponse } from '../models/models';

@Component({
  selector: 'app-transaction-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule,
            HeaderComponent, SidebarComponent, FooterComponent],
  templateUrl: './transaction-list.component.html',
  styleUrls: ['./transaction-list.component.scss']
})
export class TransactionListComponent implements OnInit {

  sidebarCollapsed = false;
  page: PageResponse<Transaction> = { content: [], totalElements: 0, totalPages: 0, size: 10, number: 0 };
  categories: Category[] = [];
  showModal  = false;
  editMode   = false;
  editId: number | null = null;
  error = '';

  // Filters
  filter: TransactionFilter = { page: 0, size: 10, sortBy: 'date', sortDir: 'desc' };

  form: FormGroup;
  today = new Date().toISOString().split('T')[0];

  constructor(
    private txSvc:  TransactionService,
    private catSvc: CategoryService,
    private fb:     FormBuilder
  ) {
    this.form = this.fb.group({
      amount:     [null, [Validators.required, Validators.min(0.01)]],
      type:       ['EXPENSE', Validators.required],
      date:       ['', Validators.required],
      note:       [''],
      categoryId: [null, Validators.required]
    });
  }

  ngOnInit(): void {
    this.catSvc.getAll().subscribe({ next: d => this.categories = d });
    this.load();
  }

  load(): void {
    this.txSvc.getAll(this.filter).subscribe({ next: d => this.page = d });
  }

  applyFilter(): void { this.filter.page = 0; this.load(); }
  clearFilter(): void {
    this.filter = { page: 0, size: 10, sortBy: 'date', sortDir: 'desc' };
    this.load();
  }

  goToPage(p: number): void { this.filter.page = p; this.load(); }

  get pages(): number[] {
    return Array.from({ length: this.page.totalPages }, (_, i) => i);
  }

  openCreate(): void {
    this.editMode = false; this.editId = null;
    this.form.reset({ type: 'EXPENSE' });
    this.error = ''; this.showModal = true;
  }

  openEdit(tx: Transaction): void {
    this.editMode = true; this.editId = tx.id!;
    this.form.patchValue({ amount: tx.amount, type: tx.type, date: tx.date, note: tx.note, categoryId: tx.categoryId });
    this.error = ''; this.showModal = true;
  }

  save(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    const payload = this.form.value as Transaction;
    const req = this.editMode
      ? this.txSvc.update(this.editId!, payload)
      : this.txSvc.create(payload);

    req.subscribe({
      next: () => { this.showModal = false; this.load(); },
      error: err => this.error = err.error?.message ?? 'Error saving transaction'
    });
  }

  delete(id: number): void {
    if (!confirm('Delete this transaction?')) return;
    this.txSvc.delete(id).subscribe({ next: () => this.load() });
  }
}
