package com.shieldx.securities.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shieldx.securities.dto.LoginDTO;
import com.shieldx.securities.model.Login;
import com.shieldx.securities.repository.LoginRepository;

@Service
public class LoginService {

<<<<<<< HEAD
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final String STATUS_ACTIVE = "active";
    private static final String STATUS_DISABLED = "disabled";

    @Autowired
    public LoginService(LoginRepository loginRepository, PasswordEncoder passwordEncoder) {
        this.loginRepository = loginRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Login login(LoginDTO loginDTO) {
        // Validate input
        if (loginDTO == null || (isEmpty(loginDTO.getUsername()) && isEmpty(loginDTO.getEmail())) 
            || isEmpty(loginDTO.getPassword())) {
            throw new IllegalArgumentException("Username or email and password are required");
        }

        // Find login by username or email
        Optional<Login> loginOptional = isEmpty(loginDTO.getUsername())
                ? loginRepository.findByEmail(loginDTO.getEmail())
                : loginRepository.findByUsername(loginDTO.getUsername());

        if (!loginOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        Login login = loginOptional.get();

        // Check account status
        if (!login.getStatus().equalsIgnoreCase(STATUS_ACTIVE)) {
            throw new RuntimeException("Account is not active");
        }

        // Check failed attempts
        int failedAttempts = Integer.parseInt(login.getFailedAttempts());
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            login.setStatus(STATUS_DISABLED);
            loginRepository.save(login);
            throw new RuntimeException("Account is disabled due to too many failed login attempts");
        }

        // Verify password
        if (passwordEncoder.matches(loginDTO.getPassword(), login.getPassword())) {
            login.setFailedAttempts("0");
            login.setLastLogin(java.time.LocalDateTime.now());
            return loginRepository.save(login);
        } else {
            failedAttempts++;
            login.setFailedAttempts(String.valueOf(failedAttempts));
            loginRepository.save(login);
            throw new RuntimeException("Invalid password. Attempt " + failedAttempts + " of " + MAX_FAILED_ATTEMPTS);
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
=======
	private PasswordEncoder passwordEncoder;	
	private LoginRepository loginRepository;
	@Autowired
	public LoginService(LoginRepository loginRepository,PasswordEncoder passwordEncoder) {
		this.loginRepository = loginRepository;
		this.passwordEncoder = passwordEncoder;
	}
	public Login login(LoginDTO loginDTO) {
		if (loginDTO.getUsername() != null && !loginDTO.getUsername().isEmpty()) {
			Optional<Login> validLogin = loginRepository.findByUsername(loginDTO.getUsername());
			if(validLogin.isPresent()) {	
				Login login = validLogin.get();
				if(login.getStatus().equalsIgnoreCase("active")) {
					if(Integer.valueOf(login.getFailedAttempts())<5) {
						if ( passwordEncoder.matches(loginDTO.getPassword(), validLogin.get().getPassword())) {
							login.setFailedAttempts("0");
							return loginRepository.save(login);
						}else {
							int faildAttempt = Integer.valueOf(login.getFailedAttempts())+1;
							login.setFailedAttempts(String.valueOf(faildAttempt));
							return loginRepository.save(login);
						}
					}else {
						login.setStatus("Disable");
						return loginRepository.save(login);
					}
				}
			}
		} else if (loginDTO.getEmail() != null && !loginDTO.getEmail().isEmpty()) {
			Login validLogin = loginRepository.findByEmail(loginDTO.getEmail());
			if(validLogin != null && validLogin.getStatus().equalsIgnoreCase("Active")) {
				if(Integer.valueOf(validLogin.getFailedAttempts())<5) {	
					if ( passwordEncoder.matches(loginDTO.getPassword(), validLogin.getPassword())) {
						validLogin.setFailedAttempts("0");
						return loginRepository.save(validLogin);
					}else {
						int faildAttempt = Integer.valueOf(validLogin.getFailedAttempts())+1;
						validLogin.setFailedAttempts(String.valueOf(faildAttempt));
						return loginRepository.save(validLogin);
					}
				}else {
					validLogin.setStatus("Disable");
					return loginRepository.save(validLogin);
				}
			}	
		}
		return null;
	}
}
>>>>>>> 6ff7aa185b303b4620bebe1bd219edf3445c916e
