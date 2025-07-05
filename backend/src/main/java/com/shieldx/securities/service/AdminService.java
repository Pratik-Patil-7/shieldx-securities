package com.shieldx.securities.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shieldx.securities.dto.DashboardStatsResponse;
import com.shieldx.securities.dto.UserResponse;
import com.shieldx.securities.model.User;
import com.shieldx.securities.repository.BookingRepository;
import com.shieldx.securities.repository.JobApplicationRepository;
import com.shieldx.securities.repository.PaymentRepository;
import com.shieldx.securities.repository.UserRepository;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    public DashboardStatsResponse getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalBookings = bookingRepository.count();
        long totalApplications = jobApplicationRepository.count();
        long totalPayments = paymentRepository.count();
        return new DashboardStatsResponse(totalUsers, totalBookings, totalApplications, totalPayments);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(user.getUserId(), user.getFirstName(), user.getLastName(),
                                             user.getUsername(), user.getEmail(), user.getMobile(), user.getAddress()))
                .collect(Collectors.toList());
    }

    public UserResponse getUserDetails(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponse(user.getUserId(), user.getFirstName(), user.getLastName(),
                               user.getUsername(), user.getEmail(), user.getMobile(), user.getAddress());
    }

    public void deactivateUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}