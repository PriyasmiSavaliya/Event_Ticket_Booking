package com.example.etb.service.CustomerModule;

import com.example.etb.model.CustomerModule.BookingModel;
import com.example.etb.model.EventOrganizerModule.EventModel;
import com.example.etb.model.UserModel;

import com.example.etb.repository.CustomerModule.BookingRepository;
import com.example.etb.repository.EventOrganizerModule.EventRepository;
import com.example.etb.repository.UserRepository;
import com.example.etb.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailUtil emailUtil;

    public BookingModel bookEvent(Long eventId, Long userId, Integer ticketQuantity) {
        // Fetch event and user from the repositories
        EventModel event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if enough tickets are available
        if (event.getTotalTicket() < ticketQuantity) {
            throw new RuntimeException("Not enough tickets available");
        }

        // Calculate total price (assuming each ticket is the price set for the event)
        Double totalPrice = (double) (ticketQuantity * event.getEventPrice());

        // Create and save the booking
        BookingModel booking = new BookingModel();
        booking.setEvent(event);
        booking.setUser(user);
        booking.setTicketQuantity(ticketQuantity);
        booking.setTotalPrice(totalPrice);
        booking.setStatus("Booked");

        // Update the event's available tickets
        event.setTotalTicket(event.getTotalTicket() - ticketQuantity);
        eventRepository.save(event);

        // Save the booking in the repository
        BookingModel savedBooking = bookingRepository.save(booking);

        // Construct email subject and body for event booking confirmation
        String subject = "Event Booking Confirmation - " + event.getEventName();
        String body = "Dear " + user.getName() + ",\n\n" +
                "Thank you for booking tickets for the event: " + event.getEventName() + "!\n\n" +
                "Here are your booking details:\n\n" +
                "Event Name: " + event.getEventName() + "\n" +
                "Event Date: " + event.getEventDate() + "\n" +
                "Venue: " + event.getEventAddress() + "\n\n" +
                "Booking ID: " + savedBooking.getId() + "\n" +  // Use the saved booking's ID
                "Number of Tickets: " + ticketQuantity + "\n" +
                "Total Price: $" + totalPrice + "\n\n" +
                "Please keep this information safe. You will receive further updates regarding the event as the date approaches.\n\n" +
                "Best Regards,\nYour Event Team";

        // Send email using email utility
        emailUtil.sendSimpleEmail(user.getEmail(), subject, body);

        // Return the saved booking object
        return savedBooking;
    }


    public List<BookingModel> getBookingsByUser(UserModel user) {
        // Fetch bookings for the given user
        return bookingRepository.findByUser(user);
    }

    public List<BookingModel> getBookingsForEvent(EventModel event, Long orgId, String status) {
        // Call the repository method to fetch bookings for the given event, organization, and status
        return bookingRepository.findBookingsForEventAndOrganizationWithStatusAndOrdering(event.getId(), orgId , status);
    }

}
