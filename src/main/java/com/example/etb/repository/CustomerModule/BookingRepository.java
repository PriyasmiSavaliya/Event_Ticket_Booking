package com.example.etb.repository.CustomerModule;

import com.example.etb.model.CustomerModule.BookingModel;
import com.example.etb.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<BookingModel, Long> {
    // Method to fetch bookings for a specific user by their user ID
    List<BookingModel> findByUser(UserModel user);

    @Query("SELECT b FROM BookingModel b " +
            "JOIN b.event e " +
            "WHERE b.event.id = :eventId " +
            "AND e.eventOrganizer.id = :orgId " +
            "AND b.status = :status " +
            "ORDER BY b.id DESC")
    List<BookingModel> findBookingsForEventAndOrganizationWithStatusAndOrdering(@Param("eventId") Long eventId,
                                                                                @Param("orgId") Long orgId,
                                                                                @Param("status") String status);


}
