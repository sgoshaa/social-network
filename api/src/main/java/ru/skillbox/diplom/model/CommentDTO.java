package ru.skillbox.diplom.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    @JsonProperty("parent_id")
    private Integer parentId;
    @JsonProperty("comment_text")
    private String commentText;
    private int id;
    @JsonProperty("post_id")
    private Integer postId;
    private Long time;
    @JsonProperty("author")
    private PersonDTO author;
    @JsonProperty("is_blocked")
    private boolean isBlocked;
    private boolean isDeleted;
    @JsonProperty("sub_comments")
    private List<CommentDTO> subComments = new ArrayList<>();
    private Integer likes;


}


