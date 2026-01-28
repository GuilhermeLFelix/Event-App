package com.felix.eventmanager.models;

import com.felix.eventmanager.enums.EventStatus;
import com.felix.eventmanager.enums.EventType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String uuid;

    private String description;

    private EventType type;

    private EventStatus status;

    private Date date;

    private String beginTime;

    private String endTime;

    private Date creation;
}
