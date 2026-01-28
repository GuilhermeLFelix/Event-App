package com.felix.eventmanager.controllers;

import com.felix.eventmanager.dtos.EventDTO;
import com.felix.eventmanager.enums.EventStatus;
import com.felix.eventmanager.enums.EventType;
import com.felix.eventmanager.models.Event;
import com.felix.eventmanager.services.EventService;
import com.felix.eventmanager.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/api/event")
public class EventController {
    private final EventService eventService;

    public EventController(final EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(required = false) String status) {
        try {
            List<EventDTO> data = new ArrayList<>();

            for (Event item : eventService.getAllEvents(status)) {
                data.add(new EventDTO(item.getUuid(), item.getDescription(), item.getType().toString(),
                        item.getStatus().toString(), Utils.dateToStr(item.getDate()), item.getBeginTime(),
                        item.getEndTime()));
            }

            return ResponseEntity.ok(data);
        } catch (Exception exc) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> findByUuid(@PathVariable String uuid) {
        try {
            Event item = eventService.getEvent(uuid);

            return ResponseEntity.ok(new EventDTO(item.getUuid(), item.getDescription(), item.getType().toString(),
                    item.getStatus().toString(), Utils.dateToStr(item.getDate()), item.getBeginTime(),
                    item.getEndTime()));
        } catch (Exception exc) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody EventDTO event) {
        ResponseEntity.BodyBuilder badRequest = ResponseEntity.status(HttpStatus.BAD_REQUEST.value());

        EventType type;

        if (event.description() == null || event.description().isBlank())
            badRequest.body("Invalid Description");
        else if (event.day() == null)
            badRequest.body("Invalid Date");
        else if ((type = EventType.get(event.type())) == null || event.type() == null)
            badRequest.body("Invalid Type");
        else if (type == EventType.SCHEDULED_TIME && (event.beginTime() == null || event.endTime() == null))
            badRequest.body("[beginTime/endTime] required for Type [SCHEDULED_TIME]");
        else
            badRequest = null;

        if (badRequest != null)
            return badRequest.build();

        try {
            Event item = eventService.saveEvent(Event.builder()
                    .uuid(UUID.randomUUID().toString())
                    .description(event.description())
                    .type(EventType.get(event.type()))
                    .date(Utils.strToDate(event.day()))
                    .beginTime(event.beginTime())
                    .endTime(event.endTime())
                    .creation(new Date())
                    .status(EventStatus.ACTIVATED)
                    .build());

            return ResponseEntity.ok(Map.of("uuid", item.getUuid(), "creation", item.getCreation()));
        } catch (ParseException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Date");
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody EventDTO event) {
        try {
            Event e1 = eventService.getEvent(event.uuid());

            if (e1 == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found! Check the [uuid]");

            try {
                Event item = eventService.updateEvent(e1.toBuilder()
                        .description(event.description())
                        .type(EventType.get(event.type()))
                        .date(Utils.strToDate(event.day()))
                        .beginTime(event.beginTime())
                        .endTime(event.endTime())
                        .build(), false);

                return ResponseEntity.ok(new EventDTO(item.getUuid(), item.getDescription(), item.getType().toString(),
                        item.getStatus().toString(), Utils.dateToStr(item.getDate()), item.getBeginTime(),
                        item.getEndTime()));
            } catch (ParseException exc) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Date");
            }

        } catch (Exception exc) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteByUuid(@PathVariable String uuid) {
        try {
            eventService.deleteByUuid(uuid);

            return ResponseEntity.ok().build();
        } catch (Exception exc) {
            return ResponseEntity.internalServerError().build();
        }
    }
}