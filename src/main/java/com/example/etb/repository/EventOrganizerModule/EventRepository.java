package com.example.etb.repository.EventOrganizerModule;

import com.example.etb.model.EventOrganizerModule.EventModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventModel, Long> {
    List<EventModel> findByEventOrganizerId(Long eventOrganizerId);

    // Find events by organizer ID
//    List<EventModel> findByOrganizationId(Long orgId);

}