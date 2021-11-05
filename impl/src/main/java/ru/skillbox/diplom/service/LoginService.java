package ru.skillbox.diplom.service;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.config.security.JwtTokenProvider;
import ru.skillbox.diplom.mappers.PersonMapper;
import ru.skillbox.diplom.model.Person;
import ru.skillbox.diplom.model.PersonDTO;
import ru.skillbox.diplom.model.api.request.LoginRequest;
import ru.skillbox.diplom.model.api.response.LoginResponse;
import ru.skillbox.diplom.repository.PersonRepository;
import ru.skillbox.diplom.service.enums.LoggerLevel;
import ru.skillbox.diplom.service.enums.LoggerValue;

import java.util.Optional;

@Service
public class LoginService implements SocialNetworkService{

    private final Class<EmailService> loggerClass = EmailService.class;
    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);
    private final PersonRepository personRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final NotificationsService notificationsService;

    public LoginService(PersonRepository personRepository,
                        JwtTokenProvider jwtTokenProvider, NotificationsService notificationsService) {
        this.personRepository = personRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.notificationsService = notificationsService;
    }

    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        log(loggerClass,LoggerLevel.INFO,"login", LoggerValue.LOGIN_REQUESTED, loginRequest.getEmail());
        LoginResponse loginResponse = new LoginResponse();
        String requestMail = loginRequest.getEmail();
        Optional<Person> optional = personRepository.findByEmail(requestMail);
        if (optional.isPresent()) {
            Person user = optional.get();
            if (BCrypt.checkpw(loginRequest.getPassword(), user.getPassword()) & user.getIsApproved()) {
                String token = jwtTokenProvider.createToken(requestMail, user.getRole().name());
                PersonDTO personDTO = personMapper.toPersonDTO(personRepository.getOne(user.getId()), token);
                loginResponse.applied(personDTO);
                //создаем др
                notificationsService.createListFriendsBirthdayToday(user.getId());
                //
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                try {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log(loggerClass, LoggerLevel.INFO,"login", LoggerValue.LOGIN_ACCEPTED, requestMail);
                } catch (Exception e) {
                    log(loggerClass, LoggerLevel.ERROR,"login", LoggerValue.LOGIN_FAILED, requestMail);
                }
            }
        } else {
            loginResponse.denied();
            log(loggerClass, LoggerLevel.WARN,"login", LoggerValue.ACCOUNT_404, requestMail);
        }
        return loginResponse.getData() != null ?
                ResponseEntity.ok(loginResponse) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
    }
}