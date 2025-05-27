package com.example.busbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.busbooking.entity.Booking;
import com.example.busbooking.entity.Passenger;
import com.example.busbooking.entity.Route;
import com.example.busbooking.entity.User;
import com.example.busbooking.repository.BookingRepository;
import com.example.busbooking.repository.RouteRepository;
import com.example.busbooking.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RouteRepository routeRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, UserRepository userRepository,
                          RouteRepository routeRepository) { // PassengerRepository is not directly needed here
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.routeRepository = routeRepository;
    }

    /**
     * Creates a new booking for a user on a specific route.
     * @param userId The ID of the user making the booking.
     * @param routeId The ID of the route being booked.
     * @param passengerDetails A list of Passenger objects for this booking.
     * @return The created Booking entity, or null if user/route not found.
     */
    @Transactional
    public Booking createBooking(Long userId, Long routeId, List<Passenger> passengerDetails) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Route> routeOptional = routeRepository.findById(routeId);

        if (userOptional.isEmpty() || routeOptional.isEmpty()) {
            System.err.println("User or Route not found for booking.");
            return null; // Or throw a specific exception like ResourceNotFoundException
        }

        User user = userOptional.get();
        Route route = routeOptional.get();

        // Basic validation for available seats (optional, more complex logic needed for real seats)
        // For simplicity, we assume enough capacity. In a real system, you'd manage seat availability.
        if (route.getBus().getCapacity() < passengerDetails.size()) {
            System.err.println("Not enough seats available for this booking.");
            return null; // Or throw a specific exception
        }

        double totalAmount = route.getPrice() * passengerDetails.size();

        Booking booking = new Booking(null, totalAmount, null, user, route);
        booking.setBookingDate(LocalDateTime.now());
        booking.setTotalAmount(totalAmount);
        booking.setStatus("CONFIRMED"); // Default status, can be "PENDING" for payment
        booking.setUser(user);
        booking.setRoute(route);

        // Add passengers to the booking. The cascade will save them with the booking.
        for (Passenger passenger : passengerDetails) {
            booking.addPassenger(passenger);
        }

        return bookingRepository.save(booking);
    }

    /**
     * Retrieves a booking by its ID.
     * @param id The ID of the booking.
     * @return An Optional containing the Booking if found, otherwise empty.
     */
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    /**
     * Retrieves all bookings for a specific user.
     * @param userId The ID of the user.
     * @return A list of Booking entities for the given user.
     */
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUser_Id(userId);
    }

    /**
     * Updates the status of a booking.
     * @param bookingId The ID of the booking to update.
     * @param newStatus The new status (e.g., "CANCELLED").
     * @return The updated Booking, or null if not found.
     */
    @Transactional
    public Booking updateBookingStatus(Long bookingId, String newStatus) {
        return bookingRepository.findById(bookingId).map(booking -> {
            booking.setStatus(newStatus);
            return bookingRepository.save(booking);
        }).orElse(null);
    }
}
