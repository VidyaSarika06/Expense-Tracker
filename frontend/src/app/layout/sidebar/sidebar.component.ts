import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { SessionStorageService } from '../../services/session-storage.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {

  @Input() collapsed = false;

  // Getter reads from sessionStorage on every change-detection cycle,
  // ensuring the sidebar always reflects the current session role.
  get isIndividual(): boolean {
    return this.session.getRole() === 'ROLE_INDIVIDUAL';
  }

  constructor(private session: SessionStorageService) {}
}
