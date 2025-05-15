package com.example.etb.Constant.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Check the user's roles and redirect accordingly
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            response.sendRedirect("/admin/dashboard"); // Redirect to admin dashboard
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("EVENT_ORGANIZER"))) {
            response.sendRedirect("/eventorg/dashboard"); // Redirect to faculty dashboard
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("CUSTOMER"))) {
            response.sendRedirect("/customer/dashboard"); // Redirect to faculty dashboard
        } else {
            response.sendRedirect("/default"); // Default fallback
        }
    }
}