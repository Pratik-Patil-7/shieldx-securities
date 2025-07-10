package com.shieldx.securities.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.shieldx.securities.dto.BookingRequest;
import com.shieldx.securities.dto.BookingResponse;
import com.shieldx.securities.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

	private final BookingService bookingService;

	public BookingController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	@PostMapping("/calculate")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> calculateBookingCost(@Valid @RequestBody BookingRequest request,
			BindingResult bindingResult, Authentication authentication) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(Map.of("error", bindingResult.getFieldError().getDefaultMessage()));
		}

		try {
			double cost = bookingService.calculateBookingCost(request);
			return ResponseEntity.ok(Map.of("totalCost", cost));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest request, BindingResult bindingResult,
			Authentication authentication) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(Map.of("error", bindingResult.getFieldError().getDefaultMessage()));
		}

		try {
			Integer userId = Integer.parseInt(authentication.getName());
			BookingResponse response = bookingService.createBooking(userId, request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> getUserBookings(Authentication authentication) {
		try {
			Integer userId = Integer.parseInt(authentication.getName());
			List<BookingResponse> bookings = bookingService.getUserBookings(userId);
			return ResponseEntity.ok(bookings);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BookingResponse> getBookingDetails(@PathVariable("id") Integer bookingId,
			Authentication authentication) {
		Integer userId = Integer.parseInt(authentication.getName());
		System.out.println("Fetching booking ID: " + bookingId + " for user ID: " + userId);
		return ResponseEntity.ok(bookingService.getBookingDetails(userId, bookingId));
	}

	@PutMapping("/{id}/cancel")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Map<String, String>> cancelBooking(@PathVariable("id") Integer bookingId,
			Authentication authentication) {
		Integer userId = Integer.parseInt(authentication.getName());
		System.out.println("Cancelling booking ID: " + bookingId + " for user ID: " + userId);
		bookingService.cancelBooking(userId, bookingId);
		return ResponseEntity.ok(Collections.singletonMap("message", "Booking cancelled successfully"));
	}

	@GetMapping("/current")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<BookingResponse>> listCurrentBookings(Authentication authentication) {
		Integer userId = Integer.parseInt(authentication.getName());
		System.out.println("Fetching current bookings for user ID: " + userId);
		return ResponseEntity.ok(bookingService.getCurrentBookings(userId));
	}

	@GetMapping("/past")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<BookingResponse>> listPastBookings(Authentication authentication) {
		Integer userId = Integer.parseInt(authentication.getName());
		System.out.println("Fetching past bookings for user ID: " + userId);
		return ResponseEntity.ok(bookingService.getPastBookings(userId));
	}

	@GetMapping("/cancelled")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<BookingResponse>> listCancelledBookings(Authentication authentication) {
		Integer userId = Integer.parseInt(authentication.getName());
		System.out.println("Fetching cancelled bookings for user ID: " + userId);
		return ResponseEntity.ok(bookingService.getCancelledBookings(userId));
	}

//	@GetMapping("/user/{status}")
//	@PreAuthorize("isAuthenticated()")
//	public ResponseEntity<List<BookingResponse>> getUserBookingsByStatus(@PathVariable String status,
//			Authentication authentication) {
//		Integer userId = Integer.parseInt(authentication.getName());
//		System.out.println("Fetching " + status + " bookings for user ID: " + userId);
//		return ResponseEntity.ok(bookingService.getBookingsByUserAndStatus(userId, status));
//	}
}