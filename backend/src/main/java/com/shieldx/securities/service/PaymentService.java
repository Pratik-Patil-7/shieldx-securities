//package com.shieldx.securities.service;
//
//import java.time.LocalDateTime;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.razorpay.RazorpayException;
//import com.shieldx.securities.dto.InitiatePaymentRequest;
//import com.shieldx.securities.dto.PaymentResponse;
//import com.shieldx.securities.dto.VerifyPaymentRequest;
//import com.shieldx.securities.model.Booking;
//import com.shieldx.securities.model.Payment;
//import com.shieldx.securities.model.User;
//import com.shieldx.securities.repository.BookingRepository;
//import com.shieldx.securities.repository.PaymentRepository;
//import com.shieldx.securities.repository.UserRepository;
//
//@Service
//public class PaymentService {
//
//	@Autowired
//	private PaymentRepository paymentRepository;
//
//	@Autowired
//	private BookingRepository bookingRepository;
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@Autowired
//	private RazorpayService razorpayService;
//
//	public PaymentResponse getPaymentDetails(Integer userId, Integer bookingId) {
//		Booking booking = bookingRepository.findById(bookingId)
//				.orElseThrow(() -> new RuntimeException("Booking not found"));
//		if (!booking.getUser().getUserId().equals(userId)) {
//			throw new RuntimeException("Unauthorized access to payment");
//		}
//		Payment payment = paymentRepository.findByBookingId(bookingId)
//				.orElseThrow(() -> new RuntimeException("Payment not found"));
//		return new PaymentResponse(payment.getPaymentId(), payment.getBooking().getBookingId(),
//				payment.getUser().getUserId(), payment.getAmount(), payment.getStatus(), payment.getPaymentDate());
//	}
//
//	public String initiatePayment(Integer userId, InitiatePaymentRequest request) throws RazorpayException {
//		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//		Booking booking = bookingRepository.findById(request.getBookingId())
//				.orElseThrow(() -> new RuntimeException("Booking not found"));
//		if (!booking.getUser().getUserId().equals(userId)) {
//			throw new RuntimeException("Unauthorized access to booking");
//		}
//
//		Payment payment = new Payment();
//		payment.setUser(user);
//		payment.setBooking(booking);
//		payment.setAmount(booking.getTotalPrice());
//		payment.setStatus("pending");
//		payment.setPaymentDate(LocalDateTime.now());
//		paymentRepository.save(payment);
//
//		return razorpayService.createPaymentOrder(payment.getAmount(), payment.getPaymentId());
//	}
//
//	public void verifyPayment(Integer userId, VerifyPaymentRequest request) throws RazorpayException {
//		Payment payment = paymentRepository.findById(request.getPaymentId())
//				.orElseThrow(() -> new RuntimeException("Payment not found"));
//		if (!payment.getUser().getUserId().equals(userId)) {
//			throw new RuntimeException("Unauthorized access to payment");
//		}
//		boolean isValid = razorpayService.verifyPaymentSignature(request.getRazorpayPaymentId(),
//				request.getRazorpayOrderId(), request.getRazorpaySignature());
//		if (!isValid) {
//			throw new RuntimeException("Payment verification failed");
//		}
//		payment.setStatus("completed");
//		paymentRepository.save(payment);
//	}
//}


package com.shieldx.securities.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shieldx.securities.dto.InitiatePaymentRequest;
import com.shieldx.securities.dto.PaymentResponse;
import com.shieldx.securities.dto.VerifyPaymentRequest;
import com.shieldx.securities.model.Booking;
import com.shieldx.securities.repository.BookingRepository;

@Service
public class PaymentService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String beeceptorBaseUrl = "https://fake-payment.free.beeceptor.com";

    public String initiatePayment(Integer userId, InitiatePaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        if (!"confirmed".equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException("Booking must be confirmed to initiate payment");
        }

        // Mock Beeceptor API call to initiate payment
        String paymentId = "PAY_" + System.currentTimeMillis();
        String paymentUrl = "http://localhost:4200/payments/verify?paymentId=" + paymentId + "&bookingId=" + request.getBookingId();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<InitiatePaymentRequest> entity = new HttpEntity<>(request, headers);
        restTemplate.exchange(
                beeceptorBaseUrl + "/initiate",
                HttpMethod.POST,
                entity,
                String.class
        );
        return paymentUrl;
    }

    public void verifyPayment(Integer userId, VerifyPaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        // Mock Beeceptor API call to verify payment
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<VerifyPaymentRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                beeceptorBaseUrl + "/verify",
                HttpMethod.POST,
                entity,
                String.class
        );

        if ("success".equalsIgnoreCase(response.getBody())) {
            // Assign bouncer after successful payment
            bookingService.assignBouncer(request.getBookingId(), request.getBouncerId());
        } else {
            throw new RuntimeException("Payment verification failed");
        }
    }

    public PaymentResponse getPaymentDetails(Integer userId, Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        // Mock payment details
        return new PaymentResponse(bookingId, "PAY_" + bookingId, booking.getTotalPrice(), "pending");
    }
}