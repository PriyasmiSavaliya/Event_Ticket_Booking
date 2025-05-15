package com.example.etb.Constant.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {
//
    private final AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    public SecurityConfig(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**", "/assets/**").permitAll() // Public resources
                        .requestMatchers("/login", "/register").permitAll() // Public login and register endpoints
                        .requestMatchers("/","customer/my-bookings","customer/dashboard","customer/browse-events","customer/book-event/").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN") // Require admin role for admin paths
                        .requestMatchers("/eventorg/**").hasAuthority("EVENT_ORGANIZER")
                        .requestMatchers("/customer/**").hasAuthority("CUSTOMER")// Require faculty role for faculty paths
                        .anyRequest().authenticated() // All other requests must be authenticated
                )
                .formLogin(form -> form
                        .loginPage("/login") // Custom login page
                        .successHandler(customAuthenticationSuccessHandler) // Custom success handler
                        .permitAll() // Allow everyone to access the login page
                )
                .csrf(csrf -> csrf.disable()) // Disable CSRF (not recommended for production unless justified)
                .logout(logout -> logout
                        .logoutUrl("/logout") // The URL that triggers logout
                        .logoutSuccessUrl("/login?logout") // Redirect after successful logout
                        .invalidateHttpSession(true) // Invalidate the HTTP session
                        .deleteCookies("JSESSIONID", "other-cookie-name") // Delete specific cookies
                        .permitAll() // Allow logout for everyone
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("uniqueAndSecret") // Secret key for remember-me token
                        .tokenValiditySeconds(7 * 24 * 60 * 60) // 7 days token validity
                        .rememberMeParameter("remember-me") // Checkbox name for remember-me
                );


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
