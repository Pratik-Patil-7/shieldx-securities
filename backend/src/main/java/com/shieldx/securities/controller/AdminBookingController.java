package com.shieldx.securities.controller;

import com.shieldx.securities.dto.AssignBouncerRequest;
import com.shieldx.securities.dto.BookingResponse;
import com.shieldx.securities.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/bookings")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminBookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<BookingResponse>> listAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingDetails(@PathVariable("id") Integer bookingId) {
        BookingResponse booking = bookingService.getBookingDetails(null, bookingId); // Admin context, userId null
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveBooking(@PathVariable("id") Integer bookingId) {
        bookingService.approveBooking(bookingId);
        return ResponseEntity.ok("Booking approved successfully");
    }


    @PutMapping("/{id}/reject")
    public ResponseEntity<String> rejectBooking(@PathVariable("id") Integer bookingId) {
        bookingService.cancelBooking(null, bookingId); // Admin context, userId not required
        return ResponseEntity.ok("Booking rejected successfully");
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<String> assignBouncer(@PathVariable("id") Integer bookingId,
                                               @RequestBody AssignBouncerRequest request) {
        bookingService.assignBouncer(bookingId, request.getBouncerId());
        return ResponseEntity.ok("Bouncer assigned successfully");
    }
}