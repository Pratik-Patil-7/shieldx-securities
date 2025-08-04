import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';

export interface JobApplication {
  id: number;
  name: string;
  email: string;
  mobile: string;
  dob: string; // Matches the JSON "2003-06-12" format
  gender: string;
  address: string;
  qualification: string;
  experience: string;
  status: string;
  resumeUrl: string | null;
  photoUrl: string | null;
  applicationId?: number; // Optional, as per JSON
  userId?: number; // Optional, added to match JSON "userId": 11
}

export interface JobApplicationSubmission {
  name: string;
  email: string;
  mobile: string;
  dob: string;
  gender: string;
  address: string;
  qualification: string;
  experience: string;
  resumeUrl?: string | null;
  photoUrl?: string | null;
  userId?: number; // Optional for submission if required by backend
}

@Injectable({
  providedIn: 'root'
})
export class JobService {
  private apiUrl = 'http://localhost:8080/api/jobs';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    if (!token) {
      throw new Error('No authentication token available');
    }
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  private handleError(error: any): Observable<never> {
    console.error('API error:', error);
    return throwError(() => new Error(error.message || 'An unexpected error occurred'));
  }

  applyForJob(application: JobApplicationSubmission): Observable<any> {
    return this.http.post(`${this.apiUrl}/apply`, application, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  getApplicationById(id: number): Observable<JobApplication> {
    return this.http.get<JobApplication>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  getPendingApplications(): Observable<JobApplication[]> {
    return this.http.get<JobApplication[]>(`${this.apiUrl}/pending`, { headers: this.getAuthHeaders() }).pipe(
      catchError(this.handleError)
    );
  }
}
