
// import { Injectable } from '@angular/core';
// import { HttpClient } from '@angular/common/http';
// import { Observable, BehaviorSubject } from 'rxjs';
// import { tap } from 'rxjs/operators';
// import { jwtDecode } from 'jwt-decode';
// import { Router } from '@angular/router';

// export interface LoginResponse {
//   token: string;
//   email: string;
//   userId: number;
//   role: string;
//   firstName?: string;
//   lastName?: string;
// }

// export interface UserProfile {
//   userId: number;
//   firstName: string;
//   lastName: string;
//   email: string;
//   mobile: string;
//   address: string;
//   username: string;
// }

// @Injectable({
//   providedIn: 'root'
// })
// export class AuthService {
//   private apiUrl = 'http://localhost:8080/api/auth';
//   private currentUserSubject = new BehaviorSubject<UserProfile | null>(null);
//   public currentUser = this.currentUserSubject.asObservable();

//   constructor(
//     private http: HttpClient,
//     private router: Router
//   ) {
//     this.loadUserProfile();
//   }

//   private loadUserProfile(): void {
//     const token = this.getToken();
//     if (token) {
//       try {
//         const decoded: any = jwtDecode(token);
//         if (decoded.exp * 1000 > Date.now()) {
//           this.getUserProfile().subscribe();
//         } else {
//           this.logout();
//         }
//       } catch (error) {
//         this.logout();
//       }
//     }
//   }

//   login(email: string, password: string): Observable<LoginResponse> {
//     return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { email, password }).pipe(
//       tap(response => {
//         this.storeAuthData(response);
//         this.getUserProfile().subscribe();
//       })
//     );
//   }

//   register(userData: any): Observable<any> {
//     return this.http.post(`${this.apiUrl}/register`, userData);
//   }

//   logout(): void {
//     localStorage.removeItem('token');
//     localStorage.removeItem('role');
//     localStorage.removeItem('email');
//     localStorage.removeItem('userId');
//     this.currentUserSubject.next(null);
//     this.router.navigate(['/login']);
//   }

//   isLoggedIn(): boolean {
//     const token = this.getToken();
//     if (!token) return false;
//      console.log("in isloogedin",token);
//     try {
//       const decoded: any = jwtDecode(token);
//       return decoded.exp * 1000 > Date.now();
//     } catch (error) {
//       console.log("in error");
//       return false;
//     }
//   }

//   getToken(): string | null {
//     return localStorage.getItem('token');
//   }

//   getUserProfile(): Observable<UserProfile> {
//     return this.http.get<UserProfile>("http://localhost:8080/api/users/profile").pipe(
//       tap(profile => {
//         this.currentUserSubject.next(profile);
//       })
//     );
//   }

//   getCurrentUser(): UserProfile | null {
//     return this.currentUserSubject.value;
//   }

//   getUserId(): number | null {
//     const user = this.currentUserSubject.value;
//     return user ? user.userId : null;
//   }

//   getRole(): string | null {
//     return localStorage.getItem('role');
//   }

//   isAdmin(): boolean {
//     return this.getRole() === 'ADMIN';
//   }

//   updateProfile(profileData: any): Observable<UserProfile> {
//     return this.http.put<UserProfile>(`${this.apiUrl}/profile`, profileData).pipe(
//       tap(updatedProfile => {
//         this.currentUserSubject.next(updatedProfile);
//       })
//     );
//   }

//   changePassword(currentPassword: string, newPassword: string): Observable<any> {
//     return this.http.put(`${this.apiUrl}/change-password`, {
//       currentPassword,
//       newPassword
//     });
//   }

//   requestPasswordReset(email: string): Observable<any> {
//     return this.http.post(`${this.apiUrl}/forgot-password`, { email });
//   }

//   resetPassword(token: string, newPassword: string): Observable<any> {
//     return this.http.post(`${this.apiUrl}/reset-password`, {
//       token,
//       newPassword
//     });
//   }

//   private storeAuthData(authData: LoginResponse): void {
//     localStorage.setItem('token', authData.token);
//     localStorage.setItem('role', authData.role);
//     localStorage.setItem('email', authData.email);
//     localStorage.setItem('userId', authData.userId.toString());
//   }
// }


import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { jwtDecode } from 'jwt-decode';
import { Router } from '@angular/router';

