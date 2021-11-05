package ru.skillbox.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.skillbox.diplom.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer>, JpaSpecificationExecutor<Tag> {

    @Modifying
    @Query("DELETE FROM Tag t WHERE t.id NOT IN (SELECT p2t.tag.id FROM PostToTag p2t)")
    void deleteTagsNotExistsPost();

}
