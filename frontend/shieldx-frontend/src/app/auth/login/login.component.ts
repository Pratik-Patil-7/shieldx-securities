import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { AuthService } from 'src/app/core/services/auth.service';
import { LoginResponse } from 'src/app/models/LoginResponse';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  email: string = '';
  password: string = '';
  errorMessage: string = '';
  isLoading: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    // Check if user is already logged in with valid token
    if (this.authService.isLoggedIn()) {
      this.attemptAutoLogin();
    }
  }

  private attemptAutoLogin(): void {
    this.isLoading = true;
    this.errorMessage = '';

    // Try to get user profile to verify token is still valid
    this.authService.getUserProfile().subscribe({
      next: (profile) => {
        this.isLoading = false;
        this.router.navigate(['']); // Or your default authenticated route
      },
      error: (err) => {
        this.isLoading = false;
        this.authService.logout(); // Clear invalid token
        console.error('Auto-login failed:', err);
      }
    });
  }

  onLogin(form: NgForm): void {
    if (form.valid) {
      this.isLoading = true;
      this.errorMessage = '';

      this.authService.login(this.email, this.password).subscribe({
        next: (response: LoginResponse) => {
          this.handleSuccessfulLogin(response);
        },
        error: (error) => {
          this.handleLoginError(error);
        }
      });
    }
  }

  private handleSuccessfulLogin(response: LoginResponse): void {
    // Store the auth data (handled by AuthService)
    console.log('Login successful:', response);

    // Get user profile to complete login
    this.authService.getUserProfile().subscribe({
      next: (profile) => {
        this.isLoading = false;
        this.router.navigate(['']); // Redirect to main page
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Login successful but failed to load profile';
        console.error('Profile load error:', err);
      }
    });
  }

  private handleLoginError(error: any): void {
    this.isLoading = false;

    if (error.status === 401) {
      this.errorMessage = 'Invalid email or password';
    } else if (error.status === 0) {
      this.errorMessage = 'Unable to connect to server';
    } else {
      this.errorMessage = 'Login failed. Please try again later.';
    }

    console.error('Login error:', error);
  }
}
