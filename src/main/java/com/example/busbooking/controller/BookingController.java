package com.example.busbooking.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.busbooking.dto.BookingRequestDTO;
import com.example.busbooking.dto.PassengerDTO;
import com.example.busbooking.entity.Booking;
import com.example.busbooking.entity.Passenger;
import com.example.busbooking.entity.Route;
import com.example.busbooking.entity.User;
import com.example.busbooking.service.BookingService;
import com.example.busbooking.service.RouteService;
import com.example.busbooking.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class BookingController {

    private final UserService userService;
    private final RouteService routeService;
    private final BookingService bookingService;

    @Autowired
    public BookingController(UserService userService, RouteService routeService, BookingService bookingService) {
        this.userService = userService;
        this.routeService = routeService;
        this.bookingService = bookingService;
    }

    /**
     * Helper method to get the ID of the currently authenticated user.
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            String userEmail = authentication.getName();
            return userService.getUserByEmail(userEmail).map(User::getId).orElse(null);
        }
        return null;
    }

    @GetMapping("/book-ticket")
    public String showBookingForm(@RequestParam("routeId") Long routeId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Route> selectedRoute = routeService.getRouteById(routeId);
        if (selectedRoute.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Selected bus route not found.");
            return "redirect:/search";
        }
        model.addAttribute("selectedRoute", selectedRoute.get());

        // Initialize BookingRequestDTO with one empty passenger for the form
        BookingRequestDTO bookingRequestDTO = new BookingRequestDTO();
        bookingRequestDTO.setRouteId(routeId);
        bookingRequestDTO.setPassengers(Collections.singletonList(new PassengerDTO()));
        model.addAttribute("bookingRequestDTO", bookingRequestDTO);

        return "booking-form";
    }

    @PostMapping("/confirm-booking")
    public String confirmBooking(@Valid @ModelAttribute("bookingRequestDTO") BookingRequestDTO bookingDTO,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Authentication required to book a ticket.");
            return "redirect:/login";
        }

        // Re-fetch route to ensure it's valid if we need to return to form
        Optional<Route> selectedRoute = routeService.getRouteById(bookingDTO.getRouteId());
        if (selectedRoute.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Selected bus route not found.");
            return "redirect:/search";
        }
        model.addAttribute("selectedRoute", selectedRoute.get()); // Re-add for error rendering

        if (bindingResult.hasErrors()) {
            // If validation errors, return to the form to display them
            return "booking-form";
        }

        // Map DTO passengers to Entity passengers
        List<Passenger> passengers = bookingDTO.getPassengers().stream()
                .map(dto -> new Passenger(dto.getName(), dto.getAge(), dto.getGender(), dto.getSeatPreference()))
                .collect(Collectors.toList());

        Booking newBooking = bookingService.createBooking(userId, bookingDTO.getRouteId(), passengers);

        if (newBooking != null) {
            redirectAttributes.addFlashAttribute("successMessage", "Booking confirmed! Your e-ticket has been sent.");
            return "redirect:/history";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking failed. Please try again. (Insufficient seats or other issue)");
            // Redirect back to the booking form with the selected route ID to retain context
            return "redirect:/book-ticket?routeId=" + bookingDTO.getRouteId();
        }
    }

    @GetMapping("/history")
    public String bookingHistoryPage(Model model) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return "redirect:/login";
        }
        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        model.addAttribute("bookings", bookings);
        return "history";
    }
}
