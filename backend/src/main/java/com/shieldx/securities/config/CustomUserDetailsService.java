package com.shieldx.securities.config;

import com.shieldx.securities.model.Login;
import com.shieldx.securities.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private LoginRepository loginRepository;

    @Override
    public UserDetails loadUserByUsername(String userIdStr) throws UsernameNotFoundException {
        try {
            Integer userId = Integer.parseInt(userIdStr);
            Login login = loginRepository.findByUserId(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
            String role = login.getEmail().toLowerCase().contains("admin") ? "ADMIN" : "USER"; // Case-insensitive
            return new User(
                    login.getUser().getUserId().toString(), // Ensure getUser() is safe
                    login.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
            );
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Invalid userId format: " + userIdStr, e);
        }
    }
}