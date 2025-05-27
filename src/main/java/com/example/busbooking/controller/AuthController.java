package com.example.busbooking.controller;

import com.example.busbooking.dto.UserRegistrationDTO;
import com.example.busbooking.entity.User;
import com.example.busbooking.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Controller
public class AuthController {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/")
    public String showHomePage() {
        logger.info("Accessing home page at /");
        return "home"; // renders home.html from templates
    }
     @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password.");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out.");
        }
        return "login";
    }


    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userRegistrationDTO", new UserRegistrationDTO()); // Provide a new DTO for the form
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("userRegistrationDTO") UserRegistrationDTO registrationDTO,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            // If there are validation errors, return to the register page to display them
            return "register";
        }

        // Check if passwords match
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match.");
            return "register";
        }

        // Check if email already exists
        if (userService.getUserByEmail(registrationDTO.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "email.duplicate", "Email already registered.");
            return "register";
        }

        // Map DTO to Entity for service layer
        User newUser = new User(registrationDTO.getName(), registrationDTO.getEmail(), registrationDTO.getPassword(), "ROLE_USER");
        User registeredUser = userService.registerUser(newUser);

        if (registeredUser != null) {
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");
            return "redirect:/login";
        } else {
            // This case should ideally be caught by duplicate email check or validation,
            // but as a fallback for unexpected issues.
            redirectAttributes.addFlashAttribute("errorMessage", "Registration failed. Please try again.");
            return "redirect:/register";
        }
    }
}
