package com.example.busbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.busbooking.entity.Bus;
import com.example.busbooking.repository.BusRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BusService {

    private final BusRepository busRepository;

    @Autowired
    public BusService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    /**
     * Retrieves all buses.
     * @return A list of all Bus entities.
     */
    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    /**
     * Retrieves a bus by its ID.
     * @param id The ID of the bus.
     * @return An Optional containing the Bus if found, otherwise empty.
     */
    public Optional<Bus> getBusById(Long id) {
        return busRepository.findById(id);
    }

    /**
     * Saves a new bus or updates an existing one.
     * @param bus The Bus entity to save.
     * @return The saved Bus entity.
     */
    @Transactional
    public Bus saveBus(Bus bus) {
        // Optional: Check for unique busNumber before saving
        if (bus.getId() == null && busRepository.findByBusNumber(bus.getBusNumber()).isPresent()) {
            System.err.println("Bus with number " + bus.getBusNumber() + " already exists.");
            return null; // Or throw an exception
        }
        return busRepository.save(bus);
    }

    /**
     * Deletes a bus by its ID.
     * @param id The ID of the bus to delete.
     */
    @Transactional
    public void deleteBus(Long id) {
        busRepository.deleteById(id);
    }
}
