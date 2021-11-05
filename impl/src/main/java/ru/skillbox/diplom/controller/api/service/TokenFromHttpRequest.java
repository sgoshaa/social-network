package ru.skillbox.diplom.controller.api.service;

import ru.skillbox.diplom.model.api.request.SetPasswordRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TokenFromHttpRequest {
    private static final String token = "TOKEN";

    public static SetPasswordRequest giveRequest(SetPasswordRequest request, HttpServletRequest httpRequest) {
        List<Cookie> cookies = Arrays.stream(httpRequest.getCookies()).collect(Collectors.toList());
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(token)) {
                request.setToken(cookie.getValue());
            }
        }
        return request;
    }
}
