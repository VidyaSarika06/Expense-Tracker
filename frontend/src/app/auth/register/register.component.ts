import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  form: FormGroup;
  loading = false;
  error   = '';
  success = '';

  constructor(
    private fb:     FormBuilder,
    private auth:   AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      name:            ['', Validators.required],
      email:           ['', [Validators.required, Validators.email]],
      password:        ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', Validators.required],
      role:            ['ROLE_STUDENT', Validators.required]
    }, { validators: this.passwordMatch });
  }

  get f() { return this.form.controls; }

  passwordMatch(group: AbstractControl) {
    const pw  = group.get('password')?.value;
    const cpw = group.get('confirmPassword')?.value;
    return pw === cpw ? null : { mismatch: true };
  }

  submit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.loading = true;
    this.error   = '';

    const { confirmPassword, ...payload } = this.form.value;

    this.auth.register(payload).subscribe({
      next: res => {
        this.success = res.message;
        setTimeout(() => this.router.navigate(['/login']), 1500);
      },
      error: err => {
        this.error   = err.error?.message ?? 'Registration failed';
        this.loading = false;
      }
    });
  }
}
