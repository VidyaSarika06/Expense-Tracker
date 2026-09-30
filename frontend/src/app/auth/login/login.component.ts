import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { SessionStorageService } from '../../services/session-storage.service';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  form: FormGroup;
  loading = false;
  error   = '';

  constructor(
    private fb:      FormBuilder,
    private auth:    AuthService,
    private session: SessionStorageService,
    private router:  Router
  ) {
    this.form = this.fb.group({
      email:    ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      // Role selection on login — drives UI rendering
      role:     ['ROLE_STUDENT', Validators.required]
    });
  }

  get f() { return this.form.controls; }

  submit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.loading = true;//login process running
    this.error   = '';


    const { email, password, role } = this.form.value;

    this.auth.login({ email, password }).subscribe({
      next: res => {
        this.session.setToken(res.token);
        this.session.setUserEmail(res.email);
        this.session.setRole(res.role);
        this.router.navigate(['/dashboard']);
        // Start session timeout (30 minutes)
        setTimeout(() => {
          sessionStorage.clear();
          alert('Session expired. Please login again.');
          this.router.navigate(['/login']);
        }, 30 * 60 * 1000);
      },
      error: err => {
        this.error   = err.error?.message ?? 'Invalid email or password';
        this.loading = false;
      }
    });
  }
}
