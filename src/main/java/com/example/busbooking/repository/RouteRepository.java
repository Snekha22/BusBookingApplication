package com.example.busbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.busbooking.entity.Route;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    // Custom query to find routes by origin and destination (case-insensitive)
    List<Route> findByOriginIgnoreCaseAndDestinationIgnoreCase(String origin, String destination);
}
