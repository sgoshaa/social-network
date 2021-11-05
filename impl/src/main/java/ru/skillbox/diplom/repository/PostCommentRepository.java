package ru.skillbox.diplom.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.model.PostComment;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment,Integer>, JpaSpecificationExecutor<PostComment> {

    Page<PostComment> findAllByPostId(Integer id, Pageable pageable);
}
