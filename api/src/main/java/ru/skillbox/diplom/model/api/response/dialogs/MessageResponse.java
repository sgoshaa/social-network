package ru.skillbox.diplom.model.api.response.dialogs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.skillbox.diplom.model.enums.ReadStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Data
public class MessageResponse {

    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date time;

    @JsonProperty(value = "author")
    private Integer author;

    @JsonProperty(value = "recipient")
    private Integer recipient;

    @JsonProperty(value = "message_text")
    private String messageText;

    @JsonProperty(value = "read_status")
    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;
}
