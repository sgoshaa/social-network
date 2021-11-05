package ru.skillbox.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.skillbox.diplom.model.PostToTag;
import ru.skillbox.diplom.model.Tag;

import java.util.List;

public interface PostToTagRepository extends JpaRepository<PostToTag, Integer> {

    @Modifying
    @Query(value = "DELETE FROM PostToTag WHERE postId = :idPost  AND tag IN :tags")
    void deleteTags(@Param("idPost") Integer idPost, @Param("tags") List<Tag> tags );


}
