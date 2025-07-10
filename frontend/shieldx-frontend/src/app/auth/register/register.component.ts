import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  firstName: string = '';
  lastName: string = '';
  username: string = '';
  email: string = '';
  mobile: string = '';
  address: string = '';
  password: string = '';
  confirmPassword: string = '';
  terms: boolean = false;
  errorMessage: string = '';
  successMessage: string = ''; // Added to fix undefined variable

  constructor(private authService: AuthService, private router: Router) {} // Fixed service name to match injection

  onRegister(form: NgForm) {
    // Validate form
    if (form.invalid) {
      this.errorMessage = 'Please fill all required fields correctly';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match';
      return;
    }

    if (!this.terms) {
      this.errorMessage = 'You must accept the terms and conditions';
      return;
    }

    // Prepare registration data
    const registrationData = {
      firstName: this.firstName,
      lastName: this.lastName,
      username: this.username,
      email: this.email,
      mobile: this.mobile,
      address: this.address,
      password: this.password
    };

    // Call registration service
    this.authService.register(registrationData).subscribe({
  next: (response) => {
    localStorage.setItem('token', response.token);
    const role = (response.email?.toLowerCase().includes('admin')) ? 'ADMIN' : 'USER';
    localStorage.setItem('role', role);
    localStorage.setItem('email', response.email);
    localStorage.setItem('userId', response.userId);
    this.successMessage = 'Registration successful! Redirecting to dashboard...';
    setTimeout(() => this.router.navigate(['']), 2000);
    form.resetForm();
    this.errorMessage = '';
  },
  error: (error) => {
    if (error.error === 'Email already exists') {
      this.errorMessage = 'This email address is already registered. Please use a different email or login.';
    } else {
      this.errorMessage = 'Registration failed. Please try again.';
    }
    console.error('Registration error:', error);
  }
});
  }
}
