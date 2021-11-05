package ru.skillbox.diplom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.model.CommentLike;
import ru.skillbox.diplom.model.Person;
import ru.skillbox.diplom.model.Post;
import ru.skillbox.diplom.model.PostComment;
import ru.skillbox.diplom.model.PostLike;
import ru.skillbox.diplom.model.User;
import ru.skillbox.diplom.model.api.request.LikeRequest;
import ru.skillbox.diplom.model.api.response.LikeResponse;
import ru.skillbox.diplom.repository.CommentLikeRepository;
import ru.skillbox.diplom.repository.PersonRepository;
import ru.skillbox.diplom.repository.PostCommentRepository;
import ru.skillbox.diplom.repository.PostLikeRepository;
import ru.skillbox.diplom.repository.PostRepository;
import ru.skillbox.diplom.util.UserUtility;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LikeService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostLikeRepository postLikeRepository;
    private final PersonRepository personRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Autowired
    public LikeService(PostRepository postRepository, PostCommentRepository postCommentRepository, PostLikeRepository postLikeRepository, PersonRepository personRepository, CommentLikeRepository commentLikeRepository) {
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
        this.postLikeRepository = postLikeRepository;
        this.personRepository = personRepository;
        this.commentLikeRepository = commentLikeRepository;
    }

    public LikeResponse liked(int userId, int itemId, String type) {
        LikeResponse response = new LikeResponse();
        User user = UserUtility.getUser();
        if (user == null) {
            response.setError("invalid_request");
            response.setErrorDescription("unauthorized");
            return response;
        }
        Map<String, Boolean> data = new HashMap<>();
        switch (type) {
            case "Post":
                PostLike postLike = postLikeRepository.findByPostId(itemId).orElse(null);
                data.put("likes", postLike != null && postLike.getPersonId().getId().equals(userId));
                break;
            case "Comment":
                CommentLike commentLike = commentLikeRepository.findByPostCommentId(itemId).orElse(null);
                data.put("likes", commentLike != null && commentLike.getPersonId().getId().equals(userId));
                break;
            default:
                response.setError("invalid_request");
                response.setErrorDescription("Bad Request");
                return response;
        }
        response.setError("no errors");
        response.setTimestamp(System.currentTimeMillis());
        response.setData(data);
        return response;
    }

    public LikeResponse getLikes(int itemId, String type) {
        LikeResponse response = new LikeResponse();
        User user = UserUtility.getUser();
        if (user == null) {
            response.setError("invalid_request");
            response.setErrorDescription("unauthorized");
            return response;
        }
        Map data = new HashMap<>();
        switch (type) {
            case "Post":
                Post post = postRepository.findById(itemId).orElse(null);
                if (post != null) {
                    data.put("likes", String.valueOf(post.getLikes().size()));
                    List<String> users = new ArrayList<>();
                    post.getLikes().forEach(like -> users.add(String.valueOf(like.getPersonId().getId())));
                    data.put("users", users.toArray());
                }
                break;
            case "Comment":
                PostComment postComment = postCommentRepository.findById(itemId).orElse(null);
                if (postComment != null) {
                    data.put("likes", String.valueOf(postComment.getLikes().size()));
                    List<String> users = new ArrayList<>();
                    postComment.getLikes().forEach(commentLike -> users.add(String.valueOf(commentLike.getPersonId().getId())));
                    data.put("users", users.toArray());
                }
                break;
            default:
                response.setError("invalid_request");
                response.setErrorDescription("Bad Request");
                return response;
        }
        response.setError("no errors");
        response.setTimestamp(System.currentTimeMillis());
        response.setData(data);
        return response;
    }

    public LikeResponse putLike(LikeRequest request) {
        LikeResponse response = new LikeResponse();
        User user = UserUtility.getUser();
        Person person;
        if (user == null) {
            response.setError("invalid_request");
            response.setErrorDescription("unauthorized");
            return response;
        }
        person = personRepository.getOne(user.getId());
        String type = request.getType();
        switch (type) {
            case "Post":
                Post post = postRepository.findById(request.getItemId()).orElse(null);
                if (post != null && postLikeRepository.findByPostAndPerson(post, person).isEmpty()) {
                    PostLike postLike = new PostLike();
                    postLike.setPostId(post);
                    postLike.setPersonId(person);
                    postLike.setTime(LocalDateTime.now());
                    postLikeRepository.save(postLike);
                    return getLikes(post.getId(), "Post");
                }
                break;
            case "Comment":
                PostComment postComment = postCommentRepository.findById(request.getItemId()).orElse(null);
                if (postComment != null && commentLikeRepository.findByPostCommentAndPerson(postComment, person).isEmpty()) {
                    CommentLike commentLike = new CommentLike();
                    commentLike.setPostCommentId(postComment);
                    commentLike.setPersonId(person);
                    commentLike.setTime(LocalDateTime.now());
                    commentLikeRepository.save(commentLike);
                    return getLikes(postComment.getId(), "Comment");
                }
                break;
        }
        response.setError("invalid_request");
        response.setErrorDescription("Bad Request");
        return response;
    }

    public LikeResponse deleteLike(int itemId, String type) {
        LikeResponse response = new LikeResponse();
        User user = UserUtility.getUser();
        Person person;
        if (user == null) {
            response.setError("invalid_request");
            response.setErrorDescription("unauthorized");
            return response;
        }
        person = personRepository.getOne(user.getId());
        Map data = new HashMap<>();
        switch (type) {
            case "Post":
                Post post = postRepository.findById(itemId).orElse(null);
                if (post != null && postLikeRepository.findByPostAndPerson(post, person).isPresent()) {
                    postLikeRepository.deleteByPostAndPerson(post, person);
                    data.put("likes", String.valueOf(post.getLikesAmount()));
                }
                break;
            case "Comment":
                PostComment postComment = postCommentRepository.findById(itemId).orElse(null);
                if (postComment != null && commentLikeRepository.findByPostCommentAndPerson(postComment, person).isPresent()) {
                    commentLikeRepository.deleteByPostCommentAndPerson(postComment, person);
                    data.put("likes", String.valueOf(postComment.getLikesAmount()));
                }
                break;
            default:
                response.setError("invalid_request");
                response.setErrorDescription("Bad Request");
                return response;
        }
        response.setError("no errors");
        response.setTimestamp(System.currentTimeMillis());
        response.setData(data);
        return response;
    }

}
