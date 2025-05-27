package com.example.busbooking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.busbooking.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity // Enables Spring Security's web security support
@EnableMethodSecurity // Enables method-level security (e.g., @PreAuthorize)
public class SecurityConfig {

    // Defines the UserDetailsService bean, which Spring Security uses to load user-specific data.
    // We are using our custom UserDetailsServiceImpl.
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    // Defines the password encoder bean. BCryptPasswordEncoder is recommended for strong password hashing.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configures the AuthenticationProvider. It uses DaoAuthenticationProvider to retrieve user details
    // from the UserDetailsService and verify passwords using the PasswordEncoder.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    // Configures the security filter chain, defining authorization rules for HTTP requests.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for simplicity in this example (consider enabling in production)
            .authorizeHttpRequests(authorize -> authorize
                // Allow unauthenticated access to login, register, and static resources (like CSS/JS)
                .requestMatchers("/","/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
            	    .loginPage("/login")
            	    .usernameParameter("username") // Use this if you want to keep name="email"
            	    .passwordParameter("password")
            	    .loginProcessingUrl("/login")
            	    .defaultSuccessUrl("/search", true)
            	    .failureUrl("/login?error=true")
            	    .permitAll()
            	)

            .logout(logout -> logout
                .logoutUrl("/logout") // The URL to trigger logout
                .logoutSuccessUrl("/login?logout=true") // Redirect to /login?logout after successful logout
                .permitAll()
            );
        return http.build();
    }
    
}
