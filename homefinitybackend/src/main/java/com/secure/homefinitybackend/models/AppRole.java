package com.secure.homefinitybackend.models;

public enum AppRole {
    ROLE_OWNER,      // Property owners
    ROLE_TENANT,     // Renters (replaces ROLE_USER)
    ROLE_ADMIN,      // Platform administrators
    ROLE_BROKER      // Property brokers/agents
}