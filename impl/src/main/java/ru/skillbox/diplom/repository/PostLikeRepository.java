package ru.skillbox.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.model.Person;
import ru.skillbox.diplom.model.Post;
import ru.skillbox.diplom.model.PostLike;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {

    Optional<PostLike> findByPostId(Integer postId);
    Optional<PostLike> findByPersonId(Integer personId);

    @Query("select l from PostLike l where l.personId = :personId and l.postId = :postId")
    Optional<PostLike> findByPostAndPerson(Post postId, Person personId);

    @Transactional
    @Modifying
    @Query("delete from PostLike l where l.personId = :personId and l.postId = :postId")
    void deleteByPostAndPerson(Post postId, Person personId);

}
