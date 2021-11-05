package ru.skillbox.diplom.model.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.skillbox.diplom.model.NotificationSettingDTO;
import ru.skillbox.diplom.model.api.enums.Errors;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NotificationSettingsResponse {
    private Errors error;
    @JsonProperty("error_description")
    private String errorDescription;
    private LocalDateTime timestamp;
    private List<NotificationSettingDTO> data;

    public NotificationSettingsResponse applied(List<NotificationSettingDTO> data) {
        error = Errors.string;
        this.data = data;
        timestamp = LocalDateTime.now();
        return this;
    }

    public NotificationSettingsResponse denied() {
        error = Errors.INVALID_REQUEST;
        errorDescription = Errors.string.name();
        return this;
    }
}
