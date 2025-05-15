package com.example.etb.controller.EventOrganizerModule;

import com.example.etb.Constant.Config.PathConstant;
import com.example.etb.model.CustomerModule.BookingModel;
import com.example.etb.model.DTO.EventBookingDetailDto;
import com.example.etb.model.EventOrganizerModule.EventModel;
import com.example.etb.service.CustomerModule.BookingService;
import com.example.etb.service.EventOrganizerModule.EventService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(PathConstant.EVENT_ORG_PATH)
public class EventOrgDashboardController {

    @Autowired
    private EventService eventService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/dashboard")
    public String EventOrganizerModule() {
        return "EventOrganizerModule/event-organisers-dashboard";
    }

    @GetMapping("/event/view-bookings")
    public String viewEventBookings(HttpSession session, Model model) {
        // Fetch the orgId from the session
        Long orgId = (Long) session.getAttribute("userId");

        // Ensure the orgId is present in the session
        if (orgId != null) {
            // Fetch all events for the given organization
            List<EventModel> events = eventService.getEventsByOrganization(orgId);

            System.out.println(events);
            if (events != null && !events.isEmpty()) {
                // Create a list to store booking details for each event
                List<EventBookingDetailDto> eventBookingDetails = new ArrayList<>();

                // For each event, fetch the bookings and user details
                for (EventModel event : events) {
                    // Fetch bookings for the event
                    List<BookingModel> bookings = bookingService.getBookingsForEvent(event, orgId, "Booked");

                    // Create a detail object for each event's bookings
                    EventBookingDetailDto eventDetail = new EventBookingDetailDto();
                    eventDetail.setEvent(event);
                    eventDetail.setBookings(bookings);

                    // Add the event details to the list
                    eventBookingDetails.add(eventDetail);
                }

                // Add the events and eventBookingDetails to the model
                model.addAttribute("eventBookingDetails", eventBookingDetails);
                model.addAttribute("organizationId", orgId);

                // Return the view for displaying bookings
                return "EventOrganizerModule/view-bookings"; // Thymeleaf page to display the bookings
            } else {
                // If no events found for the organization
                model.addAttribute("message", "No events found for this organization.");
                return "EventOrganizerModule/noEventsFound"; // A view to show when no events are found
            }
        } else {
            // If orgId is not found in the session, return an error page
            return "error/404";
        }
    }


    @GetMapping("/event/view-booking/{id}")
    public String viewEventBooking(HttpSession session, Model model) {
        // Fetch the orgId from the session
        Long orgId = (Long) session.getAttribute("userId");

        // Ensure the orgId is present in the session
        if (orgId != null) {
            // Fetch all events for the given organization
            List<EventModel> events = eventService.getEventsByOrganization(orgId);

            System.out.println(events);
            if (events != null && !events.isEmpty()) {
                // Create a list to store booking details for each event
                List<EventBookingDetailDto> eventBookingDetails = new ArrayList<>();

                // For each event, fetch the bookings and user details
                for (EventModel event : events) {
                    // Fetch bookings for the event
                    List<BookingModel> bookings = bookingService.getBookingsForEvent(event, orgId, "Booked");

                    // Create a detail object for each event's bookings
                    EventBookingDetailDto eventDetail = new EventBookingDetailDto();
                    eventDetail.setEvent(event);
                    eventDetail.setBookings(bookings);

                    // Add the event details to the list
                    eventBookingDetails.add(eventDetail);
                }

                // Add the events and eventBookingDetails to the model
                model.addAttribute("eventBookingDetails", eventBookingDetails);
                model.addAttribute("organizationId", orgId);

                // Return the view for displaying bookings
                return "EventOrganizerModule/view-booking"; // Thymeleaf page to display the bookings
            } else {
                // If no events found for the organization
                model.addAttribute("message", "No events found for this organization.");
                return "EventOrganizerModule/noEventsFound"; // A view to show when no events are found
            }
        } else {
            // If orgId is not found in the session, return an error page
            return "error/404";
        }
    }



}
