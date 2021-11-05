package ru.skillbox.diplom.resources;

import org.springframework.http.ResponseEntity;
import ru.skillbox.diplom.model.api.request.LoginRequest;
import ru.skillbox.diplom.model.api.response.LoginResponse;
import ru.skillbox.diplom.model.api.response.LogoutResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface AuthController {
    ResponseEntity<LoginResponse> login(LoginRequest loginRequest);

    LogoutResponse logout(HttpServletRequest request, HttpServletResponse response);
}
