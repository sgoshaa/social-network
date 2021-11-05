package ru.skillbox.diplom.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.model.PostDTO;
import ru.skillbox.diplom.model.api.request.CommentRequest;
import ru.skillbox.diplom.model.api.request.PostRequest;
import ru.skillbox.diplom.model.api.request.SearchRequest;
import ru.skillbox.diplom.model.api.response.CommentResponse;
import ru.skillbox.diplom.model.api.response.CommentResponseWithList;
import ru.skillbox.diplom.model.api.response.PostResponse;
import ru.skillbox.diplom.model.api.response.ReportResponse;
import ru.skillbox.diplom.model.api.response.Response;
import ru.skillbox.diplom.resources.PostController;
import ru.skillbox.diplom.service.PostService;

@RestController
@RequestMapping("/api/v1/post")
public class PostControllerImpl implements PostController {

    private final PostService postService;

    @Autowired
    public PostControllerImpl(PostService postService) {
        this.postService = postService;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getById(@PathVariable Integer id) {
        return postService.findById(id);
    }

    @Override
    @PutMapping("/{id}/recover")
    public ResponseEntity<PostResponse> recoverPost(@PathVariable Integer id) {
        return postService.recoverPost(id);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> editById(@PathVariable Integer id,
                                                 @RequestBody PostRequest postRequest) {
        return postService.editingPost(id, postRequest);
    }


    @Override
    @GetMapping("/{id}/comments")
    public ResponseEntity<CommentResponseWithList> getComment(@PathVariable Integer id,
                                                              @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset ,
                                                              @RequestParam (value = "perPage", required = false, defaultValue = "20") Integer perPage) {
        return postService.getComment(id, offset, perPage);
    }


    @Override
    @PostMapping("/{id}/comments")
    @ResponseBody
    public ResponseEntity<CommentResponse> postComment(@PathVariable Integer id,
                                                       @RequestBody CommentRequest request) {
        return postService.postComment(id, request);
    }

    @Override
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<PostResponse> deleting(@PathVariable("id") Integer id) {
        return postService.delete(id);
    }

    @Override
    @PutMapping("/{id}/comments/{commentId}")
    public ResponseEntity<CommentResponse> editCommentByPostIdAndCommentId(@PathVariable Integer id,
                                                                           @PathVariable Integer commentId,
                                                                           @RequestBody CommentRequest request) {
        return postService.editingComment(id, commentId, request);
    }

    @Override
    @PostMapping("/{id}/report")
    public ResponseEntity<ReportResponse> createReportForPost(@PathVariable Integer id) {
        return postService.createReportForPost(id);
    }

    @Override
    @PostMapping("/{id}/comments/{commentId}/report")
    public ResponseEntity<ReportResponse> createReportForComment(@PathVariable Integer id,
                                                                 @PathVariable Integer commentId) {
        return postService.createReportForComment(id, commentId);
    }


    @Override
    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<CommentResponse> deleteComment(@PathVariable Integer id,
                                                         @PathVariable Integer commentId) {
        return postService.deleteComment(id, commentId);
    }

    @Override
    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<Response<PostDTO>> search(SearchRequest searchRequest) {
        return postService.searchPost(searchRequest);
    }

    @Override
    @PutMapping("/{id}/comments/{commentId}/recover")
    public ResponseEntity<CommentResponse> recoverComment(@PathVariable Integer id,
                                                          @PathVariable Integer commentId) {
        return postService.recoverComment(id, commentId);
    }
}
