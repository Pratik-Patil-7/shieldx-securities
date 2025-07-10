import { Component } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  constructor(private authService: AuthService, private router: Router) {}

  // Check authentication state
  isAuthenticated(): boolean {
    return !!localStorage.getItem('token'); // Assumes token exists when authenticated
  }

  // Get user name (e.g., from email or a user object)
  get userName(): string {
    const email = localStorage.getItem('email') || localStorage.getItem('username');
    return email?.split('@')[0] || 'User'; // Extracts name before @ or defaults to 'User'
  }

  // Logout function
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('email'); // Remove email if stored
    this.router.navigate(['/login']);
  }

}
