package ru.skillbox.diplom.model.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.skillbox.diplom.model.api.enums.DataEnums;
import ru.skillbox.diplom.model.api.enums.Errors;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationResponse {

    private String error;
    private Long timestamp;
    private Map<String, String> data;
    @JsonProperty("error_description")
    private String errorDescription;

    public RegistrationResponse applied() {
        error = Errors.NO_ERRORS.description;
        timestamp = new Date().getTime();
        data = new HashMap<>();
        data.put(DataEnums.MESSAGE.name().toLowerCase(Locale.ROOT), DataEnums.MESSAGE.answer);
        return this;
    }

    public RegistrationResponse denied() {
        error = Errors.INVALID_REQUEST.name().toLowerCase(Locale.ROOT);
        errorDescription = Errors.INVALID_REQUEST.description;
        return this;
    }
}