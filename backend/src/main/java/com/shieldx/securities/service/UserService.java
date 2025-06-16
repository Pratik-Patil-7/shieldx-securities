package com.shieldx.securities.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.shieldx.securities.dto.UserDTO;
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
    private BCryptPasswordEncoder passwordEncoder;

    public String registerUser(UserDTO dto) {
        if (loginRepository.existsByUsername(dto.getUsername())) {
            return "Username already exists.";
        }

        if (loginRepository.existsByEmail(dto.getEmail())) {
            return "Email already registered.";
        }

     
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setMobile(dto.getMobile());
        user.setAddress(dto.getAddress());

        User savedUser = userRepository.save(user);

  
        Login login = new Login();
        login.setUser(savedUser);
        login.setUsername(dto.getUsername());
        login.setEmail(dto.getEmail());
        login.setPassword(passwordEncoder.encode(dto.getPassword())); 
        login.setLastLogin(LocalDateTime.now());
        login.setStatus("active");
        login.setFailedAttempts("0");

        loginRepository.save(login);

        return "User registered successfully!";
    }
    
    
	
}
