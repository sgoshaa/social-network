package ru.skillbox.diplom.model.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRequest {

    @JsonProperty("parent_id")
    private Integer parent_id;
    @JsonProperty("comment_text")
    private String comment_text;

}
