package com.shieldx.securities.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    public String createPaymentOrder(double amount, Integer paymentId) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // Convert to paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "payment_" + paymentId);
        Order order = razorpay.orders.create(orderRequest);
        return order.get("id").toString();
    }

    public boolean verifyPaymentSignature(String paymentId, String orderId, String signature) throws RazorpayException {
        // Implement signature verification logic
        // Use Razorpay's utility to verify paymentId, orderId, and signature
        return true; // Placeholder, replace with actual verification
    }
}