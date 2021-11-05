package ru.skillbox.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.model.User;
import ru.skillbox.diplom.model.UserSetting;

import java.util.List;

@Repository
public interface UserSettingsRepository extends JpaRepository<UserSetting, Integer> {

    List<UserSetting> getAllByUserId(int userId);

    @Modifying
    @Query("DELETE FROM UserSetting us WHERE us.user = :user")
    void userDelete(User user);
}
