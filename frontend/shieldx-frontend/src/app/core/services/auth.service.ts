import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, BehaviorSubject, throwError, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { jwtDecode } from 'jwt-decode';
import { Router } from '@angular/router';

export interface LoginResponse {
  token: string;
  email: string;
  userId: number;
  role?: string;
  firstName?: string;
  lastName?: string;
}

export interface UserProfile {
  userId: number;
  firstName: string;
  lastName: string;
  email: string;
  mobile: string;
  address: string;
}

interface TempAuthData {
  token: string;
  email: string;
}

interface PasswordChangeResponse {
  message: string;
  success: boolean;
}

interface OtpResponse {
  message: string;
  verified?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private currentUserSubject = new BehaviorSubject<UserProfile | null>(null);
  public currentUser = this.currentUserSubject.asObservable();
  private tempAuthDataSubject = new BehaviorSubject<TempAuthData | null>(null);

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.loadUserProfile();
    // Restore temp auth data from sessionStorage on initialization
    const tempAuthData = window.sessionStorage.getItem('tempAuthData');
    if (tempAuthData) {
      try {
        const parsed = JSON.parse(tempAuthData);
        if (this.isValidJwt(parsed.token)) {
          console.log('Restored temp auth token:', parsed.token); // Debug
          this.tempAuthDataSubject.next(parsed);
        } else {
          console.warn('Invalid token during restore:', parsed.token);
          this.clearTempAuthData();
        }
      } catch (error) {
        console.warn('Failed to parse temp auth data from sessionStorage:', error);
        this.clearTempAuthData();
      }
    }
  }

  isauthenticated(): boolean {
    const token = this.getToken();
    if (!token) return false;
    try {
      const decoded: any = jwtDecode(token);
      return decoded.exp * 1000 > Date.now();
    } catch {
      return false;
    }
  }

  // Initialize auth state
  private loadUserProfile(): void {
    const token = this.getToken();
    if (!token) {
      this.logout();
      return;
    }

    try {
      const decoded: any = jwtDecode(token);
      if (!decoded.exp || decoded.exp * 1000 < Date.now()) {
        this.logout();
        return;
      }
      this.getUserProfile().subscribe({
        error: () => this.logout()
      });
    } catch (error) {
      this.logout();
    }
  }

  // Authentication methods
  login(email: string, password: string): Observable<{ requiresOtp: boolean }> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { email, password }).pipe(
      tap(response => {
        console.log('Login response token:', response.token); // Debug token
        if (!this.isValidJwt(response.token)) {
          console.error('Invalid JWT format:', response.token);
          throw new Error('Invalid token received from login');
        }
        const tempAuthData = {
          token: response.token,
          email: response.email // Use email from response to ensure consistency
        };
        console.log('Storing temp auth data:', tempAuthData); // Debug
        this.tempAuthDataSubject.next(tempAuthData);
        window.sessionStorage.setItem('tempAuthData', JSON.stringify(tempAuthData));
      }),
      map(() => ({ requiresOtp: true })),
      catchError(error => {
        this.clearTempAuthData();
        return throwError(() => ({
          message: error.message || 'Login failed. Please check your credentials.'
        }));
      })
    );
  }

  verifyOtp(otpCode: string): Observable<{ token: string }> {
    const tempAuthData = this.tempAuthDataSubject.value;
    if (!tempAuthData) {
      console.error('No temp auth data available');
      return throwError(() => new Error('No pending authentication'));
    }

    console.log('Verifying OTP with token:', tempAuthData.token); // Debug
    if (!this.isValidJwt(tempAuthData.token)) {
      console.error('Invalid temp token:', tempAuthData.token);
      this.clearTempAuthData();
      return throwError(() => new Error('Invalid temporary token'));
    }

    return this.http.post(
      `${this.apiUrl}/verify-otp`,
      {
        email: tempAuthData.email,
        otpCode: otpCode
      },
      {
        responseType: 'text' // Expect plain text response
      }
    ).pipe(
      map(response => {
        console.log('Verify OTP response:', response); // Debug
        if (response.includes('OTP verified successfully')) {
          return { token: tempAuthData.token };
        }
        throw new Error('Invalid OTP');
      }),
      tap(response => {
        this.storeAuthData(response.token);
        this.clearTempAuthData();
        this.loadUserProfile();
      }),
      catchError(error => {
        console.error('Verify OTP error:', error); // Debug
        return throwError(() => ({
          message: error.message || 'Invalid OTP'
        }));
      })
    );
  }

  // Restore temp auth data
  restoreTempAuthData(email: string, token: string): void {
    if (this.isValidJwt(token)) {
      const tempAuthData = { email, token };
      console.log('Restoring temp auth data:', tempAuthData); // Debug
      this.tempAuthDataSubject.next(tempAuthData);
      window.sessionStorage.setItem('tempAuthData', JSON.stringify(tempAuthData));
    } else {
      console.warn('Invalid token during restore:', token);
    }
  }

  // Get temp auth data
  getTempAuthData(): TempAuthData | null {
    return this.tempAuthDataSubject.value;
  }

  // Validate JWT format
  private isValidJwt(token: string): boolean {
    if (!token) return false;
    const parts = token.split('.');
    return parts.length === 3; // JWT should have 3 parts: header, payload, signature
  }

  // User management
  register(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, userData);
  }

  logout(): void {
    this.clearAuthData();
    this.router.navigate(['/login']);
  }



  // Profile methods
  getUserProfile(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`http://localhost:8080/api/users/profile`, {
      headers: this.getAuthHeaders()
    }).pipe(
      tap(profile => this.currentUserSubject.next(profile)),
      catchError(error => {
        if (error.status === 403) this.logout();
        return throwError(() => error);
      })
    );
  }

  updateProfile(profileData: Partial<UserProfile>): Observable<UserProfile> {
    return this.http.put<UserProfile>(`http://localhost:8080/api/users/profile`, profileData, {
      headers: this.getAuthHeaders()
    });
  }

  // Password methods
  changePassword(currentPassword: string, newPassword: string): Observable<PasswordChangeResponse> {
    return this.http.put<PasswordChangeResponse>(
      `http://localhost:8080/api/users/profile/password`,
      { currentPassword, newPassword },
      { headers: this.getAuthHeaders() }
    );
  }

  forgotPassword(email: string): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${this.apiUrl}/forgot-password`, { email });
  }

  resetPassword(email: string, token: string, newPassword: string): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(
      `${this.apiUrl}/reset-password`,
      { email, token, newPassword }
    );
  }

  // OTP methods
  sendOtp(email: string): Observable<OtpResponse> {
    return this.http.post<OtpResponse>(`${this.apiUrl}/send-otp`, { email }).pipe(
      catchError(error => {
        if (error.status === 200) {
          return of({ message: 'OTP sent successfully' });
        }
        return throwError(() => error);
      })
    );
  }

  // Utility methods
  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;

    try {
      const decoded: any = jwtDecode(token);
      return decoded.exp * 1000 > Date.now();
    } catch {
      return false;
    }
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getCurrentUser(): UserProfile | null {
    return this.currentUserSubject.value;
  }

  getUserId(): number | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const decoded: any = jwtDecode(token);
      return decoded.sub || null; // Use 'sub' as per backend JWT
    } catch (error) {
      return null;
    }
  }

  getRole(): string | null {
    return localStorage.getItem('role');
  }

  // Existing AuthService code remains largely the same, but update isAdmin
  isAdmin(): boolean {
    return this.getRole() === 'ADMIN';
  }

  // Private helpers
  public storeAuthData(token: string): void {
    try {
      const decoded: any = jwtDecode(token);
      localStorage.setItem('token', token);
      localStorage.setItem('email', decoded.email || '');
      localStorage.setItem('userId', decoded.sub.toString()); // Use 'sub' as per backend JWT
      localStorage.setItem('role', decoded.role || ''); // Role may not exist in JWT
    } catch (error) {
      console.error('Failed to decode token:', error);
      throw new Error('Invalid authentication token');
    }
  }

  public clearAuthData(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('email');
    localStorage.removeItem('userId');
    localStorage.removeItem('role');
    this.currentUserSubject.next(null);
  }

  public clearTempAuthData(): void {
    console.log('Clearing temp auth data'); // Debug
    this.tempAuthDataSubject.next(null);
    window.sessionStorage.removeItem('tempAuthData');
  }

  private getAuthHeaders(token?: string): HttpHeaders {
    const authToken = token || this.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${authToken}`,
      'Content-Type': 'application/json'
    });
  }

  isTokenValid(token: string): boolean {
    try {
      const decoded: any = jwtDecode(token);
      return decoded.exp * 1000 > Date.now();
    } catch {
      return false;
    }
  }

  getValidTokenOrThrow(): string {
  const token = this.getToken();
  if (!token || !this.isTokenValid(token)) {
    this.clearAuthData();
    throw new Error('Invalid or expired token');
  }
  return token;
}


getValidToken(): string {
  const token = localStorage.getItem('token');
  if (!token) {
    console.error('No token found in localStorage');
    throw new Error('No token found');
  }
  try {
    const decoded: any = jwtDecode(token);
    console.log('Token payload:', decoded);
    const isExpired = decoded.exp * 1000 < Date.now();
    if (isExpired) {
      console.error('Token expired:', decoded.exp);
      this.clearAuthData();
      throw new Error('Token expired');
    }
    return token;
  } catch (error) {
    console.error('Invalid token:', error);
    this.clearAuthData();
    throw new Error('Invalid token');
  }
}




}
