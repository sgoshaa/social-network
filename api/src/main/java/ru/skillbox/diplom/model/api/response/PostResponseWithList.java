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
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseWithList {
    private Integer offset;
    private Integer perPage;
    private Errors error;
    private Long timestamp;
    private List<PostDTO> data;
    @JsonProperty("error_description")
    private String errorDescription;
    private long total;

    public PostResponseWithList invalid() {
        error = Errors.INVALID_REQUEST;
        errorDescription = Errors.string.name();
        return this;
    }

    public PostResponseWithList getFullResponse(List<PostDTO> post, Integer offset, Integer itemPerPage) {
        timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        setError(Errors.string);
        setData(post);
        setOffset(offset);
        setPerPage(itemPerPage);
        return this;
    }

    public PostResponseWithList unauthorised() {
        error = Errors.UNAUTHORISED;
        errorDescription = Errors.string.name();
        return this;
    }

    public PostResponseWithList setResponse(List<PostDTO> post, Integer offset, Integer itemPerPage) {
        setData(post);
        setOffset(offset);
        setPerPage(itemPerPage);
        return this;
    }

}
