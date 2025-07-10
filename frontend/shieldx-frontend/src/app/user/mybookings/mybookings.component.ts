import { AuthService } from 'src/app/core/services/auth.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, catchError, finalize, of, tap, throwError } from 'rxjs';
import { BookingService, Booking, BookingRequest } from '../../core/services/booking.service';
import { VipService, VipPerson } from '../../core/services/vip.service';
import { SecurityTypeService, SecurityType } from '../../core/services/security-type.service';

interface BookingTab {
  name: string;
  label: string;
  request: Observable<Booking[]>;
}

@Component({
  selector: 'app-mybookings',
  templateUrl: './mybookings.component.html',
  styleUrls: ['./mybookings.component.css']
})
export class MybookingsComponent implements OnInit {
  // Data
  bookings: Booking[] = [];
  filteredBookings: Booking[] = [];
  vipPersons: VipPerson[] = [];
  securityTypes: SecurityType[] = [];

  // Form state
  newBooking: BookingRequest = this.getDefaultBookingRequest();
  estimatedCost: number | null = null;

  // UI state
  currentTab: string = 'current';
  isLoading: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';
  showNewBookingForm: boolean = false;
  selectedBooking: Booking | null = null;

  // Tab configuration
  tabs: BookingTab[] = [
    { name: 'current', label: 'Current Bookings', request: this.bookingService.getCurrentBookings() },
    { name: 'past', label: 'Past Bookings', request: this.bookingService.getPastBookings() },
    { name: 'cancelled', label: 'Cancelled Bookings', request: this.bookingService.getCancelledBookings() }
  ];

  constructor(
    private bookingService: BookingService,
    private authService: AuthService,
    private router: Router,
    private vipService: VipService,
    private securityTypeService: SecurityTypeService
  ) { }

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }
    console.log(this.securityTypeService.getAllSecurityTypes());
    this.loadInitialData();
  }

  private loadInitialData(): void {
    this.isLoading = true;

    // Load all required data in parallel
    Promise.all([
      this.loadBookings().toPromise(),
      this.loadVipPersons().toPromise(),
      this.loadSecurityTypes().toPromise()
    ]).finally(() => this.isLoading = false);

  }

  private loadBookings(): Observable<Booking[]> {
    const currentTab = this.tabs.find(tab => tab.name === this.currentTab);
    const request = currentTab?.request || this.bookingService.getUserBookings();

    return request.pipe(
      catchError(err => this.handleError('Failed to load bookings', err)),
      finalize(() => this.isLoading = false)
    ).pipe(
      tap(bookings => {
        this.bookings = bookings;
        this.filteredBookings = [...bookings];
      })
    );
  }

  private loadVipPersons(): Observable<VipPerson[]> {
    return this.vipService.getAllVipPersons().pipe(
      catchError(err => {
        console.error('Error loading VIP persons:', err);
        return of([]);
      }),
      tap(vips => this.vipPersons = vips)
    );
  }



 private loadSecurityTypes(): Observable<SecurityType[]> {
  return this.securityTypeService.getAllSecurityTypes().pipe(
    tap(types => {
      console.log('Security Types:', JSON.stringify(types, null, 2));
      this.securityTypes = types;
    }),
    catchError(err => {
      console.error('Error loading security types:', err);
      return of([]);
    })
  );
}

  // Form methods
  toggleNewBookingForm(): void {
    this.showNewBookingForm = !this.showNewBookingForm;
    if (!this.showNewBookingForm) {
      this.resetBookingForm();
    }
  }



 private getDefaultBookingRequest(): BookingRequest {
  const userId = this.authService.getUserId();  //Get userId from AuthService

  return {
    userId: userId ?? 0, // default to 0 if null
    vipId: 1,
    securityTypeId: 1,
    bouncerCount: 1,
    startDate: '',
    endDate: '',
    startTime: '08:00',
    endTime: '18:00',
    location: '',
    bouncerIds: []
  };
}



  resetBookingForm(): void {
    this.newBooking = this.getDefaultBookingRequest();
    this.estimatedCost = null;
    this.errorMessage = '';
  }

  // Booking actions
 calculateCost(): void {
  if (!this.validateBookingForm()) return;

  // Prepare the request object in exact API format
  const request = {
    userId: this.newBooking.userId,
    vipId: this.newBooking.vipId,
    securityTypeId: this.newBooking.securityTypeId,
    bouncerCount: this.newBooking.bouncerCount.toString(), // API expects string
    startDate: this.newBooking.startDate,
    endDate: this.newBooking.endDate,
    startTime: this.formatTime(this.newBooking.startTime), // Add formatting if needed
    endTime: this.formatTime(this.newBooking.endTime),
    location: this.newBooking.location,
    bouncerIds: this.newBooking.bouncerIds
  };

  this.isLoading = true;
  this.bookingService.calculateCost(request).pipe(
    finalize(() => this.isLoading = false)
  ).subscribe({
    next: cost => this.estimatedCost = cost.totalCost, // Access totalCost from response
    error: err => this.handleError('Failed to calculate cost', err)
  });
}

