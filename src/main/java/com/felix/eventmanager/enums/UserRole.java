package com.felix.eventmanager.enums;

public enum UserRole {
    ADMIN, USER;

    private final String name;

    UserRole() {
        name = name().toLowerCase();
    }

    public static UserRole get(String name) {
        for (UserRole type : values())
            if (type.name.equalsIgnoreCase(name) )
                return type;

        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
