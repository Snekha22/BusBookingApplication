package com.example.busbooking.dto;


import jakarta.validation.constraints.NotBlank;

public class BusSearchDTO {
    @NotBlank(message = "Origin is required")
    private String from;

    @NotBlank(message = "Destination is required")
    private String to;

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}
    
    
}
