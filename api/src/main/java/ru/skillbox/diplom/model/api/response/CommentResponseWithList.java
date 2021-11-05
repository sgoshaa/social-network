package ru.skillbox.diplom.model.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.skillbox.diplom.model.CommentDTO;
import ru.skillbox.diplom.model.api.enums.Errors;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseWithList {
    private Errors error = Errors.string;
    @JsonProperty("error_description")
    private String errorDescription;
    private Long timestamp;
    private Long total;
    private Integer offset;
    private Integer perPage;
    private List<CommentDTO> data = new ArrayList<>();


    public void addToData(CommentDTO comment){
        timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        data.add(comment);
    }

    public void addToData(List<CommentDTO> comment){
        timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        data.addAll(comment);
    }


    public void addToData(Integer id){
        timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        CommentDTO comment = new CommentDTO();
        comment.setId(id);
        data.add(comment);
    }


public CommentResponseWithList invalid() {
    error = Errors.INVALID_REQUEST;
    errorDescription = Errors.string.name();
    return this;
}
}
