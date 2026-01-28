package com.felix.eventmanager.repositories;

import com.felix.eventmanager.enums.EventStatus;
import com.felix.eventmanager.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByUuid(String uuid);
    Event findByDescription(String description);

    @Query(value = "SELECT * FROM public.event WHERE status != " + 2, nativeQuery = true)
    List<Event> findEventsNotDeleted();
    List<Event> findEventByStatus(EventStatus status);
}