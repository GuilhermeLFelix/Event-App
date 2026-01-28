package com.felix.eventmanager.jobs;

import com.felix.eventmanager.models.Event;
import com.felix.eventmanager.services.EventService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class EventJob {
    private final EventService eventService;

    public EventJob(final EventService eventService) {
        this.eventService = eventService;
    }

    @PostConstruct
    public void manageEvents(){
        new Thread(() -> {
            Calendar cal = Calendar.getInstance();

            while (true) {
                try {
                    cal.setTime(new Date());
                    cal.add(Calendar.DATE, 1);

                    List<Event> events = eventService.getAllEvents(null).stream()
                            .filter(e -> e.getDate().before(cal.getTime()))
                            .toList();

                    if (! events.isEmpty()) {
                        events.forEach(e -> eventService.updateEvent(e, true));

                        System.out.println("[TASKS " + events.size() + "] | [DATE " + new Date() + "]");
                    }

                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}