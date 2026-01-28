package com.felix.eventmanager.exceptions;

public class EventDuplicatedException extends RuntimeException {
    public EventDuplicatedException(String message) { super(message); }
}
