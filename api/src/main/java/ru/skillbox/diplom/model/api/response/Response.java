package ru.skillbox.diplom.model.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.model.api.enums.Errors;

import java.util.Collection;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    @JsonProperty("error")
    private Errors error;

    @JsonProperty("timestamp")
    private Long timestamp;

    @JsonProperty("total")
    private Long total;

    @JsonProperty("offset")
    private int offset;

    @JsonProperty("perPage")
    private int perPage;

    @JsonProperty("data")
    private Collection<T> data;
}
