package ru.skillbox.diplom.model.enums;

public enum Permission {
    USER("user:write"),
    MODERATE("user:moderate"),
    ADMINISTRATION("user:administrate");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}