package com.example.etb.service.EventOrganizerModule;

import com.example.etb.model.EventOrganizerModule.EventModel;
import com.example.etb.model.UserModel;
import com.example.etb.repository.EventOrganizerModule.EventRepository;
import com.example.etb.repository.UserRepository;
import com.example.etb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserService userService;

    public EventModel saveEvent(EventModel event, Long eventOrganizerId) {
        if (eventOrganizerId == null) {
            throw new RuntimeException("Event Organizer ID is required!");
        }
        UserModel organizer = userService.getUserById(eventOrganizerId);
        event.setEventOrganizer(organizer);
        return eventRepository.save(event);
    }

    // Get events by organizer ID
    public List<EventModel> getEventsByOrganizerId(Long eventOrganizerId) {
        return eventRepository.findByEventOrganizerId(eventOrganizerId);
    }

    // Get all events
    public List<EventModel> getAllEvents() {
        return eventRepository.findAll();
    }

    // Get an event by ID
    public EventModel getEventById(Long id) {
        Optional<EventModel> event = eventRepository.findById(id);
        return event.orElse(null);
    }

    // Delete an event by ID
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public List<EventModel> getEventsByOrganization(Long orgId) {
        return eventRepository.findByEventOrganizerId(orgId);
    }

}