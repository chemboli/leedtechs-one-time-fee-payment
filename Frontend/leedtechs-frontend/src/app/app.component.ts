import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './components/shared/header/header.component';
import { ToastComponent } from './components/shared/toast/toast.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, ToastComponent],
  template: `
    <div class="app-shell">
      <app-header />
      <main class="container">
        <router-outlet />
      </main>
      <app-toast />
    </div>
  `,
  styles: [`
    .app-shell { position: relative; z-index: 1; }
    .container {
      max-width: 960px;
      margin: 0 auto;
      padding: 0 24px 80px;
    }
  `]
})
export class AppComponent {}
