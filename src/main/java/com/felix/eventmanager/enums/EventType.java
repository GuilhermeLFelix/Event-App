package com.felix.eventmanager.enums;

public enum EventType {
    ALL_DAY,
    SCHEDULED_TIME;

    private final String name;

    EventType() {
        name = name().toLowerCase();
    }

    public static EventType get(String name) {
        for (EventType type : values())
            if (type.name.equalsIgnoreCase(name) )
                return type;

        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
