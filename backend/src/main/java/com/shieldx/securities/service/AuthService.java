package com.shieldx.securities.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.shieldx.securities.config.JwtService;
import com.shieldx.securities.dto.AuthResponse;
import com.shieldx.securities.dto.LoginRequest;
import com.shieldx.securities.dto.RegisterRequest;
import com.shieldx.securities.model.Login;
import com.shieldx.securities.model.OtpVerification;
import com.shieldx.securities.model.User;
import com.shieldx.securities.repository.LoginRepository;
import com.shieldx.securities.repository.OtpVerificationRepository;
import com.shieldx.securities.repository.UserRepository;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LoginRepository loginRepository;

	@Autowired
	private OtpVerificationRepository otpVerificationRepository;

	@Autowired
	private JwtService jwtService; // Assume a service for JWT generation

	@Autowired
	private EmailService emailService; // Assume a service for sending emails

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public AuthResponse register(RegisterRequest request) {
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new RuntimeException("Email already exists");
		}
		if (userRepository.findByUsername(request.getUsername()).isPresent()) {
			throw new RuntimeException("Username already exists");
		}

		User user = new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setMobile(request.getMobile());
		user.setAddress(request.getAddress());
		userRepository.save(user);

		Login login = new Login();
		login.setUser(user);
		login.setUsername(request.getUsername());
		login.setEmail(request.getEmail());
		login.setPassword(passwordEncoder.encode(request.getPassword()));
		login.setLastLogin(LocalDateTime.now());
		login.setStatus("active");
		loginRepository.save(login);

		String token = jwtService.generateToken(user.getUserId());
		return new AuthResponse(token, user.getUserId(), user.getEmail());
	}

	public AuthResponse login(LoginRequest request) {
		Login login = loginRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("Invalid credentials"));
		if (!passwordEncoder.matches(request.getPassword(), login.getPassword())) {
			int attempts = Integer.parseInt(login.getFailedAttempts()) + 1;
			login.setFailedAttempts(String.valueOf(attempts));
			loginRepository.save(login);
			throw new RuntimeException("Invalid credentials");
		}
		login.setFailedAttempts("0");
		login.setLastLogin(LocalDateTime.now());
		loginRepository.save(login);

		String token = jwtService.generateToken(login.getUser().getUserId());
		return new AuthResponse(token, login.getUser().getUserId(), login.getEmail());
	}

	public void sendOtp(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		String otpCode = String.valueOf((int) (Math.random() * 900000) + 100000);
		OtpVerification otp = new OtpVerification();
		otp.setUser(user);
		otp.setOtpCode(otpCode);
		otp.setExpiryTime(LocalTime.now().plusMinutes(10));
		otp.setVerified("no");
		otpVerificationRepository.save(otp);

		emailService.sendOtpEmail(email, otpCode);
	}

	public boolean verifyOtp(String email, String otpCode) {
		OtpVerification otp = otpVerificationRepository.findByUserEmailAndOtpCode(email, otpCode)
				.orElseThrow(() -> new RuntimeException("Invalid OTP"));
		if (otp.getExpiryTime().isBefore(LocalTime.now())) {
			throw new RuntimeException("OTP expired");
		}
		otp.setVerified("yes");
		otpVerificationRepository.save(otp);
		return true;
	}

	public void forgotPassword(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		String token = UUID.randomUUID().toString();
		// Store token in database or cache (not shown in entities, assuming temp
		// storage)
		emailService.sendResetPasswordEmail(email, token);
	}

	public void resetPassword(String email, String token, String newPassword) {
		// Validate token (assuming stored in a temp table or cache)
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		Login login = loginRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Login not found"));
		login.setPassword(passwordEncoder.encode(newPassword));
		loginRepository.save(login);
	}

	public AuthResponse githubLogin(String code) {
		// Implement GitHub OAuth flow (exchange code for token, fetch user details)
		// For simplicity, assume user is fetched and saved
		User user = userRepository.findByEmail("github-user@example.com").orElseGet(() -> {
			User newUser = new User();
			newUser.setEmail("github-user@example.com");
			newUser.setUsername("githubuser");
			return userRepository.save(newUser);
		});
		String token = jwtService.generateToken(user.getUserId());
		return new AuthResponse(token, user.getUserId(), user.getEmail());
	}
}