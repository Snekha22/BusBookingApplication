package com.example.busbooking;

import java.time.LocalTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.example.busbooking.entity.Bus;
import com.example.busbooking.entity.Route;
import com.example.busbooking.entity.User;
import com.example.busbooking.service.BusService;
import com.example.busbooking.service.RouteService;
import com.example.busbooking.service.UserService;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.busbooking")
public class BusBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusBookingApplication.class, args);
	}
	
	  // This CommandLineRunner will execute after the application context is loaded.
    @Bean
    public CommandLineRunner demoData(UserService userService, BusService busService, RouteService routeService) {
        return args -> {
            // Create a default user if not exists
            if (userService.getUserByEmail("user@example.com").isEmpty()) {
                User mockUser = new User("John Doe", "user@example.com", "password123", "ROLE_USER");
                userService.registerUser(mockUser); // Password will be hashed by UserService
                System.out.println("Mock user created: user@example.com / password123");
            }

            // Create some sample buses and routes if not already present
            if (routeService.getAllRoutes().isEmpty()) {
                System.out.println("Populating mock bus and route data...");
                // Save buses first to get their IDs
                Bus bus1 = busService.saveBus(new Bus("KA01-A1234", 40, "AC", "Swift Travels"));
                Bus bus2 = busService.saveBus(new Bus("TN02-B5678", 35, "Non-AC", "Blue Line"));
                Bus bus3 = busService.saveBus(new Bus("KL03-C9012", 50, "Sleeper", "Green Express"));

                if (bus1 != null && bus2 != null && bus3 != null) {
                    routeService.saveRoute(new Route("Bangalore", "Chennai", LocalTime.of(8, 0), LocalTime.of(15, 0), 750.0, bus1));
                    routeService.saveRoute(new Route("Bangalore", "Chennai", LocalTime.of(10, 0), LocalTime.of(17, 0), 820.0, bus2));
                    routeService.saveRoute(new Route("Bangalore", "Coimbatore", LocalTime.of(9, 30), LocalTime.of(16, 30), 600.0, bus3));
                    routeService.saveRoute(new Route("Hyderabad", "Chennai", LocalTime.of(7, 0), LocalTime.of(14, 0), 900.0, bus1));
                    System.out.println("Mock routes created.");
                } else {
                    System.err.println("Failed to save one or more mock buses. Routes might not be created.");
                }
            }
        };
    }
}



