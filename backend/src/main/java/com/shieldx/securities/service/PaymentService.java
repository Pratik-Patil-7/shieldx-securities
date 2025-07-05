package com.shieldx.securities.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.RazorpayException;
import com.shieldx.securities.dto.InitiatePaymentRequest;
import com.shieldx.securities.dto.PaymentResponse;
import com.shieldx.securities.dto.VerifyPaymentRequest;
import com.shieldx.securities.model.Booking;
import com.shieldx.securities.model.Payment;
import com.shieldx.securities.model.User;
import com.shieldx.securities.repository.BookingRepository;
import com.shieldx.securities.repository.PaymentRepository;
import com.shieldx.securities.repository.UserRepository;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RazorpayService razorpayService;

	public PaymentResponse getPaymentDetails(Integer userId, Integer bookingId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));
		if (!booking.getUser().getUserId().equals(userId)) {
			throw new RuntimeException("Unauthorized access to payment");
		}
		Payment payment = paymentRepository.findByBookingId(bookingId)
				.orElseThrow(() -> new RuntimeException("Payment not found"));
		return new PaymentResponse(payment.getPaymentId(), payment.getBooking().getBookingId(),
				payment.getUser().getUserId(), payment.getAmount(), payment.getStatus(), payment.getPaymentDate());
	}

	public String initiatePayment(Integer userId, InitiatePaymentRequest request) throws RazorpayException {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		Booking booking = bookingRepository.findById(request.getBookingId())
				.orElseThrow(() -> new RuntimeException("Booking not found"));
		if (!booking.getUser().getUserId().equals(userId)) {
			throw new RuntimeException("Unauthorized access to booking");
		}

		Payment payment = new Payment();
		payment.setUser(user);
		payment.setBooking(booking);
		payment.setAmount(booking.getTotalPrice());
		payment.setStatus("pending");
		payment.setPaymentDate(LocalDateTime.now());
		paymentRepository.save(payment);

		return razorpayService.createPaymentOrder(payment.getAmount(), payment.getPaymentId());
	}

	public void verifyPayment(Integer userId, VerifyPaymentRequest request) throws RazorpayException {
		Payment payment = paymentRepository.findById(request.getPaymentId())
				.orElseThrow(() -> new RuntimeException("Payment not found"));
		if (!payment.getUser().getUserId().equals(userId)) {
			throw new RuntimeException("Unauthorized access to payment");
		}
		boolean isValid = razorpayService.verifyPaymentSignature(request.getRazorpayPaymentId(),
				request.getRazorpayOrderId(), request.getRazorpaySignature());
		if (!isValid) {
			throw new RuntimeException("Payment verification failed");
		}
		payment.setStatus("completed");
		paymentRepository.save(payment);
	}
}