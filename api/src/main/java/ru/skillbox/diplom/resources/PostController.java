package ru.skillbox.diplom.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.skillbox.diplom.model.PostDTO;
import ru.skillbox.diplom.model.api.request.CommentRequest;
import ru.skillbox.diplom.model.api.request.PostRequest;
import ru.skillbox.diplom.model.api.request.SearchRequest;
import ru.skillbox.diplom.model.api.response.CommentResponse;
import ru.skillbox.diplom.model.api.response.CommentResponseWithList;
import ru.skillbox.diplom.model.api.response.PostResponse;
import ru.skillbox.diplom.model.api.response.ReportResponse;
import ru.skillbox.diplom.model.api.response.Response;

public interface PostController {

    ResponseEntity<PostResponse> getById(Integer id);

    ResponseEntity<PostResponse> recoverPost(Integer id);

    ResponseEntity<PostResponse> editById(Integer id, PostRequest postRequest);


    @GetMapping("/{id}/comments")
    ResponseEntity<CommentResponseWithList> getComment(@PathVariable Integer id,
                                                       @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                                                       @RequestParam(value = "perPage", required = false, defaultValue = "20") Integer perPage);

    ResponseEntity<CommentResponse> postComment(Integer id,
                                                CommentRequest request);

    ResponseEntity<PostResponse> deleting(Integer id);


    ResponseEntity<CommentResponse> editCommentByPostIdAndCommentId(Integer id,
                                                                    Integer commentId,
                                                                    CommentRequest request);

    ResponseEntity<ReportResponse> createReportForPost(Integer id);

    ResponseEntity<ReportResponse> createReportForComment(Integer id,
                                                          Integer commentId);

    ResponseEntity<Response<PostDTO>> search(SearchRequest searchRequest);

    ResponseEntity<CommentResponse> deleteComment(Integer id,
                                                  Integer commentId);

    ResponseEntity<CommentResponse> recoverComment(Integer id,
                                                   Integer commentId);
}
