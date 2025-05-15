package com.example.etb.service.authModules;


import com.example.etb.model.UserModel;
import com.example.etb.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final HttpSession httpSession;


    public CustomUserDetailsService(UserRepository userRepository, HttpSession httpSession) {
        this.userRepository = userRepository;
        this.httpSession = httpSession;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Find the user by email
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Map user role to GrantedAuthority
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());
        // Log the user details (for debugging purposes)

        System.out.println("User Found: ");
        System.out.println("Email: " + user.getEmail());
        System.out.println("Password: " + user.getPassword());
        System.out.println("userId: " + user.getId());
//        System.out.println(user.getRole().getName().toUpperCase());
        // Add user data to session
        httpSession.setAttribute("userId", user.getId());
        httpSession.setAttribute("userEmail", user.getEmail());
//        httpSession.setAttribute("userRole", user.getRole().getName());

        // Return UserDetails with plain text password
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(), // Plain text password
                Collections.singleton(authority)
        );
    }
}