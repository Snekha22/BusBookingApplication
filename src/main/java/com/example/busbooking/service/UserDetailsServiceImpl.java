package com.example.busbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.busbooking.entity.User;
import com.example.busbooking.repository.UserRepository;

import java.util.Collections; // For simple authorities list

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Spring will inject this

    /**
     * Locates the user based on the username (email in this case).
     * This method is used by Spring Security to load user details during authentication.
     * @param email The email (username) identifying the user whose data is required.
     * @return A UserDetails object (Spring Security's user representation).
     * @throws UsernameNotFoundException if the user could not be found or has no granted authorities.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Retrieve the user from your UserRepository based on the email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Build Spring Security's UserDetails object from your User entity
        // Note: For simplicity, we're giving a single role. In a real app, you'd parse user.getRoles()
        // and convert them into GrantedAuthority objects.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(), // Hashed password
                Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority(user.getRoles()))
        );
    }
}
