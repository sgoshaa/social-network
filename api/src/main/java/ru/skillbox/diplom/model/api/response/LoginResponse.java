package ru.skillbox.diplom.model.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.skillbox.diplom.model.PersonDTO;
import ru.skillbox.diplom.model.api.enums.Errors;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private Errors error;
    @JsonProperty("error_description")
    private String errorDescription;
    private LocalDateTime timestamp;
    private PersonDTO data;

    public LoginResponse applied(PersonDTO data) {
        error = Errors.string;
        this.data = data;
        timestamp = LocalDateTime.now();
        return this;
    }

    public LoginResponse denied() {
        error = Errors.INVALID_REQUEST;
        errorDescription = Errors.string.name();
        return this;
    }
}
