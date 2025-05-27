package com.example.busbooking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "bookings")

public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime bookingDate;
    @Column(nullable = false)
    private double totalAmount;
    @Column(nullable = false)
    private String status; // e.g., "CONFIRMED", "PENDING", "CANCELLED"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Many bookings can be made by one user

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route; // Many bookings can be for one route

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Passenger> passengers = new HashSet<>();
    @ElementCollection
    @CollectionTable(name = "booking_seats", joinColumns = @JoinColumn(name = "booking_id"))
    @Column(name = "seat_number")
    private List<Integer> seatNumbers;

    // Custom constructor for creating new Booking objects
    public Booking(LocalDateTime bookingDate, double totalAmount, String status, User user, Route route) {
        this.bookingDate = bookingDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.user = user;
        this.route = route;
    }
    
    public Booking() {}
    
/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @return the bookingDate
	 */
	public LocalDateTime getBookingDate() {
		return bookingDate;
	}


	/**
	 * @param bookingDate the bookingDate to set
	 */
	public void setBookingDate(LocalDateTime bookingDate) {
		this.bookingDate = bookingDate;
	}


	/**
	 * @return the totalAmount
	 */
	public double getTotalAmount() {
		return totalAmount;
	}


	/**
	 * @param totalAmount the totalAmount to set
	 */
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}


	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}


	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}


	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}


	/**
	 * @return the route
	 */
	public Route getRoute() {
		return route;
	}


	/**
	 * @param route the route to set
	 */
	public void setRoute(Route route) {
		this.route = route;
	}


	/**
	 * @return the passengers
	 */
	public Set<Passenger> getPassengers() {
		return passengers;
	}


	/**
	 * @param passengers the passengers to set
	 */
	public void setPassengers(Set<Passenger> passengers) {
		this.passengers = passengers;
	}


	// Helper method to add passenger and maintain bidirectional relationship
    public void addPassenger(Passenger passenger) {
        this.passengers.add(passenger);
        passenger.setBooking(this);
    }

    // Helper method to remove passenger and maintain bidirectional relationship
    public void removePassenger(Passenger passenger) {
        this.passengers.remove(passenger);
        passenger.setBooking(null);
    }
}
