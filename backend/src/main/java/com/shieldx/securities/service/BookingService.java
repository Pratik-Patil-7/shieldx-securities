package com.shieldx.securities.service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shieldx.securities.dto.BookingRequest;
import com.shieldx.securities.dto.BookingResponse;
import com.shieldx.securities.model.Booking;
import com.shieldx.securities.model.Bouncer;
import com.shieldx.securities.model.SecurityType;
import com.shieldx.securities.model.User;
import com.shieldx.securities.model.VipPerson;
import com.shieldx.securities.repository.BookingRepository;
import com.shieldx.securities.repository.BouncerRepository;
import com.shieldx.securities.repository.SecurityTypeRepository;
import com.shieldx.securities.repository.UserRepository;
import com.shieldx.securities.repository.VIPpersonRepository;


@Service
public class BookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VIPpersonRepository vipPersonRepository;

	@Autowired
	private SecurityTypeRepository securityTypeRepository;

	@Autowired
	private BouncerRepository bouncerRepository;

	public double calculateBookingCost(BookingRequest request) {
		SecurityType securityType = securityTypeRepository.findById(request.getSecurityTypeId())
				.orElseThrow(() -> new RuntimeException("Security type not found"));
		long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
		int bouncerCount = Integer.parseInt(request.getBouncerCount());
		double pricePerDay = Double.parseDouble(securityType.getPricePerDay());
		double totalCost = days * bouncerCount * pricePerDay;
		return totalCost;
	}

	public BookingResponse createBooking(Integer userId, BookingRequest request) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		VipPerson vip = vipPersonRepository.findById(request.getVipId())
				.orElseThrow(() -> new RuntimeException("VIP not found"));
		SecurityType securityType = securityTypeRepository.findById(request.getSecurityTypeId())
				.orElseThrow(() -> new RuntimeException("Security type not found"));

		Booking booking = new Booking();
		booking.setUser(user);
		booking.setVip(vip);
		booking.setSecurityType(securityType);
		booking.setBouncerCount(request.getBouncerCount());
		booking.setStartDate(request.getStartDate());
		booking.setEndDate(request.getEndDate());
		booking.setStartTime(request.getStartTime());
		booking.setEndTime(request.getEndTime());
		booking.setLocation(request.getLocation());
		booking.setStatus("pending");
		booking.setTotalPrice(calculateBookingCost(request));
		bookingRepository.save(booking);

		return mapToBookingResponse(booking);
	}

	public List<BookingResponse> getAllBookings() {
		return bookingRepository.findAll().stream().map(this::mapToBookingResponse).collect(Collectors.toList());
	}

	public List<BookingResponse> getUserBookings(Integer userId) {
		return bookingRepository.findByUserId(userId).stream().map(this::mapToBookingResponse)
				.collect(Collectors.toList());
	}

	public BookingResponse getBookingDetails(Integer userId, Integer bookingId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));
		if (!booking.getUser().getUserId().equals(userId)) {
			throw new RuntimeException("Unauthorized access to booking");
		}
		return mapToBookingResponse(booking);
	}

	public List<Booking> getBookingsByUserAndStatus(Integer userId, String status) {
		return bookingRepository.findByUserIdAndStatus(userId, status);
	}

	public void cancelBooking(Integer userId, Integer bookingId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));
		if (!booking.getUser().getUserId().equals(userId)) {
			throw new RuntimeException("Unauthorized access to booking");
		}
		booking.setStatus("cancelled");
		bookingRepository.save(booking);
	}

	public List<BookingResponse> getCurrentBookings(Integer userId) {
		return bookingRepository.findByUserIdAndStatus(userId, "confirmed").stream().map(this::mapToBookingResponse)
				.collect(Collectors.toList());
	}

	public List<BookingResponse> getPastBookings(Integer userId) {
		return bookingRepository.findByUserIdAndStatus(userId, "completed").stream().map(this::mapToBookingResponse)
				.collect(Collectors.toList());
	}

	public List<BookingResponse> getCancelledBookings(Integer userId) {
		return bookingRepository.findByUserIdAndStatus(userId, "cancelled").stream().map(this::mapToBookingResponse)
				.collect(Collectors.toList());
	}

	public void approveBooking(Integer bookingId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));
		booking.setStatus("confirmed");
		bookingRepository.save(booking);
	}

	private BookingResponse mapToBookingResponse(Booking booking) {
		return new BookingResponse(booking.getBookingId(), booking.getUser().getUserId(), booking.getVip().getVipId(),
				booking.getSecurityType().getStId(), booking.getBouncerCount(), booking.getStartDate(),
				booking.getEndDate(), booking.getStartTime(), booking.getEndTime(), booking.getLocation(),
				booking.getStatus(), booking.getTotalPrice());
	}

	public void assignBouncer(Integer bookingId, Integer bouncerId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));
		Bouncer bouncer = bouncerRepository.findById(bouncerId)
				.orElseThrow(() -> new RuntimeException("Bouncer not found"));
		booking.setBouncer(bouncer);
		bookingRepository.save(booking);
	}

}