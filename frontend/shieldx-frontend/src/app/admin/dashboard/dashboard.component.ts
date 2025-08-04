import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/core/services/admin.service';
import { DashboardStatsResponse } from 'src/app/dto/dashboard-stats-response';
import { BookingResponse } from 'src/app/dto/booking-response';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  isLoading: boolean = true;
  stats: DashboardStatsResponse | null = null;
  recentBookings: BookingResponse[] = [];

  constructor(
    private adminService: AdminService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.adminService.getDashboardStats().subscribe({
      next: (stats) => {
        this.stats = stats;
        this.loadRecentBookings();
      },
      error: (err) => {
        console.error('Failed to load dashboard stats:', err);
        this.isLoading = false;
      }
    });
  }

  loadRecentBookings(): void {
    this.adminService.getAllBookings().subscribe({
      next: (bookings) => {
        this.recentBookings = bookings.slice(0, 5);
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Failed to load bookings:', err);
        this.isLoading = false;
      }
    });
  }

  navigateTo(route: string): void {
    this.router.navigate([route]);
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleDateString();
  }

  getStatusBadgeClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'CONFIRMED': return 'bg-success bg-opacity-10 text-success';
      case 'PENDING': return 'bg-warning bg-opacity-10 text-warning';
      case 'CANCELLED': return 'bg-danger bg-opacity-10 text-danger';
      default: return 'bg-secondary bg-opacity-10 text-secondary';
    }
  }

  getStatusIcon(status: string): string {
    switch (status?.toUpperCase()) {
      case 'CONFIRMED': return 'bi-check-circle';
      case 'PENDING': return 'bi-clock';
      case 'CANCELLED': return 'bi-x-circle';
      default: return 'bi-question-circle';
    }
  }
}
