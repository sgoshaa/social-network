package ru.skillbox.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.model.Dialog;

@Repository
public interface DialogRepository extends JpaRepository<Dialog, Integer> {
}
