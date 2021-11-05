package ru.skillbox.diplom.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.controller.api.service.TokenFromHttpRequest;
import ru.skillbox.diplom.model.api.request.EmailRequest;
import ru.skillbox.diplom.model.api.request.NotificationRequest;
import ru.skillbox.diplom.model.api.request.RegisterRequest;
import ru.skillbox.diplom.model.api.request.SetPasswordRequest;
import ru.skillbox.diplom.model.api.response.NotificationSettingsResponse;
import ru.skillbox.diplom.model.api.response.RegistrationResponse;
import ru.skillbox.diplom.resources.AccountController;
import ru.skillbox.diplom.service.AccountService;
import ru.skillbox.diplom.service._UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/account")
public class ApiAccountController implements AccountController {

    private final AccountService accountService;
    private final _UserService userService;
    @Value("${social_network.main_link}")
    private String mainLink;

    @Autowired
    public ApiAccountController(AccountService accountService, _UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @Override
    @PostMapping("/register")
    public RegistrationResponse register(@RequestBody RegisterRequest registerRequest, HttpServletResponse httpResponse) {
        return accountService.register(registerRequest);
    }

    @Override
    @GetMapping("/confirm/{code}")
    public void confirmPerson(@PathVariable String code,
                              HttpServletResponse response) {
        if (accountService.confirmPerson(code)) {
            try {
                response.sendRedirect(mainLink + ":8080/");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @PutMapping("/password/recovery")
    public RegistrationResponse recovery(@RequestBody EmailRequest request, HttpServletResponse httpResponse) {
        return accountService.recovery(request, httpResponse);
    }

    @Override
    @PutMapping("/password/set")
    public RegistrationResponse changePassword(@RequestBody SetPasswordRequest request, HttpServletRequest httpRequest) {
        return TokenFromHttpRequest.giveRequest(request, httpRequest).getToken() != null ?
                accountService.changePassword(request) : new RegistrationResponse().denied();
    }

    @PutMapping("/email")
    public ResponseEntity<RegistrationResponse> changeEmail(@RequestBody EmailRequest request) {
        if (!accountService.changeEmail(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RegistrationResponse().denied());
        }
        return ResponseEntity.ok(new RegistrationResponse().applied());
    }

    @GetMapping("/notifications")
    public NotificationSettingsResponse getSettings() {
        return userService.getSettings(accountService.getUser(accountService.findByCurrentEmail().getId()));
    }

    @PutMapping("/notifications")
    public RegistrationResponse changeSettings(@RequestBody NotificationRequest request) {
        return userService.changeSetting(accountService.getUser(accountService.findByCurrentEmail().getId()), request);
    }
}
