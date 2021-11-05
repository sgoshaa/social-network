package ru.skillbox.diplom.exception;

public class UserIsNotAuthorized extends RuntimeException {

    public UserIsNotAuthorized(String message) {
        super(message);
    }
}
