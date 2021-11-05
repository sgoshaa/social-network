package ru.skillbox.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.model.User2Dialog;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface User2DialogRepository extends JpaRepository<User2Dialog, Integer> {

    @Query("SELECT u2d " +
            "FROM User2Dialog u2d " +
            "WHERE u2d.userId = :id")
    List<User2Dialog> findByUserId(Integer id);

    @Query("SELECT u2d " +
            "FROM User2Dialog u2d " +
            "WHERE u2d.dialogId = :id")
    List<User2Dialog> findByDialogId(Integer id);

    @Modifying
    @Query("DELETE FROM User2Dialog u2d WHERE u2d.dialogId = :id")
    @Transactional
    void deleteByDialogId(Integer id);

    @Modifying
    @Query("DELETE FROM User2Dialog u2d WHERE u2d.dialogId in (SELECT dialogId FROM User2Dialog WHERE userId = :id)")
    @Transactional
    void deleteByDialogUserId(Integer id);
}
