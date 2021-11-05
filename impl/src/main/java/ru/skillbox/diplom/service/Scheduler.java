package ru.skillbox.diplom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.model.Post;
import ru.skillbox.diplom.model.PostComment;
import ru.skillbox.diplom.model.PostToTag;
import ru.skillbox.diplom.model.User;
import ru.skillbox.diplom.repository.PostCommentRepository;
import ru.skillbox.diplom.repository.PostRepository;
import ru.skillbox.diplom.repository.PostToTagRepository;
import ru.skillbox.diplom.repository.TagRepository;
import ru.skillbox.diplom.repository.UserRepository;
import ru.skillbox.diplom.repository.specification.ToSpecification;
import ru.skillbox.diplom.repository.specification.enums.FieldName;
import ru.skillbox.diplom.service.enums.LoggerLevel;
import ru.skillbox.diplom.service.enums.LoggerValue;

import java.util.List;

@Component
public class Scheduler implements SocialNetworkService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ToSpecification<User> userToSpecification;
    private final PostCommentRepository postCommentRepository;
    private final ToSpecification<Post> postToSpecification;
    private final ToSpecification<PostComment> postCommentToSpecification;
    private final TagRepository tagRepository;
    private final PostToTagRepository postToTagRepository;
    private final Class<Scheduler> loggerClass = Scheduler.class;

    @Autowired
    public Scheduler(UserRepository userRepository,
                     PostRepository postRepository,
                     ToSpecification<User> userToSpecification,
                     PostCommentRepository postCommentRepository,
                     ToSpecification<Post> postToSpecification,
                     TagRepository tagRepository,
                     ToSpecification<PostComment> postCommentToSpecification,
                     PostToTagRepository postToTagRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.userToSpecification = userToSpecification;
        this.postCommentRepository = postCommentRepository;
        this.postToSpecification = postToSpecification;
        this.tagRepository = tagRepository;
        this.postCommentToSpecification = postCommentToSpecification;
        this.postToTagRepository = postToTagRepository;
    }

    /**
     * очистка базы данных каждые {fixedRate}мс от удаленных пользователей, где isDeleted == true
     */

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void deleteProfile() {
        Specification<User> specificationUser = userToSpecification.isTrue(FieldName.IS_DELETED.getValue());
        List<User> userList = userRepository.findAll(specificationUser);
        userRepository.deleteAll(userList);
        log(loggerClass, LoggerLevel.INFO,"deleteProfile", LoggerValue.PERSON_DELETED,userList.toString());
    }

    /**
     * очистка базы данных каждые {fixedRate}мс от постов, где isDeleted == true
     */

    @Scheduled(fixedRate = 4800000)
    @Transactional
    public void deletePost() {
        Specification<Post> specification = postToSpecification.isTrue(FieldName.IS_DELETED.getValue());
        List<Post> postList = postRepository.findAll(specification);
        postRepository.deleteAll(postList);
        log(loggerClass, LoggerLevel.INFO,"deletePost", LoggerValue.POST_DELETED, postList.toString());
    }

    /**
     * очистка базы данных каждые {fixedRate}мс от комментариев, где isDeleted == true
     */

    @Scheduled(fixedRate = 5400000)
    @Transactional
    public void deleteComment() {
        Specification<PostComment> specification = postCommentToSpecification.isTrue(FieldName.IS_DELETED.getValue());
        List<PostComment> postCommentList = postCommentRepository.findAll(specification);
        postCommentRepository.deleteAll(postCommentList);
        log(loggerClass, LoggerLevel.INFO,"deleteComment", LoggerValue.COMMENT_DELETED, postCommentList.toString());
    }

    /**
     * Очистка не используемых тегов
     */
    @Scheduled(fixedRate = 6000000)
    @Transactional
    public void deleteTagsNotExistsPost() {
        tagRepository.deleteTagsNotExistsPost();
        log(loggerClass, LoggerLevel.INFO,"deleteTagsNotExistsPost", LoggerValue.TAG_DELETE, "");
    }
}
