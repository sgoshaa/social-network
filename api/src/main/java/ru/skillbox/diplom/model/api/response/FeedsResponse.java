package ru.skillbox.diplom.model.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import ru.skillbox.diplom.model.PostDTO;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedsResponse {
    private String error;
    private Long timestamp;
    private Long total;
    private Long offset;
    private Long itemPerPage;
    private List<PostDTO> data;

    public FeedsResponse applied(List<PostDTO> data, long itemPerPage, long total, long offset){
        this.data = data;
        error = "string";
        timestamp = new Date().getTime();
        this.itemPerPage = itemPerPage;
        this.total = total;
        this. offset = offset;
        return this;
    }
}
