//

import { HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { catchError, Observable, throwError } from 'rxjs';
import { map } from 'rxjs/operators';
import { SecurityTypeResponse } from 'src/app/dto/security-type-response';
import { BookingResponse } from '../../dto/booking-response';
import { BouncerRequest } from '../../dto/bouncer-request';
import { DashboardStatsResponse } from '../../dto/dashboard-stats-response';
import { JobApplicationResponse } from '../../dto/job-application-response';
import { SecurityTypeRequest } from '../../dto/security-type-request';
import { UserResponse } from '../../dto/user-response';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = 'http://localhost:8080/api/admin';

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService
  ) { }

  // Dashboard Stats
  getDashboardStats(): Observable<DashboardStatsResponse> {
    return this.http.get<DashboardStatsResponse>(`${this.apiUrl}/dashboard`, { headers: this.getAuthHeaders() });
  }

  // Users
  getAllUsers(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(`${this.apiUrl}/users`, { headers: this.getAuthHeaders() });
  }

  // getBookingDetails(bookingId: number): Observable<BookingResponse> {
  //   return this.http.get<BookingResponse>(`${this.apiUrl}/bookings/${bookingId}`, { headers: this.getAuthHeaders() })
  //     .pipe(catchError(this.handleError.bind(this)));
  // }

  // rejectBooking(bookingId: number): Observable<string> {
  //   return this.http.put<string>(`${this.apiUrl}/bookings/${bookingId}/reject`, null, { headers: this.getAuthHeaders() })
  //     .pipe(catchError(this.handleError.bind(this)));
  // }

    getAllBookings(): Observable<BookingResponse[]> {
    return this.http.get<BookingResponse[]>(`${this.apiUrl}/bookings`, {
      headers: this.getAuthHeaders()
    });
  }

  approveBooking(bookingId: number): Observable<string> {
    return this.http.put<string>(
      `${this.apiUrl}/bookings/${bookingId}/approve`,
      {},
      { headers: this.getAuthHeaders() }
    );
  }

  rejectBooking(bookingId: number): Observable<string> {
    return this.http.put<string>(
      `${this.apiUrl}/bookings/${bookingId}/reject`,
      {},
      { headers: this.getAuthHeaders() }
    );
  }

  assignBouncer(bookingId: number, bouncerId: number): Observable<string> {
    return this.http.put<string>(
      `${this.apiUrl}/bookings/${bookingId}/assign`,
      { bouncerId },
      { headers: this.getAuthHeaders() }
    );
  }

  getBookingDetails(bookingId: number): Observable<BookingResponse> {
    return this.http.get<BookingResponse>(
      `${this.apiUrl}/bookings/${bookingId}`,
      { headers: this.getAuthHeaders() }
    );
  }

  getAllUsersWithStatus(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(`${this.apiUrl}/userswithstatus`, { headers: this.getAuthHeaders() });
  }

  getUserDetails(userId: number): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.apiUrl}/users/${userId}`, { headers: this.getAuthHeaders() });
  }

  deactivateUser(userId: number): Observable<string> {
    return this.http.put<string>(
      `${this.apiUrl}/users/${userId}`,
      {},
      { headers: this.getAuthHeaders(), withCredentials: true }
    ).pipe(
      catchError((error: HttpErrorResponse) => {
        this.handleAuthError(error);
        return throwError(() => error);
      })
    );
  }

  toggleUserStatus(userId: number): Observable<UserResponse> {
    return this.http.put<UserResponse>(
      `${this.apiUrl}/users/${userId}/toggle-status`,
      {},
      { headers: this.getAuthHeaders(), withCredentials: true }
    ).pipe(
      catchError((error: HttpErrorResponse) => {
        this.handleAuthError(error);
        return throwError(() => error);
      })
    );
  }

  // Security Staff
  getAllApplications(): Observable<JobApplicationResponse[]> {
    return this.http.get<JobApplicationResponse[]>(`${this.apiUrl}/applications`, { headers: this.getAuthHeaders() });
  }

  approveApplication(applicationId: number): Observable<string> {
    return this.http.put<string>(`${this.apiUrl}/applications/${applicationId}/approve`, null, { headers: this.getAuthHeaders() });
  }

  rejectApplication(applicationId: number): Observable<string> {
    return this.http.put<string>(`${this.apiUrl}/applications/${applicationId}/reject`, null, { headers: this.getAuthHeaders() });
  }


  addBouncer(request: BouncerRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/bouncers`, request, {
      headers: this.getAuthHeaders(),
      responseType: 'text', // Expect plain text response
      observe: 'response' // Get full response to debug
    }).pipe(
      catchError(this.handleError.bind(this)),
      map((response: HttpResponse<string>) => {
        console.log('Raw response:', response); // Debug log
        return response.body || 'Bouncer added successfully';
      })
    );
  }

  public handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An error occurred. Please try again later.';
    if (error.status === 401 || error.status === 403) {
      errorMessage = 'Session expired or unauthorized. Please login again.';
      this.authService.logout();
      this.router.navigate(['/login']);
    } else if (error.status === 400) {
      errorMessage = error.error?.message || 'Validation error. Please check all fields.';
    } else if (error.status === 404) {
      errorMessage = error.error?.message || 'Resource not found.';
    } else if (error.status >= 500) {
      errorMessage = error.error?.message || 'Server error. Please try again later.';
    }
    this.toastr.error(errorMessage, 'Error');
    return throwError(() => new Error(errorMessage));
  }

  // Bookings
  // getAllBookings(): Observable<BookingResponse[]> {
  //   return this.http.get<BookingResponse[]>(`${this.apiUrl}/bookings`, { headers: this.getAuthHeaders() });
  // }

  // approveBooking(bookingId: number): Observable<string> {
  //   return this.http.put<string>(`${this.apiUrl}/bookings/${bookingId}/approve`, { headers: this.getAuthHeaders() });
  // }

  // assignBouncer(bookingId: number, bouncerId: number): Observable<string> {
  //   return this.http.put<string>(`${this.apiUrl}/bookings/${bookingId}/assign`, { bouncerId }, { headers: this.getAuthHeaders() });
  // }

  // Security Types
  addSecurityType(request: SecurityTypeRequest): Observable<SecurityTypeResponse> {
    return this.http.post<SecurityTypeResponse>(`${this.apiUrl}/security-types`, request, { headers: this.getAuthHeaders() });
  }

  updateSecurityType(stId: number, request: SecurityTypeRequest): Observable<SecurityTypeResponse> {
    return this.http.put<SecurityTypeResponse>(`${this.apiUrl}/security-types/${stId}`, request, { headers: this.getAuthHeaders() });
  }

  deleteSecurityType(stId: number): Observable<void> {
    try {
      const token = this.authService.getValidToken();
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      });
      return this.http.delete<void>(`${this.apiUrl}/security-types/${stId}`, { headers }).pipe(
        catchError((error: HttpErrorResponse) => {
          let errorMessage = 'Failed to delete security type';
          if (error.status === 401) {
            this.authService.clearAuthData();
            this.router.navigate(['/login']);
            errorMessage = 'Session expired. Please log in again.';
          } else if (error.status === 404) {
            errorMessage = error.error?.error || 'Security type not found.';
          } else if (error.status === 403) {
            errorMessage = 'Unauthorized: You do not have permission to delete this security type.';
          } else if (error.status >= 500) {
            errorMessage = error.error?.error || 'Server error. Please try again later.';
          }
          this.toastr.error(errorMessage, 'Error');
          return throwError(() => new Error(errorMessage));
        })
      );
    } catch (error) {
      this.toastr.error('Session expired. Please log in again.', 'Error');
      this.authService.clearAuthData();
      this.router.navigate(['/login']);
      return throwError(() => new Error('Session expired'));
    }
  }

  getAllSecurityTypes(): Observable<SecurityTypeResponse[]> {
    return this.http.get<SecurityTypeResponse[]>(`${this.apiUrl}/security-types`, { headers: this.getAuthHeaders() }).pipe(
      catchError((error: HttpErrorResponse) => {
        this.handleAuthError(error);
        return throwError(error);
      })
    );
  }



  private handleAuthError(error: HttpErrorResponse): void {
    if (error.status === 401 || error.status === 403) {
      this.toastr.error('Session expired or unauthorized. Please login again.');
      this.authService.logout();
      this.router.navigate(['/login']);
    }
  }

  public getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    if (!token) {
      this.handleAuthError({ status: 401 } as HttpErrorResponse);
      throw new Error('No token available');
    }
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }
}
