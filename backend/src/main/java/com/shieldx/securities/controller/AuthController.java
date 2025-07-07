package com.shieldx.securities.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shieldx.securities.config.JwtService;
import com.shieldx.securities.dto.AuthResponse;
import com.shieldx.securities.dto.GithubCallbackRequest;
import com.shieldx.securities.dto.LoginRequest;
import com.shieldx.securities.dto.OtpRequest;
import com.shieldx.securities.dto.RegisterRequest;
import com.shieldx.securities.dto.ResetPasswordRequest;
import com.shieldx.securities.model.Login;
import com.shieldx.securities.repository.LoginRepository;
import com.shieldx.securities.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        Login login = loginRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), login.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        
        String token = jwtService.generateToken(login.getUser().getUserId());
        return ResponseEntity.ok(new AuthResponse(token, login.getUser().getUserId(), login.getEmail()));
    }
    
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequest request) {
        authService.sendOtp(request.getEmail());
        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpRequest request) {
        boolean verified = authService.verifyOtp(request.getEmail(), request.getOtpCode());
        return ResponseEntity.ok(verified ? "OTP verified successfully" : "Invalid OTP");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody OtpRequest request) {
        authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok("Password reset token sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getEmail(), request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successfully");
    }

    @PostMapping("/github/callback")
    public ResponseEntity<AuthResponse> githubCallback(@RequestBody GithubCallbackRequest request) {
        return ResponseEntity.ok(authService.githubLogin(request.getCode()));
    }
}