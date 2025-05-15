package com.example.etb.controller.CustomerModule;

import com.example.etb.model.CustomerModule.BookingModel;
import com.example.etb.model.EventOrganizerModule.EventModel;

import com.example.etb.model.UserModel;
import com.example.etb.service.CustomerModule.BookingService;
import com.example.etb.service.EventOrganizerModule.EventService;
import com.example.etb.service.UserService;
import com.example.etb.util.EmailUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    // Injecting HttpSession to access the session
    @Autowired
    private HttpSession session;



    @GetMapping("/customer/book-event/{id}")
    public String showBookingForm(@PathVariable Long id, Model model) {
        // Fetch the event details
        EventModel event = eventService.getEventById(id);
        model.addAttribute("event", event);
        return "CustomerModule/book-event"; // Booking form page
    }

    @PostMapping("/customer/book-event/{id}")
    public String bookEvent(@PathVariable Long id,
                            @RequestParam Integer quantity,
                            Model model) {
        // Get the logged-in user ID from session
        Long userId = (Long) session.getAttribute("userId"); // Assuming userId is stored in the session

        if (userId == null) {
            model.addAttribute("error", "You need to be logged in to book an event.");
            return "/login"; // Redirect to login page if userId is not found in session
        }

        try {
            // Get the user by ID (you may need to modify your service method to support this)
            UserModel user = userService.getUserById(userId); // Assuming the user service has a method to get user by ID



            // Book the event
            BookingModel booking = bookingService.bookEvent(id, user.getId(), quantity);
            model.addAttribute("booking", booking);
            return "CustomerModule/booking-confirmation"; // Booking confirmation page
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "CustomerModule/book-event"; // Redirect back to the booking form with error
        }
    }
}
