import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

export interface Booking {
  bookingId: number;
  userId: number;
  vipId: number;
  securityTypeId: number;
  bouncerCount: number;
  startDate: string;
  endDate: string;
  startTime: string;
  endTime: string;
  location: string;
  status: string;
  totalPrice: number;
  vipName?: string;
  securityTypeName?: string;
  bouncerIds?: number[];
}

export interface BookingRequest {
  vipId: number;
  userId:number;
  securityTypeId: number;
  bouncerCount: number;
  startDate: string;
  endDate: string;
  startTime: string;
  endTime: string;
  location: string;
  bouncerIds: number[];
}




@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private apiUrl = 'http://localhost:8080/api/bookings';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders() {
    return new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`,
      'Content-Type': 'application/json'
    });
  }

  calculateCost(request: any): Observable<{totalCost: number}> {
  return this.http.post<{totalCost: number}>(`${this.apiUrl}/calculate`, request, {
    headers: this.getHeaders()
  });
}

  createBooking(request: BookingRequest): Observable<Booking> {
    return this.http.post<Booking>(this.apiUrl, request, {
      headers: this.getHeaders()
    });
  }

  getUserBookings(): Observable<Booking[]> {
    return this.http.get<Booking[]>(this.apiUrl, {
      headers: this.getHeaders()
    });
  }

  getCurrentBookings(): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${this.apiUrl}/current`, {
      headers: this.getHeaders()
    });
  }

  getPastBookings(): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${this.apiUrl}/past`, {
      headers: this.getHeaders()
    });
  }

  getCancelledBookings(): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${this.apiUrl}/cancelled`, {
      headers: this.getHeaders()
    });
  }

  getBookingDetails(bookingId: number): Observable<Booking> {
    return this.http.get<Booking>(`${this.apiUrl}/${bookingId}`, {
      headers: this.getHeaders()
    });
  }

  cancelBooking(bookingId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${bookingId}/cancel`, {}, {
      headers: this.getHeaders()
    });
  }
}
