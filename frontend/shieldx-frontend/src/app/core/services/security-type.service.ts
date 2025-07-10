import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';

export interface SecurityType {
  stId: number;
  levelName: string;
  description: string;
  isArmed: string;
  pricePerDay: number;
}


@Injectable({
  providedIn: 'root'
})
export class SecurityTypeService {
  private apiUrl = 'http://localhost:8080/api/security';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) { }

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

  getAllSecurityTypes(): Observable<SecurityType[]> {
    return this.http.get<SecurityType[]>(`${this.apiUrl}/types`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(error => {
        console.error('Error fetching security types:', error);
        return throwError(() => new Error('Failed to fetch security types'));
      })
    );
  }

  // getArmedSecurityTypes(): Observable<SecurityType[]> {
  //   return this.http.get<SecurityType[]>(`${this.apiUrl}/armed-types`, {
  //     headers: this.getAuthHeaders()
  //   }).pipe(
  //     catchError(error => {
  //       console.error('Error fetching armed security types:', error);
  //       return throwError(() => new Error('Failed to fetch armed security types'));
  //     })
  //   );
  // }

  getSecurityLevels(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/levels`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(error => {
        console.error('Error fetching security levels:', error);
        return throwError(() => new Error('Failed to fetch security levels'));
      })
    );

  }

  getSecurityPricing(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/pricing`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(error => {
        console.error('Error fetching security pricing:', error);
        return throwError(() => new Error('Failed to fetch security pricing'));
      })
    );
  }
}
