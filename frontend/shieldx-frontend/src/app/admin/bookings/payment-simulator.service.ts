import { Injectable } from '@angular/core';
import { of } from 'rxjs';
import { delay } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class PaymentSimulatorService {
  simulatePayment(bookingId: number, amount: number) {
    // Simulate API call with 1.5 second delay
    return of({
      success: true,
      paymentId: `PAY-${bookingId}-${Date.now()}`,
      amount: amount,
      timestamp: new Date().toISOString()
    }).pipe(delay(1500));
  }
}
