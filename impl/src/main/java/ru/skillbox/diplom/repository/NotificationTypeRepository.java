package ru.skillbox.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.model.NotificationType;
import ru.skillbox.diplom.model.enums.Type;

@Repository
public interface NotificationTypeRepository extends JpaRepository<NotificationType, Integer> {

    NotificationType getById(int id);
    NotificationType getNotificationTypeByCode(Type type);
}
