package ru.skillbox.diplom.model.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LogoutResponse {

    @JsonProperty("error")
    private String error;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("message")
    private String message;
}
