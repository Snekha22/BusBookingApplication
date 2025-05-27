package com.example.busbooking.entity;

import jakarta.persistence.*;


import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "buses")

public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String busNumber;
    @Column(nullable = false)
    private int capacity;
    @Column(nullable = false)
    private String type; // e.g., "AC", "Non-AC", "Sleeper"
    @Column(nullable = false)
    private String operator; // e.g., "Swift Travels"

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Route> routes = new HashSet<>();

    public Bus() {}
    // Custom constructor for creating new Bus objects
    public Bus(String busNumber, int capacity, String type, String operator) {
        this.busNumber = busNumber;
        this.capacity = capacity;
        this.type = type;
        this.operator = operator;
    }

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
	 * @return the busNumber
	 */
	public String getBusNumber() {
		return busNumber;
	}

	/**
	 * @param busNumber the busNumber to set
	 */
	public void setBusNumber(String busNumber) {
		this.busNumber = busNumber;
	}

	/**
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * @param capacity the capacity to set
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @return the routes
	 */
	public Set<Route> getRoutes() {
		return routes;
	}

	/**
	 * @param routes the routes to set
	 */
	public void setRoutes(Set<Route> routes) {
		this.routes = routes;
	}

    
    
}
