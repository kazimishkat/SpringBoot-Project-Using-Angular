import { Component, signal } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { Header } from './components/shared/layout/header/header';
import { Navbar } from './components/shared/layout/navbar/navbar';
import { Footer } from './components/shared/layout/footer/footer';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, Navbar, Footer],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('PharmacyManagementAngular');

  constructor(private router: Router) {}

  isDashboardRoute(): boolean {
    return this.router.url.startsWith('/dashboard');
  }
}

