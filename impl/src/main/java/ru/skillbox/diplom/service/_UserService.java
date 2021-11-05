package ru.skillbox.diplom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.model.NotificationSettingDTO;
import ru.skillbox.diplom.model.NotificationType;
import ru.skillbox.diplom.model.User;
import ru.skillbox.diplom.model.UserSetting;
import ru.skillbox.diplom.model.api.request.NotificationRequest;
import ru.skillbox.diplom.model.api.response.NotificationSettingsResponse;
import ru.skillbox.diplom.model.api.response.RegistrationResponse;
import ru.skillbox.diplom.model.enums.Type;
import ru.skillbox.diplom.repository.NotificationTypeRepository;
import ru.skillbox.diplom.repository.UserRepository;
import ru.skillbox.diplom.service.enums.LoggerLevel;
import ru.skillbox.diplom.service.enums.LoggerValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class _UserService implements SocialNetworkService {
    private final Class<_UserService> loggerClass = _UserService.class;
    private final _UserSettingsService userSettingsService;
    private final UserRepository userRepository;
    private final NotificationTypeRepository notificationTypeRepository;

    @Autowired
    public _UserService(_UserSettingsService userSettingsService, UserRepository userRepository, NotificationTypeRepository notificationTypeRepository) {
        this.userSettingsService = userSettingsService;
        this.userRepository = userRepository;
        this.notificationTypeRepository = notificationTypeRepository;
    }

    public RegistrationResponse changeSetting(User user, NotificationRequest request) {
        List<UserSetting> settings = userSettingsService.getAllByUserId(user.getId());
        for (UserSetting s : settings) {
            if (s.getType().getCode().equals(request.getType())) {
                s.setValue(request.isEnable());
                userSettingsService.save(s);
                log(loggerClass, LoggerLevel.INFO, "changeSetting", LoggerValue.SETTING_CHANGED, request.toString());
                return new RegistrationResponse().applied();
            }
        }
        return new RegistrationResponse().denied();
    }

    public NotificationSettingsResponse getSettings(User user) {
        List<UserSetting> settings = userSettingsService.getAllByUserId(user.getId());
        if (settings.isEmpty()) {
            settings = fulfillSettingsForNewUser(user);
        }
        List<NotificationSettingDTO> result = settings.stream()
                .map(NotificationSettingDTO::fromSetting).collect(Collectors.toList());
        log(loggerClass, LoggerLevel.INFO, "getSettings", LoggerValue.SETTINGS_GIVEN, user.getEmail());
        return new NotificationSettingsResponse().applied(result);
    }

    private List<UserSetting> fulfillSettingsForNewUser(User user) {
        log(loggerClass, LoggerLevel.INFO, "fulfillSettingsForNewUser", LoggerValue.SETTINGS_NEW, user.getEmail());
        List<UserSetting> result = new ArrayList<>();
        for (int i = 0; i < Type.values().length; i++) {
            NotificationType notificationType = notificationTypeRepository.getById(i + 1);
            UserSetting setting = new UserSetting(user, notificationType, false);
            userSettingsService.save(setting);
            result.add(setting);
        }
        return result;
    }

    public User getUser(int id) {
        return userRepository.getOne(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
