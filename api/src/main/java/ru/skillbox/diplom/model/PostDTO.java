package ru.skillbox.diplom.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {
    private Integer id;
    private Long time;
    private PersonDTO author;
    private String title;
    @JsonProperty("post_text")
    private String postText;
    @JsonProperty("is_blocked")
    private boolean isBlocked;
    private Integer likes;
    @JsonProperty("tags")
    private List<String> tags;
    @JsonProperty("comments")
    private List<CommentDTO> postComments;
    private String type;
}
