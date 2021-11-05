package ru.skillbox.diplom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.model.UserSetting;
import ru.skillbox.diplom.repository.UserSettingsRepository;

import java.util.List;

@Service
public class _UserSettingsService {

    private final UserSettingsRepository userSettingsRepository;

    @Autowired
    public _UserSettingsService(UserSettingsRepository userSettingsRepository) {
        this.userSettingsRepository = userSettingsRepository;
    }

    public UserSetting save(UserSetting settings){
        return userSettingsRepository.save(settings);
    }

    public List<UserSetting> getAllByUserId(Integer id) {
        return userSettingsRepository.getAllByUserId(id);
    }
}
