import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HeaderComponent }  from '../layout/header/header.component';
import { SidebarComponent } from '../layout/sidebar/sidebar.component';
import { FooterComponent }  from '../layout/footer/footer.component';
import { CategoryService }  from '../services/category.service';
import { Category } from '../models/models';

@Component({
  selector: 'app-category-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule,
            HeaderComponent, SidebarComponent, FooterComponent],
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.scss']
})
export class CategoryListComponent implements OnInit {

  sidebarCollapsed = false;
  categories: Category[] = [];
  showModal  = false;
  editMode   = false;
  editId: number | null = null;
  error = '';

  form: FormGroup;

  constructor(private svc: CategoryService, private fb: FormBuilder) {
    this.form = this.fb.group({
      name:        ['', Validators.required],
      description: ['']
    });
  }

  ngOnInit(): void { this.load(); }

  load(): void {
    this.svc.getAll().subscribe({ next: data => this.categories = data });
  }

  openCreate(): void {
    this.editMode = false;
    this.editId   = null;
    this.form.reset();
    this.showModal = true;
  }

  openEdit(cat: Category): void {
    this.editMode = true;
    this.editId   = cat.id!;
    this.form.patchValue({ name: cat.name, description: cat.description });
    this.showModal = true;
  }

  save(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    const payload = this.form.value as Category;

    const req = this.editMode
      ? this.svc.update(this.editId!, payload)
      : this.svc.create(payload);

    req.subscribe({
      next: () => { this.showModal = false; this.load(); },
      error: err => this.error = err.error?.message ?? 'Error saving category'
    });
  }

  delete(id: number): void {
    if (!confirm('Delete this category?')) return;
    this.svc.delete(id).subscribe({ next: () => this.load() });
  }
}
