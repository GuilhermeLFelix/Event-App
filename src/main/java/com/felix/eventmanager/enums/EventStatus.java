package com.felix.eventmanager.enums;

import lombok.Getter;

@Getter
public enum EventStatus {
    ACTIVATED,
    PROCEEDING,
    DELETED,
    FINISHED;

    private final String name;

    EventStatus() {
        name = name().toLowerCase();
    }

    public static EventStatus get(String name) {
        for (EventStatus type : values())
            if (type.name.equalsIgnoreCase(name) )
                return type;

        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}