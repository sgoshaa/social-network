package ru.skillbox.diplom.model.api.enums;

public enum Errors {
    UNAUTHORISED(""),
    INVALID_REQUEST("incredible error"),
    string(""),
    NO_ERRORS("no errors"),
    errors("");
    public String description;

    Errors(String description) {
        this.description = description;
    }
}
