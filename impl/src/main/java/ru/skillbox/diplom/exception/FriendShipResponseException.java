package ru.skillbox.diplom.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendShipResponseException {

    @JsonProperty("error")
    private String error;

    @JsonProperty("error_description")
    private String errorDescription;

}
