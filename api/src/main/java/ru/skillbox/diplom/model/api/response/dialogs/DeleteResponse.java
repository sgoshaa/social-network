package ru.skillbox.diplom.model.api.response.dialogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.skillbox.diplom.model.api.enums.Errors;

@Data
public class DeleteResponse {

    @JsonProperty("error")
    private Errors error;

    @JsonProperty("timestamp")
    private Long timestamp;

    @JsonProperty("data")
    private PostDialog data;
}
