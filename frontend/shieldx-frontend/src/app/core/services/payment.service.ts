import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { InitiatePaymentRequest } from '../../dto/initiate-payment-request';
import { VerifyPaymentRequest } from '../../dto/verify-payment-request';
import { PaymentResponse } from '../../dto/payment-response';
import { AdminService } from './admin.service';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private paymentApiUrl = 'http://localhost:8080/api/payments';

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService,
    private adminService: AdminService
  ) { }

  initiatePayment(request: InitiatePaymentRequest): Observable<string> {
    return this.http.post<string>(`${this.paymentApiUrl}/initiate`, request, { headers: this.adminService.getAuthHeaders() })
      .pipe(catchError(this.adminService.handleError.bind(this)));
  }

  verifyPayment(request: VerifyPaymentRequest): Observable<string> {
    return this.http.post<string>(`${this.paymentApiUrl}/verify`, request, { headers: this.adminService.getAuthHeaders() })
      .pipe(catchError(this.adminService.handleError.bind(this)));
  }

  getPaymentDetails(bookingId: number): Observable<PaymentResponse> {
    return this.http.get<PaymentResponse>(`${this.paymentApiUrl}/${bookingId}`, { headers: this.adminService.getAuthHeaders() })
      .pipe(catchError(this.adminService.handleError.bind(this)));
  }
}
