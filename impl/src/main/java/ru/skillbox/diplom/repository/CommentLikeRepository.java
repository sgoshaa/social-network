package ru.skillbox.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.model.CommentLike;
import ru.skillbox.diplom.model.Person;
import ru.skillbox.diplom.model.PostComment;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer> {

    Optional<CommentLike> findByPostCommentId(Integer postCommentId);

    @Query("select l from CommentLike l where l.personId = :personId and l.postCommentId = :postCommentId")
    Optional<CommentLike> findByPostCommentAndPerson(PostComment postCommentId, Person personId);

    @Transactional
    @Modifying
    @Query("delete from CommentLike l where l.personId = :personId and l.postCommentId = :postCommentId")
    void deleteByPostCommentAndPerson(PostComment postCommentId, Person personId);

}
