import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { jwtDecode } from 'jwt-decode'; // Ensure this is installed

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  userSubject = new BehaviorSubject<any>({
    firstName: '',
    lastName: '',
    email: '',
    mobile: '',
    address: ''
  });

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.authService.currentUser.subscribe(user => {
      if (user) {
        this.userSubject.next(user);
      }
    });
  }

  isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    if (!token) {
      console.log('No token found in localStorage');
      return false;
    }
    try {
      const decoded = jwtDecode(token);
      return !!decoded.exp && new Date(decoded.exp * 1000) > new Date(); // Check if token is not expired
    } catch (e) {
      console.error('Token decoding error:', e);
      return false;
    }
  }

  get userName(): string {
    const user = this.userSubject.value;
    const firstName = user?.firstName || '';
    const lastName = user?.lastName || '';
    const email = localStorage.getItem('email') || '';
    return (firstName + ' ' + lastName).trim() || (email.split('@')[0] || 'User');
  }

  get dashboardRoute(): string[] {
    const role = localStorage.getItem('role'); // Use localStorage since no AuthGuard
    return role === 'ADMIN' ? ['/admin'] : ['/user-dashboard']; // Adjusted to match lazy-loaded paths
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
