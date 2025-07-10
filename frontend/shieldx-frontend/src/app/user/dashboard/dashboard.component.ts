import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router } from '@angular/router';
import { BookingService } from 'src/app/core/services/booking.service';
import { Booking } from 'src/app/core/services/booking.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  isLoading: boolean = true;
  userProfile: any = null;
  bookings: Booking[] = [];
  stats = {
    active: 0,
    pending: 0,
    cancelled: 0
  };

  constructor(
    private authService: AuthService,
    public router: Router,
    private bookingService: BookingService
  ) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }

    this.loadUserData();
    this.loadBookings();
  }

  loadUserData(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.userProfile = user;
    } else {
      this.authService.getUserProfile().subscribe({
        next: (profile) => {
          this.userProfile = profile;
        },
        error: (err) => {
          console.error('Failed to load user profile:', err);
        }
      });
    }
  }

  loadBookings(): void {
    this.bookingService.getUserBookings().subscribe({
      next: (bookings) => {
        this.bookings = bookings;
        this.calculateStats();
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Failed to load bookings:', err);
        this.isLoading = false;
      }
    });
  }

  calculateStats(): void {
    // Reset counters
    this.stats = {
      active: 0,
      pending: 0,
      cancelled: 0
    };

    // Calculate counts
    this.bookings.forEach(booking => {
      if (booking.status) {
        const status = booking.status.toUpperCase();
        if (status === 'CONFIRMED') {
          this.stats.active++;
        } else if (status === 'PENDING') {
          this.stats.pending++;
        } else if (status === 'CANCELLED') {
          this.stats.cancelled++;
        }
      }
    });
  }
  navigateTo(route: string): void {
    this.router.navigate([route]);
  }

  logout(): void {
    this.authService.logout();
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString();
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'CONFIRMED':
        return 'bg-success bg-opacity-10 text-success';
      case 'PENDING':
        return 'bg-warning bg-opacity-10 text-warning';
      case 'CANCELLED':
        return 'bg-danger bg-opacity-10 text-danger';
      default:
        return 'bg-secondary bg-opacity-10 text-secondary';
    }
  }

  getStatusIcon(status: string): string {
    switch (status) {
      case 'CONFIRMED':
        return 'bi-check-circle';
      case 'PENDING':
        return 'bi-clock';
      case 'CANCELLED':
        return 'bi-x-circle';
      default:
        return 'bi-question-circle';
    }
  }
}