export interface LoginResponse {
  token: string;
  email: string;
  userId: number;
  role: string;
  firstName?: string;
  lastName?: string;
}

export interface UserProfile {
  userId:number,
  firstName: string;
  lastName: string;
  email: string;
  mobile: string;
  address: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private currentUserSubject = new BehaviorSubject<UserProfile | null>(null);
  public currentUser = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.loadUserProfile();
  }

  private loadUserProfile(): void {
    const token = this.getToken();

    if (!token) {
      console.log('No token found');
      this.logout();
      return;
    }

    try {
      const decoded: any = jwtDecode(token);
      console.log('Decoded token:', decoded);

      // Check if token has expiration and if it's still valid
      if (!decoded.exp) {
        console.error('Token has no expiration claim');
        this.logout();
        return;
      }

      // Convert expiration time (seconds since epoch) to milliseconds
      const expirationTime = decoded.exp * 1000;
      const currentTime = Date.now();

      console.log(`Token expires at: ${new Date(expirationTime)}`);
      console.log(`Current time is: ${new Date(currentTime)}`);

      if (currentTime < expirationTime) {
        console.log('Token is valid, loading profile...');
        this.getUserProfile().subscribe({
          next: (profile) => console.log('Profile loaded successfully'),
          error: (err) => {
            console.error('Failed to load profile:', err);
            this.logout();
          }
        });
      } else {
        console.log('Token expired');
        this.logout();
      }
    } catch (error) {
      console.error('Error decoding token:', error);
      this.logout();
    }
  }

  login(email: string, password: string): Observable<LoginResponse> {
  return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { email, password }).pipe(
    tap(response => {
      this.storeAuthData(response);
      // Load profile with error handling
      this.getUserProfile().subscribe({
        error: (err) => {
          console.error('Profile load failed after login:', err);
          this.logout();
        }
      });
    })
  );
}
  register(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, userData);
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('email');
    localStorage.removeItem('userId');
    this.currentUserSubject.next(null);
    this.router.navigate(['']);
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;

    try {
      const decoded: any = jwtDecode(token);
      return decoded.exp * 1000 > Date.now();
    } catch (error) {
      return false;
    }
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }



  getUserProfile(): Observable<UserProfile> {
    const token = this.getToken();
    if (!token) {
      return throwError(() => new Error('No token available'));
    }

    return this.http.get<UserProfile>("http://localhost:8080/api/users/profile", {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      })
    }).pipe(
      tap(profile => {
        this.currentUserSubject.next(profile);
      }),
      catchError(error => {
        console.error('Profile load error:', error);
        if (error.status === 403) {
          this.logout(); // Force logout on 403
        }
        return throwError(() => error);
      })
    );
  }




  getCurrentUser(): UserProfile | null {
    return this.currentUserSubject.value;
  }

  getUserId(): number | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const decoded: any = jwtDecode(token);
      return decoded.userId || null;
    } catch (error) {
      return null;
    }
  }

  getRole(): string | null {
    return localStorage.getItem('role');
  }

  isAdmin(): boolean {
    return this.getRole() === 'ADMIN';
  }

// auth.service.ts
updateProfile(profileData: Partial<UserProfile>): Observable<UserProfile> {
  const token = this.getToken();
  if (!token) {
    return throwError(() => new Error('No authentication token found'));
  }

  return this.http.put<UserProfile>("http://localhost:8080/api/users/profile", profileData, {
    headers: new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    })
  });
}

  // auth.service.ts
changePassword(currentPassword: string, newPassword: string): Observable<any> {
  const token = this.getToken();
  if (!token) {
    return throwError(() => new Error('No authentication token found'));
  }

  return this.http.put("http://localhost:8080/api/users/profile/password", {
    currentPassword,
    newPassword
  }, {
    headers: new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    })
  });
}

  // resetPassword(token: string, newPassword: string): Observable<any> {
  //   return this.http.post(`${this.apiUrl}/reset-password`, {
  //     token,
  //     newPassword
  //   });
  // }

  private storeAuthData(authData: LoginResponse): void {
    localStorage.setItem('token', authData.token);
    localStorage.setItem('role', authData.role);
    localStorage.setItem('email', authData.email);
    localStorage.setItem('userId', authData.userId.toString());
  }
}
