package ru.skillbox.diplom.model.api.response.dialogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DialogResponse {

    private Integer id;

    @JsonProperty(value = "unread_count")
    private Integer unreadCount;

    @JsonProperty(value = "last_message")
    private LastMessage lastMessage;
}