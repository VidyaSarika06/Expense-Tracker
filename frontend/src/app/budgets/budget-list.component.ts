import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HeaderComponent }  from '../layout/header/header.component';
import { SidebarComponent } from '../layout/sidebar/sidebar.component';
import { FooterComponent }  from '../layout/footer/footer.component';
import { BudgetService }    from '../services/budget.service';
import { CategoryService }  from '../services/category.service';
import { Budget, Category } from '../models/models';

@Component({
  selector: 'app-budget-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule,
            HeaderComponent, SidebarComponent, FooterComponent],
  templateUrl: './budget-list.component.html',
  styleUrls: ['./budget-list.component.scss']
})
export class BudgetListComponent implements OnInit {

  sidebarCollapsed = false;
  budgets:    Budget[]   = [];
  categories: Category[] = [];
  showModal  = false;
  editMode   = false;
  editId: number | null = null;
  error = '';

  form: FormGroup;

  constructor(
    private svc:    BudgetService,
    private catSvc: CategoryService,
    private fb:     FormBuilder
  ) {
    this.form = this.fb.group({
      month:       ['', Validators.required],
      limitAmount: [null, [Validators.required, Validators.min(0.01)]],
      categoryId:  [null, Validators.required]
    });
  }

  ngOnInit(): void {
    this.catSvc.getAll().subscribe({ next: d => this.categories = d });
    this.load();
  }

  load(): void {
    this.svc.getAll().subscribe({ next: d => this.budgets = d });
  }

  openCreate(): void {
    this.editMode = false; this.editId = null;
    this.form.reset(); this.error = ''; this.showModal = true;
  }

  openEdit(b: Budget): void {
    this.editMode = true; this.editId = b.id!;
    this.form.patchValue({ month: b.month, limitAmount: b.limitAmount, categoryId: b.categoryId });
    this.error = ''; this.showModal = true;
  }

  save(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    const payload = this.form.value as Budget;
    const req = this.editMode
      ? this.svc.update(this.editId!, payload)
      : this.svc.create(payload);

    req.subscribe({
      next: () => { this.showModal = false; this.load(); },
      error: err => this.error = err.error?.message ?? 'Error saving budget'
    });
  }

  delete(id: number): void {
    if (!confirm('Delete this budget?')) return;
    this.svc.delete(id).subscribe({ next: () => this.load() });
  }
}
