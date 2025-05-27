package com.example.busbooking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import java.util.List;


public class BookingRequestDTO {
    @NotNull(message = "Route ID is required")
    @Min(value = 1, message = "Route ID must be positive")
    private Long routeId;

    @Valid // This ensures validation is applied to each PassengerDTO in the list
    @NotNull(message = "At least one passenger is required")
    @Size(min = 1, message = "At least one passenger is required")
    private List<PassengerDTO> passengers;

	/**
	 * @return the routeId
	 */
	public Long getRouteId() {
		return routeId;
	}

	/**
	 * @param routeId the routeId to set
	 */
	public void setRouteId(Long routeId) {
		this.routeId = routeId;
	}

	/**
	 * @return the passengers
	 */
	public List<PassengerDTO> getPassengers() {
		return passengers;
	}

	/**
	 * @param passengers the passengers to set
	 */
	public void setPassengers(List<PassengerDTO> passengers) {
		this.passengers = passengers;
	}
    
    
}
