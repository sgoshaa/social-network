package ru.skillbox.diplom.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.model.Message;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query(
            "SELECT m "
                    + "FROM Message m "
                    + "WHERE m.readStatus = 'SENT' AND m.recipientId.id = :id "
                    + "GROUP BY m.id")
    List<Message> findUnreadedMessages(Integer id);

    @Query(
            "SELECT m "
                    + "FROM Message m "
                    + "WHERE m.readStatus = 'SENT' AND m.recipientId.id = :id AND m.dialogId = :dialogId "
                    + "GROUP BY m.id")
    List<Message> findAllUnreadMessages(Integer id, Integer dialogId);

    @Query(
            "SELECT m " +
                    "FROM Message m " +
                    "WHERE m.authorId.id = :id OR m.recipientId.id = :id " +
                    "ORDER BY m.time DESC")
    Page<Message> findMessages(Integer id, Pageable pageable);

    @Query(
            "SELECT m " +
                    "FROM Message m " +
                    "WHERE m.dialogId = :id " +
                    "ORDER BY m.time DESC")
    Page<Message> findAllMessages(Integer id, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Message m WHERE m.dialogId = :id")
    @Transactional
    void deleteByDialogId(Integer id);
}
