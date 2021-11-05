package ru.skillbox.diplom.model.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.model.PostDTO;
import ru.skillbox.diplom.model.api.enums.Errors;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponse {
    private Errors error = Errors.string;
    private long timestamp;
    private PostDTO data;
    @JsonProperty("error_description")
    private String errorDescription;


    public PostResponse getFullResponse(PostDTO post) {
        timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        setData(post);
        return this;
    }

    public PostResponse unauthorised() {
        error = Errors.UNAUTHORISED;
        errorDescription = Errors.string.name();
        return this;
    }

    public PostResponse invalid() {
        error = Errors.INVALID_REQUEST;
        errorDescription = Errors.string.name();
        return this;
    }

    //ToDo mapper
    public PostResponse posted(PostDTO post) {
        timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        data = post;
        return this;
    }
}
