package com.felix.eventmanager.dtos;

import com.felix.eventmanager.enums.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {}
