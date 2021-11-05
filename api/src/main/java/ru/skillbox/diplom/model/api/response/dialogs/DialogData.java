package ru.skillbox.diplom.model.api.response.dialogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DialogData {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("unread_count")
    private Integer unreadCount;

    @JsonProperty("last_message")
    private LastMessage lastMessage;
}
