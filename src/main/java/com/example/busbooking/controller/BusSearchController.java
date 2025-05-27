package com.example.busbooking.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.busbooking.dto.BusSearchDTO;
import com.example.busbooking.entity.Route;
import com.example.busbooking.service.RouteService;

import java.util.List;
@Controller
public class BusSearchController {

    private final RouteService routeService;

    @Autowired
    public BusSearchController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/search")
    public String searchPage(Model model) {
        model.addAttribute("busSearchDTO", new BusSearchDTO()); // Provide a new DTO for the form
        return "search";
    }

    @PostMapping("/search-buses")
    public String searchBuses(@Valid @ModelAttribute("busSearchDTO") BusSearchDTO searchDTO,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "search"; // Return to search page to display validation errors
        }

        List<Route> filteredRoutes = routeService.searchRoutes(searchDTO.getFrom(), searchDTO.getTo());

        model.addAttribute("busRoutes", filteredRoutes);
        model.addAttribute("fromLocation", searchDTO.getFrom());
        model.addAttribute("toLocation", searchDTO.getTo());

        if (filteredRoutes.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "No buses found for the selected route.");
            return "redirect:/search"; // Redirect back to search page with message
        }

        return "bus-list";
    }
}
