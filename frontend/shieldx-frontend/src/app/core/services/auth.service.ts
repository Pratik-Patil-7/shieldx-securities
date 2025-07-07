import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {jwtDecode} from 'jwt-decode';
import { LoginResponse } from 'src/app/models/LoginResponse';
// Define the interface for the login response

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, { email, password });
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('email');
    localStorage.removeItem('userId');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUserId(): number | null {
    const token = this.getToken();
    if (token) {
      const decoded: { sub: string } = jwtDecode(token);
      return parseInt(decoded.sub, 10);
    }
    return null;
  }

  getCurrentUser(): any {
    const userId = this.getUserId();
    if (userId) {
      return { id: userId, username: 'User' + userId, role: 'USER' };
    }
    return null;
  }
}
