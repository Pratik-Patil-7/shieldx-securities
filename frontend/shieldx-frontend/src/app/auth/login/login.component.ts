import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { AuthService } from 'src/app/core/services/auth.service';
import { LoginResponse } from 'src/app/models/LoginResponse';

// If LoginResponse is not defined elsewhere, define or import it appropriately

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

    email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin(form: NgForm) {
    if (form.valid) {
      this.authService.login(this.email, this.password).subscribe(
        (response: LoginResponse) => {
          localStorage.setItem('token', response.token);
          const role = (response.email?.toLowerCase().includes('admin')) ? 'ADMIN' : 'USER';
          localStorage.setItem('role', role);
          // this.router.navigate(['/dashboard']);
          console.log('Login successful:', response);
        },
        (error) => {
          this.errorMessage = 'Invalid email or password';
          console.error('Login error:', error); // Log for debugging
        }
      );
    }
  }

}