// Helper method to format time if needed
private formatTime(time: string): string {
  return time.includes(':') ? time : `${time}:00`;
}


  createBooking(): void {
    if (!this.validateBookingForm() || !this.estimatedCost) return;

    this.isLoading = true;
    this.bookingService.createBooking(this.newBooking).pipe(
      finalize(() => this.isLoading = false)
    ).subscribe({
      next: () => {
        this.showSuccess('Booking created successfully');
        this.showNewBookingForm = false;
        this.loadBookings();
      },
      error: err => this.handleError('Failed to create booking', err)
    });
  }

  cancelBooking(bookingId: number): void {
    if (!confirm('Are you sure you want to cancel this booking?')) return;

    this.isLoading = true;
    this.bookingService.cancelBooking(bookingId).pipe(
      finalize(() => this.isLoading = false)
    ).subscribe({
      next: () => {
        this.showSuccess('Booking cancelled successfully');
        this.loadBookings();
      },
      error: err => this.handleError('Failed to cancel booking', err)
    });
  }

  // UI helpers
  changeTab(tabName: string): void {
    this.currentTab = tabName;
    this.loadBookings();
  }

  filterBookings(searchTerm: string): void {
    if (!searchTerm) {
      this.filteredBookings = [...this.bookings];
      return;
    }

    const term = searchTerm.toLowerCase();
    this.filteredBookings = this.bookings.filter(booking =>
      booking.location.toLowerCase().includes(term) ||
      booking.status.toLowerCase().includes(term) ||
      (booking.vipName?.toLowerCase().includes(term)) ||
      (booking.securityTypeName?.toLowerCase().includes(term))
    );
  }

  showBookingDetails(booking: Booking): void {
    this.selectedBooking = booking;
  }

  closeBookingDetails(): void {
    this.selectedBooking = null;
  }

  getSelectedSecurityType(): SecurityType | undefined {

    return this.securityTypes.find(type => type.stId === +this.newBooking.securityTypeId);

  }

  // Validation
  private validateBookingForm(): boolean {
    const errors: string[] = [];

    if (!this.newBooking.startDate || !this.newBooking.endDate) {
      errors.push('Dates are required');
    } else if (new Date(this.newBooking.startDate) > new Date(this.newBooking.endDate)) {
      errors.push('End date must be after start date');
    }

    if (!this.newBooking.location) errors.push('Location is required');
    if (this.newBooking.bouncerCount < 1) errors.push('At least one bouncer is required');

    this.errorMessage = errors.join('. ');
    return errors.length === 0;
  }

  // Error handling
  private handleError(message: string, error: any): Observable<never> {
    console.error(message, error);
    this.errorMessage = `${message}. ${error?.error?.message || ''}`;

    if (error.status === 403) {
      this.authService.logout();
    }

    return throwError(() => error);
  }

  private showSuccess(message: string): void {
    this.successMessage = message;
    setTimeout(() => this.successMessage = '', 3000);
  }
}
