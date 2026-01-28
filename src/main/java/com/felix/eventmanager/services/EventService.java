package com.felix.eventmanager.services;

import com.felix.eventmanager.enums.EventStatus;
import com.felix.eventmanager.enums.EventType;
import com.felix.eventmanager.exceptions.EventDuplicatedException;
import com.felix.eventmanager.exceptions.EventInvalidHourException;
import com.felix.eventmanager.models.Event;
import com.felix.eventmanager.repositories.EventRepository;
import com.felix.eventmanager.utils.Utils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(final EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event saveEvent(Event event) {
        if (event.getType() == EventType.SCHEDULED_TIME) {
            boolean isBegin;

            if ((isBegin = ! Utils.isValidHour(event.getBeginTime())) || ! Utils.isValidHour(event.getEndTime()))
                throw new EventInvalidHourException("The " + ( isBegin ? "[beginTime]" : "[endTime]") + " is incorrect!");
        }

        if (eventRepository.findByDescription(event.getDescription()) != null)
            throw new EventDuplicatedException("Event [" + event.getDescription() + "] already exists");

        event.setUuid(UUID.randomUUID().toString());

        return eventRepository.save(setStatus(event, false));
    }

    public Event updateEvent(Event event, boolean isTask) {
        Event doppelganger = eventRepository.findByDescription(event.getDescription());

        if (doppelganger != null && ! event.getUuid().equals(doppelganger.getUuid()))
            throw new EventDuplicatedException("Event [" + event.getDescription() + "] already exists");

        if (event.getType() == EventType.SCHEDULED_TIME) {
            boolean isBegin;

            if ((isBegin = ! Utils.isValidHour(event.getBeginTime())) || ! Utils.isValidHour(event.getEndTime()))
                throw new EventInvalidHourException("The " + (isBegin ? "[beginTime]" : "[endTime]") + " is incorrect!");
        }

        Event item = setStatus(event, isTask);

        if (item == null)
            return null;

        return eventRepository.save(item);
    }

    public Event setStatus(Event event, boolean isTask) {
        LocalDateTime today = LocalDateTime.now();
        EventStatus status;

        if ((event.getStatus() == null ? eventRepository.findByUuid(event.getUuid()).getStatus() : event.getStatus()) ==
                EventStatus.DELETED) {
            if (isTask)
                return null;
            else
                return event.toBuilder().status(EventStatus.DELETED).build();
        } else {
            LocalDateTime en = Utils.getDateWithHour(event.getDate(), event.getEndTime());

            if (today.isAfter(en))
                status = EventStatus.FINISHED;
            else if (today.isBefore(en) && today.isAfter(Utils.getDateWithHour(event.getDate(), event.getBeginTime())))
                status = EventStatus.PROCEEDING;
            else
                status = EventStatus.ACTIVATED;

            return event.toBuilder().status(status).build();
        }
    }

    public List<Event> getAllEvents(String status) {
        if (status == null)
            return eventRepository.findEventsNotDeleted();

        return eventRepository.findEventByStatus(EventStatus.get(status));
    }

    public Event getEvent(String uuid) {
        return eventRepository.findByUuid(uuid);
    }

    public void deleteByUuid(String uuid) {
        eventRepository.save(eventRepository.findByUuid(uuid).toBuilder()
                .status(EventStatus.DELETED)
                .build());
    }
}
