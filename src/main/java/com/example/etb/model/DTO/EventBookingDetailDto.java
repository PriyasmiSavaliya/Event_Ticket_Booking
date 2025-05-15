package com.example.etb.model.DTO;

import com.example.etb.model.CustomerModule.BookingModel;
import com.example.etb.model.EventOrganizerModule.EventModel;

import java.util.List;

public class EventBookingDetailDto {

    private EventModel event;
    private List<BookingModel> bookings;

    // Getters and Setters
    public EventModel getEvent() {
        return event;
    }

    public void setEvent(EventModel event) {
        this.event = event;
    }

    public List<BookingModel> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingModel> bookings) {
        this.bookings = bookings;
    }

}
