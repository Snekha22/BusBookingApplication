package com.example.busbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.busbooking.entity.Route;
import com.example.busbooking.repository.RouteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    /**
     * Retrieves all bus routes.
     * @return A list of all Route entities.
     */
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    /**
     * Retrieves a route by its ID.
     * @param id The ID of the route.
     * @return An Optional containing the Route if found, otherwise empty.
     */
    public Optional<Route> getRouteById(Long id) {
        return routeRepository.findById(id);
    }

    /**
     * Searches for routes between a given origin and destination.
     * @param origin The starting location.
     * @param destination The ending location.
     * @return A list of matching Route entities.
     */
    public List<Route> searchRoutes(String origin, String destination) {
        return routeRepository.findByOriginIgnoreCaseAndDestinationIgnoreCase(origin, destination);
    }

    /**
     * Saves a new route or updates an existing one.
     * @param route The Route entity to save.
     * @return The saved Route entity.
     */
    @Transactional
    public Route saveRoute(Route route) {
        return routeRepository.save(route);
    }

    /**
     * Deletes a route by its ID.
     * @param id The ID of the route to delete.
     */
    @Transactional
    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }
}

