package ru.skillbox.diplom.model.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class LikeResponse<K, V> {

    private String error;
    private long timestamp;
    @JsonProperty("error_description")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorDescription;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<K, V> data;
}
