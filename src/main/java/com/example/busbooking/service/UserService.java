package com.example.busbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.busbooking.entity.User;
import com.example.busbooking.repository.UserRepository;

import java.util.Optional;



@Service
public class UserService {

    private final UserRepository userRepository;
    // Corrected: Inject the PasswordEncoder interface instead of the concrete BCryptPasswordEncoder
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) { // Changed type here
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            System.err.println("User with email " + user.getEmail() + " already exists.");
            return null;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            if (!user.getEmail().equalsIgnoreCase(updatedUser.getEmail()) && userRepository.existsByEmail(updatedUser.getEmail())) {
                 System.err.println("Email " + updatedUser.getEmail() + " is already taken by another user.");
                 return null;
            }
            user.setEmail(updatedUser.getEmail());
            return userRepository.save(user);
        }).orElse(null);
    }

    @Transactional
    public boolean changePassword(Long id, String newPassword) {
        return userRepository.findById(id).map(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }).orElse(false);
    }
}
