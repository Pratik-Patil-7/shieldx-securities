import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../core/services/admin.service';
import { BookingResponse } from '../../dto/booking-response';
import { ToastrService } from 'ngx-toastr';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PaymentSimulatorService } from './payment-simulator.service';

@Component({
  selector: 'app-bookings',
  templateUrl: './bookings.component.html',
  styleUrls: ['./bookings.component.css']
})
export class BookingsComponent implements OnInit {
  bookings: BookingResponse[] = [];
  filteredBookings: BookingResponse[] = [];
  isLoading = true;
  currentPage = 1;
  itemsPerPage = 10;
  searchTerm = '';
  assignForm: FormGroup;
  selectedBooking: BookingResponse | null = null;
  paymentQRCode: string | null = null;
  isProcessingPayment = false;
  modalBooking: BookingResponse | null = null;

  constructor(
    private adminService: AdminService,
    private toastr: ToastrService,
    private modalService: NgbModal,
    private fb: FormBuilder,
    private paymentSimulator: PaymentSimulatorService
  ) {
    this.assignForm = this.fb.group({
      bouncerId: ['', [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.loadBookings();
    console.log(this.bookings)
  }

  loadBookings(): void {
    this.isLoading = true;
    this.adminService.getAllBookings().subscribe({
      next: (bookings) => {
        this.bookings = bookings;
        this.filteredBookings = [...bookings];
        this.isLoading = false;
      },
      error: (err) => {
        this.toastr.error('Failed to load bookings');
        this.isLoading = false;
      }
    });



  }

  isApproving = false;
  isRejecting = false;

  approveBooking(bookingId: number): void {
    this.isApproving = true;
    this.adminService.approveBooking(bookingId).subscribe({
      next: () => {
        this.isApproving = false;
        this.toastr.success('Booking approved');
        this.loadBookings();
      },
      error: (err) => {
        this.isApproving = false;
        this.toastr.error('Failed to approve booking');
      }
    });
  }

  get totalPages(): number {
    return Math.ceil(this.filteredBookings.length / this.itemsPerPage);
  }


  rejectBooking(bookingId: number): void {
    this.adminService.rejectBooking(bookingId).subscribe({
      next: () => {
        this.toastr.success('Booking rejected successfully.');
        this.loadBookings();
      },
      error: (err) => {
        this.toastr.error('Failed to reject booking');
      }
    });
  }


  simulatePayment(booking: BookingResponse): void {
    this.isProcessingPayment = true;
    this.selectedBooking = booking;

    this.paymentSimulator.simulatePayment(booking.bookingId, booking.totalPrice).subscribe({
      next: (result) => {
        this.paymentQRCode = `https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=PAY-${booking.bookingId}-${Date.now()}`;
        this.isProcessingPayment = false;
        this.toastr.success('Payment simulation started');
      },
      error: (err) => {
        this.isProcessingPayment = false;
        this.toastr.error('Failed to initiate payment simulation');
      }
    });
  }

  assignBouncer(): void {
    if (this.assignForm.invalid || !this.selectedBooking) return;

    const bouncerId = this.assignForm.get('bouncerId')?.value;
    this.adminService.assignBouncer(this.selectedBooking.bookingId, bouncerId).subscribe({
      next: () => {
        this.toastr.success('Bouncer assigned successfully');
        this.resetPaymentFlow();
        this.loadBookings();
      },
      error: (err) => {
        this.toastr.error('Failed to assign bouncer');
      }
    });
  }

  resetPaymentFlow(): void {
    this.paymentQRCode = null;
    this.selectedBooking = null;
    this.assignForm.reset();
  }

  openDetailsModal(content: any, booking: BookingResponse): void {
    this.modalBooking = booking;
    this.modalService.open(content, { size: 'lg', centered: true });
  }

  filterBookings(): void {
    if (!this.searchTerm) {
      this.filteredBookings = [...this.bookings];
      return;
    }

    const term = this.searchTerm.toLowerCase();
    this.filteredBookings = this.bookings.filter(booking =>
      booking.location.toLowerCase().includes(term) ||
      booking.bookingId.toString().includes(term) ||
      booking.status.toLowerCase().includes(term)
    );
  }

  getStatusBadgeClass(status: string): string {
    const statusUpper = status?.toUpperCase();
    switch (statusUpper) {
      case 'PENDING': return 'badge bg-warning text-dark';
      case 'APPROVED': return 'badge bg-primary';
      case 'CONFIRMED': return 'badge bg-success';
      case 'REJECTED': return 'badge bg-danger';
      case 'COMPLETED': return 'badge bg-secondary';
      default: return 'badge bg-light text-dark';
    }
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  }

  getBouncerIds(booking: BookingResponse): string {
    return booking.bouncerIds?.length ? booking.bouncerIds.join(', ') : 'Not assigned';
  }

  get paginatedBookings(): BookingResponse[] {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    return this.filteredBookings.slice(start, start + this.itemsPerPage);
  }
}
