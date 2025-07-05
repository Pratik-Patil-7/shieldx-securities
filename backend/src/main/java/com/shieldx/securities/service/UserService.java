package com.shieldx.securities.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shieldx.securities.dto.ChangePasswordRequest;
import com.shieldx.securities.dto.UpdateProfileRequest;
import com.shieldx.securities.dto.UserResponse;
import com.shieldx.securities.model.Login;
import com.shieldx.securities.model.User;
import com.shieldx.securities.repository.LoginRepository;
import com.shieldx.securities.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private FileStorageService fileStorageService; // Assume service for file uploads

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserResponse getProfile(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponse(user.getUserId(), user.getFirstName(), user.getLastName(), 
                               user.getUsername(), user.getEmail(), user.getMobile(), user.getAddress());
    }

    public UserResponse updateProfile(Integer userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(request.getEmail());
            Login login = loginRepository.findByEmail(user.getEmail())
                    .orElseThrow(() -> new RuntimeException("Login not found"));
            login.setEmail(request.getEmail());
            loginRepository.save(login);
        }

        user.setFirstName(request.getFirstName() != null ? request.getFirstName() : user.getFirstName());
        user.setLastName(request.getLastName() != null ? request.getLastName() : user.getLastName());
        user.setMobile(request.getMobile() != null ? request.getMobile() : user.getMobile());
        user.setAddress(request.getAddress() != null ? request.getAddress() : user.getAddress());
        userRepository.save(user);

        return new UserResponse(user.getUserId(), user.getFirstName(), user.getLastName(), 
                               user.getUsername(), user.getEmail(), user.getMobile(), user.getAddress());
    }

    public void changePassword(Integer userId, ChangePasswordRequest request) {
        Login login = loginRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Login not found"));
        if (!passwordEncoder.matches(request.getCurrentPassword(), login.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        login.setPassword(passwordEncoder.encode(request.getNewPassword()));
        loginRepository.save(login);
    }

    public String uploadAvatar(Integer userId, MultipartFile file) {
        if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")) {
            throw new RuntimeException("Only JPEG/PNG files are allowed");
        }
        String fileName = "avatar_" + userId + "_" + System.currentTimeMillis();
        String photoUrl = fileStorageService.uploadFile(file, fileName);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return photoUrl;
    }
}