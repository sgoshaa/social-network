package ru.skillbox.diplom.model.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.skillbox.diplom.model.api.enums.Errors;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class ReportResponse {
    private Errors error = Errors.string;
    @JsonProperty("error_description")
    private String errorDescription;
    private Long timestamp;
    private String data;

    public ReportResponse invalid() {
        error = Errors.INVALID_REQUEST;
        errorDescription = Errors.string.name();
        return this;
    }

    public ReportResponse getFullResponse() {
        timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        data = "ok";
        return this;
    }

    public ReportResponse unauthorised() {
        error = Errors.UNAUTHORISED;
        errorDescription = Errors.string.name();
        return this;
    }
}
