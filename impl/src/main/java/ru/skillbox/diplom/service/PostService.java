package ru.skillbox.diplom.service;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.exception.InvalidRequest;
import ru.skillbox.diplom.mappers.CommentMapper;
import ru.skillbox.diplom.mappers.DefaultResponseMapper;
import ru.skillbox.diplom.mappers.PersonMapper;
import ru.skillbox.diplom.mappers.PostMapper;
import ru.skillbox.diplom.mappers.converter.DateConverter;
import ru.skillbox.diplom.model.CommentDTO;
import ru.skillbox.diplom.model.CommentReport;
import ru.skillbox.diplom.model.Person;
import ru.skillbox.diplom.model.PersonDTO;
import ru.skillbox.diplom.model.Post;
import ru.skillbox.diplom.model.PostComment;
import ru.skillbox.diplom.model.PostDTO;
import ru.skillbox.diplom.model.PostReport;
import ru.skillbox.diplom.model.User;
import ru.skillbox.diplom.model.api.request.CommentRequest;
import ru.skillbox.diplom.model.api.request.PostRequest;
import ru.skillbox.diplom.model.api.request.SearchRequest;
import ru.skillbox.diplom.model.api.response.CommentResponse;
import ru.skillbox.diplom.model.api.response.CommentResponseWithList;
import ru.skillbox.diplom.model.api.response.PostResponse;
import ru.skillbox.diplom.model.api.response.PostResponseWithList;
import ru.skillbox.diplom.model.api.response.ReportResponse;
import ru.skillbox.diplom.model.api.response.Response;
import ru.skillbox.diplom.model.enums.Type;
import ru.skillbox.diplom.model.api.response.WallResponse;
import ru.skillbox.diplom.repository.CommentReportRepository;
import ru.skillbox.diplom.repository.PostCommentRepository;
import ru.skillbox.diplom.repository.PostReportRepository;
import ru.skillbox.diplom.repository.PostRepository;
import ru.skillbox.diplom.repository.specification.ToSpecification;
import ru.skillbox.diplom.repository.specification.enums.FieldName;
import ru.skillbox.diplom.service.enums.LoggerLevel;
import ru.skillbox.diplom.service.enums.LoggerValue;
import ru.skillbox.diplom.util.FilterEntity;
import ru.skillbox.diplom.util.Paging;
import ru.skillbox.diplom.util.UserUtility;
import ru.skillbox.diplom.util.enums.ParametersFilter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class PostService implements SocialNetworkService {

    private final Class<PostService> loggerClass = PostService.class;
    private final PostMapper postMapper = Mappers.getMapper(PostMapper.class);
    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);
    private final PostRepository postRepository;
    private final AuthService authService;
    private final AccountService accountService;
    private final PostCommentRepository postCommentRepository;
    private final PostReportRepository postReportRepository;
    private final CommentReportRepository commentReportRepository;
    private final ToSpecification<Post> toSpecPost;
    private final TagService tagService;
    private final DateConverter dateConverter;
    private final FilterEntity filterEntity;
    private final NotificationsService notificationsService;
    private final FriendShipService friendShipService;

    @Autowired
    public PostService(PostRepository postRepository,
                       AuthService authService,
                       TagService tagService,
                       AccountService accountService,
                       PostCommentRepository postCommentRepository,
                       PostReportRepository postReportRepository,
                       CommentReportRepository commentReportRepository,
                       ToSpecification<Post> toSpecPost,
                       DateConverter dateConverter,
                       NotificationsService notificationsService,
                       FriendShipService friendShipService,
                       FilterEntity filterEntity) {
        this.postRepository = postRepository;
        this.authService = authService;
        this.accountService = accountService;
        this.postCommentRepository = postCommentRepository;
        this.postReportRepository = postReportRepository;
        this.commentReportRepository = commentReportRepository;
        this.toSpecPost = toSpecPost;
        this.tagService = tagService;
        this.dateConverter = dateConverter;
        this.notificationsService = notificationsService;
        this.friendShipService = friendShipService;
        this.filterEntity = filterEntity;
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Transactional
    public PostResponse post(PostRequest request, Person user, Long publishDate) {
        log(loggerClass, LoggerLevel.INFO, "post", LoggerValue.POST_REQUEST, request.getTitle());
        PersonDTO personDTO = accountService.getPersonDTO(user);
        PostResponse response = new PostResponse();
        LocalDateTime dateTime = publishDate != null ?
                dateConverter.longToDate(publishDate)
                : LocalDateTime.now();
        Post post = postMapper.convert(request, user, dateTime);
        postRepository.save(post);
        //добавляем нотификации
        notificationsService.createNotificationsToFriends(Type.POST, friendShipService.getListFriends(user.getId()), post);
        //
        tagService.saveTags(request.getTags(), post);
        log(loggerClass, LoggerLevel.DEBUG, "post", LoggerValue.POST_ERROR, String.valueOf(post.getId()));
        return response.posted(postMapper.convert(post, personDTO));
    }


    @Transactional
    public ResponseEntity<PostResponse> delete(Integer id) {
        log(loggerClass, LoggerLevel.INFO, "delete", LoggerValue.POST_REQUEST, String.valueOf(id));
        PostResponse response = new PostResponse();
        Optional<Post> optional = postRepository.findById(id);
        boolean postExists = optional.isPresent();
        Person person = accountService.getPerson(authService.getCurrentUser().getId());
        if (person == null) {
            return ResponseEntity.badRequest().body(response.unauthorised());
        }
        if (postExists) {
            Post post = optional.get();
            if (post.getAuthor().getId() == person.getId()) {
                log(loggerClass, LoggerLevel.INFO, "delete", LoggerValue.POST_DELETED, post.getTitle());
                post.setDeleted(true);
                return ResponseEntity.ok(response.getFullResponse(postMapper.convert(post,
                        personMapper.toPersonDTO(person))));
            } else {
                log(loggerClass, LoggerLevel.DEBUG, "delete", LoggerValue.POST_ERROR, post.getTitle());
                return ResponseEntity.status(401).body(response.unauthorised());
            }
        }
        log(loggerClass, LoggerLevel.ERROR, "delete", LoggerValue.POST_ERROR, String.valueOf(id));
        return ResponseEntity.badRequest().body(response.invalid());
    }

    @Transactional
    public ResponseEntity<PostResponse> findById(Integer id) {
        PostResponse response = new PostResponse();
        Optional<Post> optional = postRepository.findById(id);
        boolean postExists = optional.isPresent();
        Person person = accountService.getPerson(authService.getCurrentUser().getId());
        if (person == null) {
            return ResponseEntity.badRequest().body(response.unauthorised());
        }
        if (postExists && !optional.get().isDeleted()) {
            Post post = optional.get();
            log(loggerClass, LoggerLevel.WARN, "findById", LoggerValue.POST_DELETED, String.valueOf(id));
            return ResponseEntity.ok(response.getFullResponse(postMapper.convert(post,
                    personMapper.toPersonDTO(person))));
        }
        log(loggerClass, LoggerLevel.WARN, "findById", LoggerValue.POST_ERROR, id + " doesn't exist.");
        return ResponseEntity.badRequest().body(response.invalid());
    }


    //редактируем
    @Transactional
    public ResponseEntity<PostResponse> editingPost(Integer id, PostRequest postRequest) {
        PostResponse response = new PostResponse();
        Optional<Post> optional = postRepository.findById(id);
        boolean postExists = optional.isPresent();
        Person user = accountService.getPerson(authService.getCurrentUser().getId());
        if (user == null) {
            return ResponseEntity.badRequest().body(response.unauthorised());
        }
        if (postExists && !optional.get().isDeleted()) {
            Post post = optional.get();
            System.out.println(user.getId() + " " + post.getAuthor().getId());
            if (!user.getId().equals(post.getAuthor().getId())) {
                return ResponseEntity.badRequest().body(response.invalid());
            }
            tagService.editTags(postRequest.getTags(), post);
            post.setTitle(postRequest.getTitle());
            post.setPostText(postRequest.getPostText());
            postRepository.save(post);
            log(loggerClass, LoggerLevel.WARN, "editingPost", LoggerValue.POST_EDIT, id.toString());
            return ResponseEntity.ok(response.getFullResponse(postMapper.convert(post, personMapper.toPersonDTO(user))));
        }
        log(loggerClass, LoggerLevel.WARN, "editingPost", LoggerValue.POST_ERROR, id.toString());
        return ResponseEntity.badRequest().body(response.invalid());
    }

    //восстановление поста
    @Transactional
    public ResponseEntity<PostResponse> recoverPost(Integer id) {
        PostResponse response = new PostResponse();
        Optional<Post> optional = postRepository.findById(id);
        Person user = accountService.getPerson(authService.getCurrentUser().getId());
        Person person = accountService.getPerson(authService.getCurrentUser().getId());
        if (person == null) {
            return ResponseEntity.badRequest().body(response.unauthorised());
        }
        boolean postExists = optional.isPresent();
        if (postExists && optional.get().isDeleted()) {
            Post post = optional.get();
            if (!user.getId().equals(post.getAuthor().getId())) {
                ResponseEntity.badRequest().body(response.invalid());
            }
            post.setDeleted(false);
            postRepository.save(post);
            return ResponseEntity.ok(response.getFullResponse(postMapper.convert(post, personMapper.toPersonDTO(user))));
        }
        log(loggerClass, LoggerLevel.WARN, "recoverPost", LoggerValue.POST_ERROR, id.toString());
        return ResponseEntity.badRequest().body(response.invalid());
    }

    @Transactional
    public ResponseEntity<CommentResponseWithList> getComment(Integer id, Integer offset, Integer itemPerPage) {
        Optional<Post> optional = postRepository.findById(id);
        boolean postExists = optional.isPresent();
        CommentResponseWithList response = new CommentResponseWithList();

        if (postExists && !optional.get().isDeleted()) {
            Post post = optional.get();
            List<CommentDTO> commentDTOS = new ArrayList<>();
            Pageable pageable = Paging.getPaging(offset, itemPerPage);
            Page<PostComment> commentPage = postCommentRepository.findAllByPostId(post.getId(), pageable);
            commentPage.stream().filter(comment ->
                            comment.isDeleted() == false && !comment.isSub())
                    .forEach(comment -> {
                        comment.getSubComments().removeIf(c -> c.isDeleted());
                        commentDTOS.add(commentMapper.convert(comment));
                    });
            response.setTimestamp(dateConverter.convertRegDate(LocalDateTime.now()));
            response.addToData(commentDTOS);
            response.setOffset(offset);
            response.setPerPage(itemPerPage);
            response.setTotal(commentPage.getTotalElements());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response.invalid());
    }

    @Transactional
    public ResponseEntity<CommentResponse> postComment(Integer id, CommentRequest request) {
        CommentResponse response = new CommentResponse();
        Optional<Post> optional = postRepository.findById(id);
        boolean postExists = optional.isPresent();
        Person user = accountService.getPerson(accountService.findByCurrentEmail().getId());
        if (user == null) {
            return ResponseEntity.badRequest().body(response.unauthorised());
        }
        if (postExists && !optional.get().isDeleted()) {

            Post post = optional.get();
            PostComment postComment = new PostComment();
            postComment.setPost(post);
            postComment.setPerson(user);
            postComment.setDeleted(false);
            postComment.setCommentText(request.getComment_text());
            postComment.setParentId(request.getParent_id() != null ? postCommentRepository.findById(request.getParent_id()).get() : null);
            postComment.setTime(LocalDateTime.now());
            postComment.setSub(request.getParent_id() != null ? true : false);
            System.out.println(post.getPostText());
            System.out.println(user.getFirstName());
            postCommentRepository.save(postComment);
            //создаем нотификацию
            createNotificationToPostComment(postComment,user);
            //
            CommentDTO commentDTO = commentMapper.convert(postComment);
            return ResponseEntity.ok(response.getFullResponse(commentDTO));
        }
        return ResponseEntity.badRequest().body(response.invalid());
    }

    @Transactional
    public ResponseEntity<Response<PostDTO>> searchPost(SearchRequest searchRequest) {
        log(loggerClass, LoggerLevel.INFO, "searchPost", LoggerValue.SEARCHING_REQUEST, searchRequest.toString());
        if  (Objects.isNull(accountService.findByCurrentEmail())) {
            throw new InvalidRequest("unauthorised");
        }
        DefaultResponseMapper<PostDTO> responseMapper = new DefaultResponseMapper<>();
        if (searchRequest.requestIsNull(Post.class)) {
            return ResponseEntity.ok(responseMapper.convert(new ArrayList<>(),0, searchRequest.getItemPerPage()));
        }
        LocalDateTime dateFrom = dateConverter.longToDate(searchRequest.getDate_from());
        LocalDateTime dateTo = dateConverter.longToDate(searchRequest.getDate_to());
        Specification<Post> specification = (toSpecPost.contains(FieldName.POST_TEXT.getValue(), searchRequest.getText())
                .or(toSpecPost.contains(FieldName.TITLE.getValue(), searchRequest.getText())))
                .and(toSpecPost.between(FieldName.TIME.getValue(), dateFrom, dateTo))
                .and(toSpecPost.joinAuthor(FieldName.AUTHOR.getValue(), searchRequest.getAuthor()))
                .and(toSpecPost.isFalse(FieldName.IS_DELETED.getValue()));
        Pageable pageable = Paging.getPaging(searchRequest.getOffset(), searchRequest.getItemPerPage(),
                Sort.by(FieldName.TIME.getValue()).descending());
        filterEntity.enableFilter(ParametersFilter.IS_DELETED.getName(), ParametersFilter.IS_DELETED.getNameParameter(), false);
        Page<Post> postPage = postRepository.findAll(specification, pageable);
        List<PostDTO> postDTOList = postPage.stream()
                .map(postMapper::convert)
                .collect(Collectors.toList());
        filterEntity.disableFilter(FieldName.IS_DELETED.getValue());
        Response<PostDTO> response = responseMapper.convert(postDTOList, postPage.getTotalElements(), searchRequest.getItemPerPage());
        log(loggerClass, LoggerLevel.INFO, "searchPost", LoggerValue.SEARCHING_RESPONSE, response.toString());
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<CommentResponse> editingComment(Integer postId, Integer commentId, CommentRequest request) {
        Optional<Post> optional = postRepository.findById(postId);
        boolean postExists = optional.isPresent();
        CommentResponse response = new CommentResponse();
        if (postExists && !optional.get().isDeleted()) {
            Post post = optional.get();
            if (post.getPostComments()
                    .stream()
                    .anyMatch(comment -> comment.getId().equals(commentId) && comment.isDeleted() == false)) {
                PostComment postComment = postCommentRepository.findById(commentId).get();
                // postComment.setParentId(request.getParent_id() == null ? postCommentRepository.findById(request.getParent_id()).get() : null);
                postComment.setCommentText(request.getComment_text());
                postCommentRepository.save(postComment);

                if (!postComment.isSub()) {
                    CommentDTO commentDTO = commentMapper.convert(postComment);
                    return ResponseEntity.ok(response.getFullResponse(commentDTO));
                } else {
                    CommentDTO commentDTO = commentMapper.convert(postComment.getParentId());
                    return ResponseEntity.ok(response.getFullResponse(commentDTO));
                }
            } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.invalid());
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.invalid());
    }

    @Transactional
    public ResponseEntity<ReportResponse> createReportForPost(Integer id) {
        ReportResponse response = new ReportResponse();
        Optional<Post> optional = postRepository.findById(id);
        boolean postExists = optional.isPresent();
        Person user = accountService.getPerson(accountService.findByCurrentEmail().getId());
        if (user == null) {
            return ResponseEntity.badRequest().body(response.unauthorised());
        }
        if (postExists && !optional.get().isDeleted()) {
            PostReport postReport = new PostReport();
            postReport.setPostId(optional.get().getId());
            postReport.setPersonId(user.getId());
            postReport.setTime(LocalDateTime.now());
            postReportRepository.save(postReport);
            return ResponseEntity.ok(response.getFullResponse());
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.invalid());
    }

    @Transactional
    public ResponseEntity<ReportResponse> createReportForComment(Integer id, Integer commentId) {
        ReportResponse response = new ReportResponse();
        Optional<Post> optional = postRepository.findById(id);
        boolean postExists = optional.isPresent();
        Person user = accountService.getPerson(accountService.findByCurrentEmail().getId());
        if (user == null) {
            return ResponseEntity.badRequest().body(response.unauthorised());
        }
        if (postExists && !optional.get().isDeleted()) {
            if (optional.get().getPostComments()
                    .stream()
                    .anyMatch(postComment -> postComment.getId().equals(commentId) && postComment.isDeleted() == false)) {
                CommentReport report = new CommentReport();
                report.setCommentId(commentId);
                report.setPersonId(user.getId());
                report.setTime(LocalDateTime.now());
                commentReportRepository.save(report);
                return ResponseEntity.ok(response.getFullResponse());
            } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.invalid());
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.invalid());
    }

    @Transactional
    public ResponseEntity<CommentResponse> deleteComment(Integer id, Integer commentId) {
        CommentResponse response = new CommentResponse();
        Optional<Post> optional = postRepository.findById(id);
        boolean postExists = optional.isPresent();
        Person user = accountService.getPerson(accountService.findByCurrentEmail().getId());
        if (user == null) {
            return ResponseEntity.badRequest().body(response.unauthorised());
        }
        if (postExists && !optional.get().isDeleted()) {
            PostComment postComment = postCommentRepository.findById(commentId).get();
            postComment.setDeleted(true);
            postComment.getSubComments().forEach(p -> p.setDeleted(true));
            postCommentRepository.save(postComment);
            return ResponseEntity.ok(response.getFullResponse(commentId));
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.invalid());
    }

    @Transactional
    public ResponseEntity<CommentResponse> recoverComment(Integer id, Integer commentId) {
        CommentResponse response = new CommentResponse();
        Optional<Post> optional = postRepository.findById(id);
        Person person = accountService.getPerson(authService.getCurrentUser().getId());
        if (person == null) {
            return ResponseEntity.badRequest().body(response.unauthorised());
        }
        if (!postCommentRepository.existsById(id) || !postRepository.existsById(id)) {
            return ResponseEntity.badRequest().body(response.unauthorised());
        }
        boolean postExists = optional.isPresent();
        if (postExists && optional.get().isDeleted()) {
            if (person
                    .getPosts()
                    .stream()
                    .anyMatch(pers -> pers
                            .getPostComments()
                            .stream()
                            .anyMatch(postComment ->
                                    postComment.getId().equals(commentId) && postComment.isDeleted()))) {
                PostComment postComment = postCommentRepository.findById(commentId).get();
                postComment.setDeleted(false);
                postCommentRepository.save(postComment);
                return ResponseEntity.ok(response.getFullResponse(commentMapper.convert(postComment)));
            }
        }
        return ResponseEntity.badRequest().body(response.invalid());
    }

    public ResponseEntity<WallResponse> getWallWithPagination(Person person, int offset, int itemPerPage) {
        WallResponse response = new WallResponse();
        User user = UserUtility.getUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.unauthorised());
        }
        PostResponseWithList posted = new PostResponseWithList();
        PostResponseWithList queued = new PostResponseWithList();

        try {
            Specification<Post> postSpecification = toSpecPost.between(FieldName.TIME.getValue(), null, LocalDateTime.now())
                    .and(toSpecPost.isFalse(FieldName.IS_DELETED.getValue()))
                    .and(toSpecPost.equals(FieldName.AUTHOR.getValue(), person));
            filterEntity.enableFilter(ParametersFilter.IS_DELETED.getName(), ParametersFilter.IS_DELETED.getNameParameter(), false);

            Page<Post> postedPostPage = postRepository.findAll(postSpecification,
                    PageRequest.of((offset / itemPerPage), itemPerPage, Sort.by("time").descending()));

            List<PostDTO> postedPostDTOList = postedPostPage.stream().map(postMapper::convert).collect(Collectors.toList());
            posted.setTotal(postedPostPage.getTotalElements());
            posted.setResponse(postedPostDTOList, offset, itemPerPage);

            if (user.getId().equals(person.getId())) {
                postSpecification = toSpecPost.greaterThan(FieldName.TIME.getValue(), LocalDateTime.now())
                        .and(toSpecPost.isFalse(FieldName.IS_DELETED.getValue()))
                        .and(toSpecPost.equals(FieldName.AUTHOR.getValue(), person));

                Page<Post> queuedPostPage = postRepository.findAll(postSpecification,
                        PageRequest.of((offset / itemPerPage), itemPerPage, Sort.by("time").ascending()));

                List<PostDTO> queuedPostDTOList = queuedPostPage.stream().map(postMapper::convert).collect(Collectors.toList());
                queued.setTotal(queuedPostPage.getTotalElements());
                queued.setResponse(queuedPostDTOList, offset, itemPerPage);
            } else {
                filterEntity.disableFilter(ParametersFilter.IS_DELETED.getName());
                queued = null;
            }
        } catch (InvalidRequest i) {
            filterEntity.disableFilter(ParametersFilter.IS_DELETED.getName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.invalid());
        }
        filterEntity.disableFilter(ParametersFilter.IS_DELETED.getName());
        return ResponseEntity.ok(response.getResponse(posted, queued));
    }

    private void createNotificationToPostComment(PostComment postComment,Person user){
        if (postComment.isSub()){
            List<PostComment> commentComments = postComment.getParentId().getSubComments();
            Person previousCommentator = null;
            if (commentComments.size() == 1){
                previousCommentator = postComment.getParentId().getPerson();
            }else{
                previousCommentator = commentComments.get(commentComments.size()-2).getPerson();
            }
            if (!previousCommentator.equals(user)) {
                notificationsService.createNotification(Type.COMMENT_COMMENT, previousCommentator.getId(),
                        postComment, "email: " + previousCommentator.getEmail() + " phone: " + previousCommentator.getPhone());
            }
        }else {
            notificationsService.createNotification(Type.POST_COMMENT, postComment.getPost().getAuthor().getId(), postComment,
                    "email: " + postComment.getPerson().getEmail()+ " phone: " + postComment.getPerson().getPhone());
        }
    }
}


