package ru.skillbox.diplom.resources;

import ru.skillbox.diplom.model.api.request.EmailRequest;
import ru.skillbox.diplom.model.api.request.RegisterRequest;
import ru.skillbox.diplom.model.api.request.SetPasswordRequest;
import ru.skillbox.diplom.model.api.response.RegistrationResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AccountController {

    RegistrationResponse register(RegisterRequest registerRequest, HttpServletResponse httpResponse);

    void confirmPerson(String code, HttpServletResponse response);

    RegistrationResponse recovery(EmailRequest request, HttpServletResponse httpResponse);

    RegistrationResponse changePassword(SetPasswordRequest request, HttpServletRequest httpRequest);}
