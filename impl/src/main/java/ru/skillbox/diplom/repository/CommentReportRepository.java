package ru.skillbox.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.diplom.model.CommentReport;


public interface CommentReportRepository extends JpaRepository<CommentReport, Integer> {
}
