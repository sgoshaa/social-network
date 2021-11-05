package ru.skillbox.diplom.model.api.response.dialogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DialogPerson {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("photo")
    private String photo;

    @JsonProperty("last_online_time")
    private LocalDateTime lastOnlineTime;
}
