import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  standalone: true,
  template: `
    <footer class="footer">
      <span>© {{ year }} Expense Tracker. All rights reserved.</span>
    </footer>
  `,
  styles: [`
    .footer {
      text-align: center;
      padding: 16px;
      font-size: 12px;
      color: var(--text-muted);
      border-top: 1px solid var(--border);
      background: var(--surface);
    }
  `]
})
export class FooterComponent {
  year = new Date().getFullYear();
}
