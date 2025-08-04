import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/users/profile';

  constructor(private http: HttpClient, private authService: AuthService) { }

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    if (!token) throw new Error('No token available');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  getProfile(): Observable<any> {
    return this.http.get(this.apiUrl, { headers: this.getAuthHeaders() });
  }

  updateProfile(profileData: any): Observable<any> {
    return this.http.put(this.apiUrl, profileData, {
      headers: this.getAuthHeaders()
    });
  }

  updatePassword(passwordData: { currentPassword: string, newPassword: string }): Observable<{ message: string }> {
    return this.http.put<{ message: string }>(
      `${this.apiUrl}/password`,
      passwordData,
      {
        headers: this.getAuthHeaders()
      }
    );
  }
}

