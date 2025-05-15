package com.example.etb.controller.CustomerModule;

import com.example.etb.model.CustomerModule.BookingModel;
import com.example.etb.model.EventOrganizerModule.EventModel;
import com.example.etb.model.UserModel;
import com.example.etb.service.CustomerModule.BookingService;
import com.example.etb.service.EventOrganizerModule.EventService;
import com.example.etb.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/")
    public String index(Model model) {
        return "CustomerModule/home";
    }
    @GetMapping("/customer/dashboard")
    public String customerDashboard(Model model) {
        return "CustomerModule/home";
    }

    @GetMapping("/customer/my-bookings")
    public String myBookings(Model model, HttpSession session) {
        // Check if the customer is logged in
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            // Fetch the userId from the session
            Long userId = (Long) session.getAttribute("userId");

            if (userId != null) {
                // Assuming you have a method to fetch the user by ID
                UserModel user = userService.getUserById(userId);

                if (user != null) {
                    // Fetch the bookings for this user
                    List<BookingModel> bookings = bookingService.getBookingsByUser(user);

                    // Add the bookings to the model
                    model.addAttribute("bookings", bookings);
                } else {
                    // If user not found in database, redirect to login
                    return "redirect:/login";
                }
            } else {
                // If userId is not found in session, redirect to login
                return "redirect:/login";
            }

            // Show the bookings page with the customer's bookings
            return "CustomerModule/my-bookings"; // Booking page where customer can view their tickets
        } else {
            // Customer is not logged in, redirect to login page
            return "redirect:/login";
        }
    }


    @GetMapping("/customer/browse-events")
    public String browseEvents(Model model) {
        // Check if the customer is logged in
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            // Fetch all events
            model.addAttribute("events", eventService.getAllEvents());
            return "CustomerModule/browse-events"; // Event browsing page
        } else {
            return "redirect:/login"; // Redirect to login page if not authenticated
        }
    }
}
