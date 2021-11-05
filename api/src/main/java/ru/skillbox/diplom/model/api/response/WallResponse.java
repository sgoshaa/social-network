package ru.skillbox.diplom.model.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.model.api.enums.Errors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WallResponse {

    private PostResponseWithList posted;
    private PostResponseWithList queued;
    private Errors error = Errors.string;
    private long timestamp;
    @JsonProperty("error_description")
    private String errorDescription;


    public WallResponse invalid() {
        error = Errors.INVALID_REQUEST;
        errorDescription = Errors.string.name();
        return this;
    }

    public WallResponse unauthorised() {
        error = Errors.UNAUTHORISED;
        errorDescription = Errors.string.name();
        return this;
    }

    public WallResponse getResponse(PostResponseWithList posted, PostResponseWithList queued) {
        timestamp = System.currentTimeMillis();
        setPosted(posted);
        setQueued(queued);
        return this;
    }

}
