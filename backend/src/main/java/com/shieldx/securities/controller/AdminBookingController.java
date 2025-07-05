package com.shieldx.securities.controller;

import com.shieldx.securities.dto.BookingResponse;
import com.shieldx.securities.dto.AssignBouncerRequest;
import com.shieldx.securities.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/bookings")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBookingController {

	@Autowired
	private BookingService bookingService;

	@GetMapping
	public ResponseEntity<List<BookingResponse>> listAllBookings() {
		return ResponseEntity.ok(bookingService.getAllBookings());
	}

	@PutMapping("/{id}/approve")
	public ResponseEntity<String> approveBooking(@PathVariable("id") Integer bookingId) {
		bookingService.approveBooking(bookingId);
		return ResponseEntity.ok("Booking approved successfully");
	}

	@PutMapping("/{id}/assign")
	public ResponseEntity<String> assignBouncer(@PathVariable("id") Integer bookingId,
			@RequestBody AssignBouncerRequest request) {
		bookingService.assignBouncer(bookingId, request.getBouncerId());
		return ResponseEntity.ok("Bouncer assigned successfully");
	}
}