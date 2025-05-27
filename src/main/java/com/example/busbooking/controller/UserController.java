package com.example.busbooking.controller;

import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils; // For copying properties between DTO and Entity
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.busbooking.dto.ChangePasswordDTO;
import com.example.busbooking.dto.UserProfileUpdateDTO;
import com.example.busbooking.entity.User;
import com.example.busbooking.service.UserService;

import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Helper method to get the ID of the currently authenticated user.
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            String userEmail = authentication.getName(); // This is the email (username)
            return userService.getUserByEmail(userEmail).map(User::getId).orElse(null);
        }
        return null;
    }

    @GetMapping("/profile")
    public String userProfilePage(Model model, RedirectAttributes redirectAttributes) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return "redirect:/login";
        }

        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserProfileUpdateDTO userProfileUpdateDTO = new UserProfileUpdateDTO();
            BeanUtils.copyProperties(user, userProfileUpdateDTO); // Copy entity properties to DTO for form pre-population
            model.addAttribute("userProfileUpdateDTO", userProfileUpdateDTO);
            model.addAttribute("changePasswordDTO", new ChangePasswordDTO()); // Add empty DTO for password form
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User profile not found.");
            return "redirect:/login";
        }
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@Valid @ModelAttribute("userProfileUpdateDTO") UserProfileUpdateDTO profileDTO,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
            return "profile";
        }

        Optional<User> existingUserOptional = userService.getUserById(userId);
        if (existingUserOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found for update.");
            return "redirect:/profile";
        }

        User existingUser = existingUserOptional.get();
        existingUser.setName(profileDTO.getName());
        existingUser.setEmail(profileDTO.getEmail());

        User updatedUser = userService.updateUser(userId, existingUser);

        if (updatedUser != null) {
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        } else {
            bindingResult.rejectValue("email", "email.duplicate", "Email already registered by another user.");
            model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
            return "profile";
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@Valid @ModelAttribute("changePasswordDTO") ChangePasswordDTO passwordDTO,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            User user = userService.getUserById(userId).orElse(new User(null, null, null, null)); // This line now works with @NoArgsConstructor
            UserProfileUpdateDTO userProfileUpdateDTO = new UserProfileUpdateDTO();
            BeanUtils.copyProperties(user, userProfileUpdateDTO);
            model.addAttribute("userProfileUpdateDTO", userProfileUpdateDTO);
            return "profile";
        }

        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmNewPassword())) {
            bindingResult.rejectValue("confirmNewPassword", "password.mismatch", "New passwords do not match.");
            User user = userService.getUserById(userId).orElse(new User(null, null, null, null));
            UserProfileUpdateDTO userProfileUpdateDTO = new UserProfileUpdateDTO();
            BeanUtils.copyProperties(user, userProfileUpdateDTO);
            model.addAttribute("userProfileUpdateDTO", userProfileUpdateDTO);
            return "profile";
        }

        boolean changed = userService.changePassword(userId, passwordDTO.getNewPassword());

        if (changed) {
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to change password.");
        }
        return "redirect:/profile";
    }
}
