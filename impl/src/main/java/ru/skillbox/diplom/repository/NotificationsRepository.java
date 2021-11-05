package ru.skillbox.diplom.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skillbox.diplom.model.FriendShip;
import ru.skillbox.diplom.model.Notification;
import ru.skillbox.diplom.model.Person;
import ru.skillbox.diplom.model.enums.Type;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationsRepository extends JpaRepository<Notification,Integer> {

    Page<Notification> findByPersonIdAndDelivered(Integer id, Pageable pageable,Boolean delivered);

    List<Notification> findByPersonIdAndDelivered(Integer id,Boolean delivered);

    List<Notification> findByTypeAndPersonId(Type type,Integer id);

    @Query("select p from Person p where p.id in " +
            "(select f.dstPersonId from FriendShip f " +
            "INNER JOIN FriendShipStatus fs on f.status = fs.id WHERE f.srcPersonId = :currentId AND fs.code = 'FRIEND')" +
            " AND " +
            "extract(day from p.birthDate) = extract(day from CURRENT_TIMESTAMP)" +
            " AND " +
            "extract(MONTH from p.birthDate) = extract(MONTH from CURRENT_TIMESTAMP) " +
            "AND p.id not in (select entity from Notification n " +
            "where " +
            "extract(MONTH from n.sentTime ) = extract(MONTH from current_timestamp)" +
            "AND extract(day from n.sentTime ) = extract(day from current_timestamp)" +
            "AND extract(year from n.sentTime ) = extract(year from current_timestamp)) ")
    List<Person> getListFriendsBirthdayToday(Integer currentId);
}
