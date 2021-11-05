package ru.skillbox.diplom.model.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.skillbox.diplom.model.enums.Type;

@Data
public class NotificationRequest {

    @JsonProperty("notification_type")
    private Type type;
    private boolean enable;
}
