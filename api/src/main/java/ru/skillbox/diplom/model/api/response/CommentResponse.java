package ru.skillbox.diplom.model.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.model.CommentDTO;
import ru.skillbox.diplom.model.PostDTO;
import ru.skillbox.diplom.model.api.enums.Errors;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponse {
    private Errors error = Errors.string;
    @JsonProperty("error_description")
    private String errorDescription;
    private Long timestamp;
    private int  total;
    private int offset;
    private int perPage;
    private CommentDTO data;

    public CommentResponse invalid() {
        error = Errors.INVALID_REQUEST;
        errorDescription = Errors.string.name();
        return this;
    }

    public CommentResponse getFullResponse(CommentDTO commentDTO) {
        timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        setData(commentDTO);
        return this;
    }

    public CommentResponse getFullResponse(Integer id) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(id);
        timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        setData(commentDTO);
        return this;
    }

    public CommentResponse unauthorised() {
        error = Errors.UNAUTHORISED;
        errorDescription = Errors.string.name();
        return this;
    }
}
