package com.shieldx.securities.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shieldx.securities.dto.BookingRequest;
import com.shieldx.securities.dto.BookingResponse;
import com.shieldx.securities.model.Booking;
import com.shieldx.securities.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*") // Allow cross-origin requests
public class BookingController {

	private BookingService bookingService;

	@Autowired
	public BookingController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	@PostMapping("/calculate")
	public ResponseEntity<Double> calculateBookingCost(@AuthenticationPrincipal Integer userId,
			@RequestBody BookingRequest request) {
		double totalCost = bookingService.calculateBookingCost(request);
		return ResponseEntity.ok(totalCost);
	}

	@PostMapping
	public ResponseEntity<BookingResponse> createBooking(@AuthenticationPrincipal Integer userId,
			@RequestBody BookingRequest request) {
		return ResponseEntity.ok(bookingService.createBooking(userId, request));
	}

	@GetMapping
	public ResponseEntity<List<BookingResponse>> listUserBookings(@AuthenticationPrincipal Integer userId) {
		return ResponseEntity.ok(bookingService.getUserBookings(userId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<BookingResponse> getBookingDetails(@AuthenticationPrincipal Integer userId,
			@PathVariable("id") Integer bookingId) {
		return ResponseEntity.ok(bookingService.getBookingDetails(userId, bookingId));
	}

	@PutMapping("/{id}/cancel")
	public ResponseEntity<String> cancelBooking(@AuthenticationPrincipal Integer userId,
			@PathVariable("id") Integer bookingId) {
		bookingService.cancelBooking(userId, bookingId);
		return ResponseEntity.ok("Booking cancelled successfully");
	}

	@GetMapping("/current")
	public ResponseEntity<List<BookingResponse>> listCurrentBookings(@AuthenticationPrincipal Integer userId) {
		return ResponseEntity.ok(bookingService.getCurrentBookings(userId));
	}

	@GetMapping("/past")
	public ResponseEntity<List<BookingResponse>> listPastBookings(@AuthenticationPrincipal Integer userId) {
		return ResponseEntity.ok(bookingService.getPastBookings(userId));
	}

	@GetMapping("/cancelled")
	public ResponseEntity<List<BookingResponse>> listCancelledBookings(@AuthenticationPrincipal Integer userId) {
		return ResponseEntity.ok(bookingService.getCancelledBookings(userId));
	}

	@GetMapping("/user/{status}")
	public ResponseEntity<List<Booking>> getUserBookingsByStatus(@AuthenticationPrincipal Integer userId,
			@PathVariable String status) {
		List<Booking> bookings = bookingService.getBookingsByUserAndStatus(userId, status);
		return ResponseEntity.ok(bookings);
	}
}