import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { SessionStorageService } from '../../services/session-storage.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  @Output() toggleSidebar = new EventEmitter<void>();

  // Getters read from sessionStorage on every change-detection cycle
  get email(): string { return this.session.getUserEmail() ?? ''; }
  get role():  string { return this.session.getRole() ?? ''; }

  get roleLabel(): string {
    return this.role === 'ROLE_INDIVIDUAL' ? 'Individual' : 'Student';
  }

  get roleBadgeClass(): string {
    return this.role === 'ROLE_INDIVIDUAL' ? 'badge-individual' : 'badge-student';
  }

  constructor(private session: SessionStorageService, private router: Router) {}

  logout(): void {
    this.session.clear();
    this.router.navigate(['/login']);
  }
}
