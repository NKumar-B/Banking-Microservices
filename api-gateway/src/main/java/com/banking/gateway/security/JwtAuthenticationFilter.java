package com.banking.gateway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.HttpServletRequestWrapper;

import org.springframework.http.HttpMethod;

import java.util.List;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        String method = request.getMethod();

        // Public authentication APIs
        if (requestPath.equals("/auth-service/auth/register")
                || requestPath.equals("/auth-service/auth/login")) {

            filterChain.doFilter(request, response);
            return;
        }

        // Get Authorization header
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            response.getWriter().write(
                    "{\"error\":\"Unauthorized\",\"message\":\"JWT token is required\"}"
            );

            return;
        }

        // Extract token
        String token = authHeader.substring(7);

        // Validate token
        if (!jwtService.isTokenValid(token)) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            response.getWriter().write(
                    "{\"error\":\"Unauthorized\",\"message\":\"Invalid or expired JWT token\"}"
            );

            return;
        }

        // Extract user information from JWT
        String email = jwtService.extractEmail(token);
        String role = jwtService.extractRole(token);
        Long userId = jwtService.extractUserId(token);

        // ============================
        // RBAC RULES
        // ============================

        boolean authorized = true;

        // CUSTOMER SERVICE
        // Only ADMIN can access
        if (requestPath.startsWith("/customer-service/")) {

            if (!role.equals("ADMIN")) {
                authorized = false;
            }
        }

        // ACCOUNT SERVICE
        else if (requestPath.startsWith("/account-service/")) {

            // GET → CUSTOMER + ADMIN
            if (method.equals(HttpMethod.GET.name())) {

                authorized = role.equals("CUSTOMER")
                        || role.equals("ADMIN");

            } else {
                // POST, PUT, DELETE → ADMIN only
                authorized = role.equals("ADMIN");
            }
        }

        // TRANSACTION SERVICE
        else if (requestPath.startsWith("/transaction-service/")) {

            // GET and POST → CUSTOMER + ADMIN
            if (method.equals(HttpMethod.GET.name())
                    || method.equals(HttpMethod.POST.name())) {

                authorized = role.equals("CUSTOMER")
                        || role.equals("ADMIN");

            } else {
                // PUT and DELETE → ADMIN only
                authorized = role.equals("ADMIN");
            }
        }

        // Check authorization
        if (!authorized) {

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");

            response.getWriter().write(
                    "{\"error\":\"Forbidden\",\"message\":\"You do not have permission to access this resource\"}"
            );

            return;
        }



        // Continue request
        HttpServletRequest wrappedRequest =
                new HttpServletRequestWrapper(request) {

                    @Override
                    public String getHeader(String name) {

                        if ("X-User-Id".equalsIgnoreCase(name)) {
                            return String.valueOf(userId);
                        }

                        if ("X-User-Role".equalsIgnoreCase(name)) {
                            return role;
                        }

                        return super.getHeader(name);
                    }
                };

        filterChain.doFilter(wrappedRequest, response);
    }
}