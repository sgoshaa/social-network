package ru.skillbox.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.diplom.model.PostComment;
import ru.skillbox.diplom.model.PostReport;

public interface PostReportRepository extends JpaRepository<PostReport,Integer> {
}
