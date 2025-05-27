package com.example.busbooking.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PassengerDTO {
    @NotBlank(message = "Passenger name is required")
    private String name;

    @NotNull(message = "Passenger age is required")
    @Min(value = 1, message = "Age must be at least 1")
    private Integer age; // Use Integer for nullable validation

    @NotBlank(message = "Gender is required")
    private String gender; // e.g., "Male", "Female", "Other"

    @NotBlank(message = "Seat preference is required")
    private String seatPreference; // e.g., "Window", "Aisle", "Any"

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the age
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the seatPreference
	 */
	public String getSeatPreference() {
		return seatPreference;
	}

	/**
	 * @param seatPreference the seatPreference to set
	 */
	public void setSeatPreference(String seatPreference) {
		this.seatPreference = seatPreference;
	}

    // Removed passenger email as it's typically tied to the booking user's email or specific to the passenger
    // If needed, add it back but consider if it's truly unique per passenger or just for communication.
    // private String email;
    
    
}