package ru.skillbox.diplom.exception;

public class IllegalPasswordEmailException extends RuntimeException {

    public IllegalPasswordEmailException(String message) {
        super(message);
    }
}
