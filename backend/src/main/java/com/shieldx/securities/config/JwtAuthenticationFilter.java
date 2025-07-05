package com.shieldx.securities.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String jwt = authHeader.substring(BEARER_PREFIX.length());
            String userIdStr = jwtService.extractUserId(jwt); // Still a String from token

            if (userIdStr != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    Integer userId = Integer.parseInt(userIdStr); // Convert to Integer
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userId.toString());
                    if (jwtService.isTokenValid(jwt, userIdStr)) { // Validate with String for now
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        logger.info("Authenticated userId: {}", userId);
                    } else {
                        logger.warn("Invalid token for userId: {}", userId);
                    }
                } catch (NumberFormatException e) {
                    logger.error("Invalid userId format from token: {}", userIdStr, e);
                } catch (Exception e) {
                    logger.error("Authentication failed for userId: {}", userIdStr, e);
                }
            }
        } else {
            logger.debug("No valid Authorization header found");
        }
        filterChain.doFilter(request, response);
    }
}