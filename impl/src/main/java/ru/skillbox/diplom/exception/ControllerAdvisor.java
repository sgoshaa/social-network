package ru.skillbox.diplom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Object> handleRegisterRequestException(EmailNotFoundException ex) {

        LoginRegRequestException registerRequestException = new LoginRegRequestException(ex.getMessage(), "This email already exists");

        return new ResponseEntity<>(registerRequestException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalPasswordEmailException.class)
    public ResponseEntity<Object> handlerLoginRequestException(IllegalPasswordEmailException ex) {

        LoginRegRequestException loginRequestException = new LoginRegRequestException(ex.getMessage(), "Incorrect email or password");

        return new ResponseEntity<>(loginRequestException, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidRequest.class)
    public ResponseEntity<Object> handlerInvalidRequestException(InvalidRequest ex) {

        FriendShipResponseException friendShipResponseException = new FriendShipResponseException(ex.getMessage(), "invalid request");

        return new ResponseEntity<>(friendShipResponseException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserIsNotAuthorized.class)
    public ResponseEntity<Object> handlerUserIsNotAutorized(UserIsNotAuthorized ex) {

        FriendShipResponseException friendShipResponseException = new FriendShipResponseException(ex.getMessage(), "the user is not logged in");

        return new ResponseEntity<>(friendShipResponseException, HttpStatus.UNAUTHORIZED);

    }
}
