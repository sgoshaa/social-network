package ru.skillbox.diplom.service;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.config.security.JwtTokenProvider;
import ru.skillbox.diplom.exception.EmailNotFoundException;
import ru.skillbox.diplom.exception.InvalidRequest;
import ru.skillbox.diplom.mappers.DefaultResponseMapper;
import ru.skillbox.diplom.mappers.PersonMapper;
import ru.skillbox.diplom.mappers.converter.DateConverter;
import ru.skillbox.diplom.model.FriendShip;
import ru.skillbox.diplom.model.Person;
import ru.skillbox.diplom.model.PersonDTO;
import ru.skillbox.diplom.model.User;
import ru.skillbox.diplom.model.api.enums.Errors;
import ru.skillbox.diplom.model.api.request.EmailRequest;
import ru.skillbox.diplom.model.api.request.NotificationRequest;
import ru.skillbox.diplom.model.api.request.ProfileRequest;
import ru.skillbox.diplom.model.api.request.RegisterRequest;
import ru.skillbox.diplom.model.api.request.SearchRequest;
import ru.skillbox.diplom.model.api.request.SetPasswordRequest;
import ru.skillbox.diplom.model.api.response.LoginResponse;
import ru.skillbox.diplom.model.api.response.RegistrationResponse;
import ru.skillbox.diplom.model.api.response.Response;
import ru.skillbox.diplom.repository.FriendShipRepository;
import ru.skillbox.diplom.repository.PersonBlockRepository;
import ru.skillbox.diplom.repository.PersonRepository;
import ru.skillbox.diplom.repository.User2DialogRepository;
import ru.skillbox.diplom.repository.UserRepository;
import ru.skillbox.diplom.repository.UserSettingsRepository;
import ru.skillbox.diplom.repository.specification.ToSpecification;
import ru.skillbox.diplom.repository.specification.enums.FieldName;
import ru.skillbox.diplom.service.enums.AccountServiceEnums;
import ru.skillbox.diplom.service.enums.LoggerLevel;
import ru.skillbox.diplom.service.enums.LoggerValue;
import ru.skillbox.diplom.util.Paging;
import ru.skillbox.diplom.util.ToEmpty;
import ru.skillbox.diplom.util.UserUtility;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService implements SocialNetworkService {
    private final Class<AccountService> loggerClass = AccountService.class;
    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);
    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ToSpecification<Person> toSpecPerson;
    private final DateConverter dateConverter;
    private final UserSettingsRepository userSettingsRepository;
    private final User2DialogRepository user2DialogRepository;
    private final FriendShipRepository friendShipRepository;
    private final ToSpecification<FriendShip> friendShipToSpecification;
    private final PersonBlockRepository personBlockRepository;

    @Autowired
    public AccountService(UserRepository userRepository,
                          PersonRepository personRepository,
                          EmailService emailService,
                          JwtTokenProvider jwtTokenProvider,
                          ToSpecification<Person> toSpecPerson,
                          DateConverter dateConverter,
                          UserSettingsRepository userSettingsRepository,
                          User2DialogRepository user2DialogRepository, FriendShipRepository friendShipRepository,
                          ToSpecification<FriendShip> friendShipToSpecification, PersonBlockRepository personBlockRepository) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.emailService = emailService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.toSpecPerson = toSpecPerson;
        this.dateConverter = dateConverter;
        this.userSettingsRepository = userSettingsRepository;
        this.user2DialogRepository = user2DialogRepository;
        this.friendShipRepository = friendShipRepository;
        this.friendShipToSpecification = friendShipToSpecification;
        this.personBlockRepository = personBlockRepository;
    }


    public void savePerson(Person person, String method) {
        personRepository.save(person);
        log(loggerClass, LoggerLevel.INFO, method, LoggerValue.PERSON_SAVED, person.getEmail());
    }

    public void deletePerson(Person person, String method) {
        personRepository.deletePerson(person);
        log(loggerClass, LoggerLevel.INFO, method, LoggerValue.PERSON_SAVED, person.getEmail());
    }

    public RegistrationResponse register(RegisterRequest registerRequest) {
        log(loggerClass, LoggerLevel.INFO, "register", LoggerValue.ACCOUNT_REQUEST, registerRequest.getEmail());
        RegistrationResponse response = new RegistrationResponse();

        String encodedPassword = cryptPassword(registerRequest.getPasswd1());
        if (!emailPresents(registerRequest.getEmail())) {
            LocalDateTime localDateTime = LocalDateTime.now();
            String confirmationCode = makeConfirmationCode();
            String link = AccountServiceEnums.LINK_REGISTRATION.link + confirmationCode;
            String subject = AccountServiceEnums.LINK_REGISTRATION.description;
            emailService.registerEmail(registerRequest.getEmail(), subject, link);
            Person person = personMapper.toPersonEntity(registerRequest, encodedPassword, localDateTime, confirmationCode);
            savePerson(person, "register");
            response.applied();
        } else {
            log(loggerClass, LoggerLevel.WARN, "register", LoggerValue.EMAIL_EXISTS, registerRequest.getEmail());
            response.denied();
            throw new EmailNotFoundException(Errors.INVALID_REQUEST.name());
        }
        return response;
    }

    public boolean confirmPerson(String code) {
        log(loggerClass, LoggerLevel.INFO, "confirmPerson", LoggerValue.PERSON_CONFIRMATION, code);
        String notThisUser = AccountServiceEnums.EMAIL_WRONG_USER.link;
        if (code.endsWith(notThisUser)) {
            personRepository.findByConfirmationCode(code.substring(0, code.length() - notThisUser.length()))
                    .ifPresent(value -> deletePerson(value, "confirmPerson"));
            return true;
        }
        Optional<Person> optional = personRepository.findByConfirmationCode(code);
        if (optional.isPresent()) {
            savePerson(optional.get().approve(), "confirmPerson");
            return true;
        } else {
            log(loggerClass, LoggerLevel.WARN, "confirmPerson", LoggerValue.ACCOUNT_404, code);

        }
        return true;
    }

    public RegistrationResponse recovery(EmailRequest request, HttpServletResponse httpResponse) {
        log(loggerClass, LoggerLevel.INFO, "recovery", LoggerValue.PERSON_RECOVERY, request.getEmail());
        RegistrationResponse response = new RegistrationResponse();
        String email = request.getEmail();
        Person person = personRepository.findByEmail(email).orElse(null);
        if (person != null && person.getIsApproved() && !person.getIsBlocked()) {
            String token = makeToken(person.getEmail(), person.getRole().name());
            savePerson(person.takeConfirmationCode(token), "recovery");

            String text = AccountServiceEnums.TOKEN_RECOVERY.link + token;
            String subject = AccountServiceEnums.TOKEN_RECOVERY.description;
            emailService.sendEmail(email, subject, text);
            httpResponse.addCookie(new Cookie("TOKEN", token));
            return response.applied();
        } else {
            log(loggerClass, LoggerLevel.INFO, "recovery", LoggerValue.PERSON_RECOVERY_DENIED, request.getEmail());
        }
        return response.denied();
    }

    public RegistrationResponse changePassword(SetPasswordRequest request) {
        log(loggerClass, LoggerLevel.INFO, "changePassword", LoggerValue.PERSON_CHANGE_REQUEST, request.getToken());
        RegistrationResponse response = new RegistrationResponse();
        String token = request.getToken();
        Person person = personRepository.findByConfirmationCode(token).orElse(null);
        if (person != null & jwtTokenProvider.validateAndLog(token)) {
            savePerson(person.changePass(person, cryptPassword(request.getPassword())), "changePassword");
            return response.applied();
        } else {
            log(loggerClass, LoggerLevel.INFO, "changePassword", LoggerValue.PERSON_CHANGE_DENIED, request.getToken());
        }
        return response.denied();
    }

    public boolean changeEmail(EmailRequest request) {
        log(loggerClass, LoggerLevel.INFO, "changeEmail", LoggerValue.PERSON_CHANGE_REQUEST, request.getEmail());
        String email = request.getEmail();
        Person person = personRepository.findByEmail(email).orElse(null);
        if (person != null && person.getIsApproved() && !person.getIsBlocked()) {
            person.setEmail(email);
            savePerson(person, "changeEmail");
            return true;
        } else {
            log(loggerClass, LoggerLevel.INFO, "changeEmail", LoggerValue.PERSON_CHANGE_DENIED, request.getEmail());
        }
        return false;
    }

    public RegistrationResponse setNotification(NotificationRequest request) {
        return new RegistrationResponse().denied();
    }

    public User getUser(int id) {
        return userRepository.getOne(id);
    }

    public PersonDTO getPersonDTO(Person user) {
        return personMapper.toPersonDTO(user);
    }

    public Person getPerson(Integer id) {
        return personRepository.getOne(id);
    }

    public Person getByEmail(String email) {
        return personRepository.findByEmail(email).orElse(null);
    }

    public Person getByCode(String code) {
        return personRepository.findByConfirmationCode(code).orElse(null);
    }

    public Person findByCurrentEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
            return getByEmail(userPrincipal.getUsername());
        }
        return null;
    }

    private boolean emailPresents(String email) {
        Optional<User> repoUser = userRepository.findByEmail(email);
        return repoUser.isPresent();
    }

    private String makeConfirmationCode() {
        SecureRandom secureRandom = new SecureRandom();
        Base64.Encoder base64Encoder = Base64.getUrlEncoder();
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    private String makeToken(String email, String role) {
        return jwtTokenProvider.createToken(email, role);
    }

    private String cryptPassword(String pass) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y, 12);
        return bCryptPasswordEncoder.encode(pass);
    }

    @Cacheable("searchPerson")
    public ResponseEntity<Response<PersonDTO>> searchPerson(SearchRequest searchRequest) {
        log(loggerClass, LoggerLevel.INFO, "searchPerson", LoggerValue.SEARCHING_REQUEST, searchRequest.toString());
        Person currentPerson = findByCurrentEmail();
        if (Objects.isNull(currentPerson)) {
            throw new InvalidRequest("unauthorised");
        }
        DefaultResponseMapper<PersonDTO> responseMapper = new DefaultResponseMapper<>();
        if (searchRequest.requestIsNull(Person.class)) {
            return ResponseEntity.ok(responseMapper.convert(new ArrayList<>(), 0, searchRequest.getItemPerPage()));
        }
        LocalDateTime dateFrom = dateConverter.ageToBirthDate(searchRequest.getAge_from());
        LocalDateTime dateTo = dateConverter.ageToBirthDate(searchRequest.getAge_to());
        Specification<Person> specification =
                toSpecPerson.getPredicateNameField(FieldName.FIRST_NAME.getValue(), FieldName.LAST_NAME.getValue(), searchRequest.getFirst_name())
                        .and(toSpecPerson.contains(FieldName.LAST_NAME.getValue(), searchRequest.getLast_name()))
                        .and(Objects.isNull(dateTo) && Objects.isNull(dateFrom) ?
                                toSpecPerson.disjunctionOrConjunction(Predicate.BooleanOperator.AND) :
                                toSpecPerson.between(FieldName.BIRTHDATE.getValue(), dateTo, dateFrom))
                        .and(toSpecPerson.contains(FieldName.TOWN.getValue(), Predicate.BooleanOperator.AND,
                                searchRequest.getCountry(), searchRequest.getCity()))
                        .and(toSpecPerson.noEquals(FieldName.ID.getValue(), currentPerson.getId()));
        Sort sort = Sort.by(FieldName.LAST_NAME.getValue()).and(Sort.by(FieldName.FIRST_NAME.getValue())).descending();
        Pageable pageable = Paging.getPaging(searchRequest.getOffset(), searchRequest.getItemPerPage(), sort);
        Page<Person> personPage = personRepository.findAll(specification, pageable);
        List<PersonDTO> personDTOList = personPage.stream()
                .map(personMapper::toPersonDTO)
                .collect(Collectors.toList());
        Response<PersonDTO> response = responseMapper.convert(personDTOList, personPage.getTotalElements(), searchRequest.getItemPerPage());
        log(loggerClass, LoggerLevel.INFO, "searchPerson", LoggerValue.SEARCHING_RESPONSE, response.toString());
        return ResponseEntity.ok(response);
    }

    @CacheEvict(value = {"profile", "searchPerson"})
    public boolean editProfile(ProfileRequest request) {
        log(loggerClass, LoggerLevel.INFO, "editProfile", LoggerValue.PERSON_CHANGE_REQUEST, request.toString());
        User user = UserUtility.getUser();
        if (user != null) {
            Person person = personRepository.getOne(user.getId());

            person.setFirstName(request.getFirstName());
            person.setLastName(request.getLastName());
            person.setBirthDate(LocalDateTime.parse(request.getBirthDate().substring(0, 19)));
            person.setPhone(request.getPhone() == null ? person.getPhone() : request.getPhone());
            person.setPhoto(request.getPhotoId());
            person.setAbout(request.getAbout() == null ? "" : request.getAbout());
            person.setTown(request.getCountry() + "," + request.getCity());
            personRepository.save(person);
            log(loggerClass, LoggerLevel.INFO, "editProfile", LoggerValue.PERSON_CHANGE_RESPONSE, request.toString());
            return true;
        }
        log(loggerClass, LoggerLevel.INFO, "editProfile", LoggerValue.PERSON_CHANGE_DENIED, request.toString());
        return false;
    }

    @CacheEvict(value = {"profile", "searchPerson"})
    @Transactional
    public void deleteProfile() {
        Person currentPerson = findByCurrentEmail();
        userSettingsRepository.userDelete(userRepository.getOne(currentPerson.getId()));
        user2DialogRepository.deleteByDialogUserId(currentPerson.getId());
        personBlockRepository.deletePersonBlockBy(currentPerson);
        Specification<FriendShip> specification = friendShipToSpecification.equals(FieldName.SRC_PERSON.getValue(), currentPerson.getId())
                .or(friendShipToSpecification.equals(FieldName.DST_PERSON.getValue(), currentPerson.getId()));
        List<FriendShip> friendShipList = friendShipRepository.findAll(specification);
        friendShipRepository.deleteAll(friendShipList);
        currentPerson.getPosts().forEach(post -> post.setDeleted(true));
        currentPerson.getPostsComment().forEach(postComment -> postComment.setDeleted(true));
        ToEmpty.toPerson(currentPerson);
        personRepository.save(currentPerson);
        log(loggerClass, LoggerLevel.INFO, "deleteProfile", LoggerValue.PERSON_DELETED, currentPerson.toString());
    }

    public LoginResponse getMe() {
        Person person = findByCurrentEmail();
        if (person == null) {
            return new LoginResponse().denied();
        }
        PersonDTO dto = personMapper.toPersonDTO(person);
        getTown(person, dto);
        return new LoginResponse().applied(dto);
    }

    @Cacheable("profile")
    public LoginResponse getPersonById(int id) {
        User user = UserUtility.getUser();
        if (user == null) {
            return new LoginResponse().denied();
        }
        Person person = getPerson(id);
        PersonDTO dto = personMapper.toPersonDTO(person);
        getTown(person, dto);
        return new LoginResponse().applied(dto);
    }

    private void getTown(Person person, PersonDTO dto) {
        if (person.getTown() == null) {
            dto.setCountry("");
            dto.setCity("");
        } else {
            String[] town = person.getTown().split(",");
            dto.setCountry(town[0]);
            dto.setCity(town[1]);
        }
    }
}