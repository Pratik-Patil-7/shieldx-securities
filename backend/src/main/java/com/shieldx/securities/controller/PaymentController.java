package com.shieldx.securities.controller;

import com.razorpay.RazorpayException;
import com.shieldx.securities.dto.InitiatePaymentRequest;
import com.shieldx.securities.dto.PaymentResponse;
import com.shieldx.securities.dto.VerifyPaymentRequest;
import com.shieldx.securities.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*") 
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@GetMapping("/{bookingId}")
	public ResponseEntity<PaymentResponse> getPaymentDetails(@AuthenticationPrincipal Integer userId,
			@PathVariable("bookingId") Integer bookingId) {
		return ResponseEntity.ok(paymentService.getPaymentDetails(userId, bookingId));
	}

	@PostMapping("/initiate")
	public ResponseEntity<String> initiatePayment(@AuthenticationPrincipal Integer userId,
			@RequestBody InitiatePaymentRequest request) throws RazorpayException {
		String paymentUrl = paymentService.initiatePayment(userId, request);
		return ResponseEntity.ok(paymentUrl);
	}

	@PostMapping("/verify")
	public ResponseEntity<String> verifyPayment(@AuthenticationPrincipal Integer userId,
			@RequestBody VerifyPaymentRequest request) throws RazorpayException {
		paymentService.verifyPayment(userId, request);
		return ResponseEntity.ok("Payment verified successfully");
	}
}